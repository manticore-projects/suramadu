package org.webswing.server.api.services.playback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.model.appframe.in.PlaybackCommandMsgIn;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.PlaybackInfoMsgOut;
import org.webswing.server.api.services.websocket.RecordingWebSocketConnection;
import org.webswing.server.common.datastore.WebswingDataStoreModule;
import org.webswing.server.common.datastore.WebswingDataStoreType;
import org.webswing.server.common.util.ProtoMapper;
import org.webswing.util.SessionRecordingHeader;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionRecordingPlayback {
  private static final Logger log = LoggerFactory.getLogger(SessionRecordingPlayback.class);

  private final ProtoMapper protoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_APPFRAME_IN,
      ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT);

  private int numberOfFrames;
  private final ScheduledExecutorService sender = Executors.newSingleThreadScheduledExecutor();

  private int currentFrame;
  private int allowedFrame = 1;

  private RecordingWebSocketConnection connection;
  private WebswingDataStoreModule dataStore;
  private String fileName;
  private InputStream openStream;

  private boolean fastForward;

  public SessionRecordingPlayback(RecordingWebSocketConnection connection, String fileName,
      WebswingDataStoreModule dataStore) {
    try {
      this.connection = connection;
      this.dataStore = dataStore;
      this.fileName = fileName;

      try (InputStream in = dataStore.readData(WebswingDataStoreType.recording.name(), fileName)) {
        Integer version = readInt(in);
        if (SessionRecordingHeader.version == version) {
          SessionRecordingHeader header = (SessionRecordingHeader) readObject(in);
          while (readInt(in) != null) {
            Integer length = readInt(in);
            in.skip(length);
            numberOfFrames++;
          }
        } else {
          throw new IOException("Version " + version
              + " of recording file is not supported. Current supported version is "
              + SessionRecordingHeader.version);
        }
      }

      resetStream();
      sendNextFrame();
    } catch (IOException e) {
      log.error("Failed to read recording file {}", fileName, e);
    }
  }

  private void resetStream() {
    try {
      if (openStream != null) {
        openStream.close();
      }
      openStream = dataStore.readData(WebswingDataStoreType.recording.name(), fileName);
      readInt(openStream); // version
      readObject(openStream); // header
    } catch (Exception e) {
      log.error("Failed to reset recording file. ", e);
    }
  }

  public synchronized void close() {
    try {
      if (openStream != null) {
        openStream.close();
      }
    } catch (IOException e) {
      log.error("Failed to close recording file. ", e);
    }
  }

  private static byte[] readFrame(InputStream is) throws IOException {
    Integer length = readInt(is);
    if (length == null || length == 0) {
      return null;
    } else {
      byte[] bytes = new byte[length];
      if (length == is.read(bytes)) {
        return bytes;
      } else {
        throw new IOException("Unexpected end of file. Frame expected.");
      }
    }
  }

  private static Integer readInt(InputStream fis) throws IOException {
    byte[] b = new byte[4];
    int length = fis.read(b);
    if (4 == length) {
      return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    } else if (length < 1) {
      return null;
    } else {
      throw new IOException("Unexpected end of file. Integer expected.");
    }
  }

  /**
   * Maximum allowed size for a serialized object header (1 MB). Prevents allocation of oversized
   * byte arrays from malicious recording files.
   */
  private static final int MAX_OBJECT_HEADER_SIZE = 1024 * 1024;

  /**
   * Deserialization filter that restricts allowed classes to prevent arbitrary code execution via
   * gadget chains (CWE-502). Only the types actually used in recording headers are permitted.
   */
  private static final ObjectInputFilter RECORDING_FILTER =
      ObjectInputFilter.Config.createFilter("org.webswing.util.SessionRecordingHeader"
          + ";java.util.Date" + ";java.lang.String" + ";java.lang.Number" + ";!*" // reject
                                                                                  // everything else
      );

  private static Object readObject(InputStream fis) throws IOException {
    readInt(fis); // delay
    Integer headerLength = readInt(fis);
    if (headerLength == null || headerLength <= 0 || headerLength > MAX_OBJECT_HEADER_SIZE) {
      throw new IOException("Invalid object header length: " + headerLength);
    }
    byte[] headerBytes = new byte[headerLength];
    if (headerLength == fis.read(headerBytes)) {
      ByteArrayInputStream bis = new ByteArrayInputStream(headerBytes);
      ObjectInput in = null;
      try (bis) {
        // Deserialization input is restricted to an explicit allowlist via RECORDING_FILTER
        // (deny-all "!*") and the header length is capped by MAX_OBJECT_HEADER_SIZE, so no
        // untrusted gadget chain is reachable.
        ObjectInputStream ois = new ObjectInputStream(bis); // nosemgrep:
                                                            // java.lang.security.audit.object-deserialization.object-deserialization
        ois.setObjectInputFilter(RECORDING_FILTER);
        in = ois;
        Object o = in.readObject();
        return o;
      } catch (ClassNotFoundException e) {
        throw new IOException("Class could not be deserialized");
      } finally {
        try {
          if (in != null) {
            in.close();
          }
        } catch (IOException ex) {
          // ignore close exception
        }
      }
    } else {
      throw new IOException("Unexpected end of file. Object expected.");
    }
  }

  public synchronized void sendNextFrame() {
    if (currentFrame < allowedFrame && currentFrame < numberOfFrames) {
      try {
        currentFrame++;
        Integer delay = readInt(openStream);
        byte[] b = readFrame(openStream);
        final AppFrameMsgOut frame = protoMapper.decodeProto(b, AppFrameMsgOut.class);
        PlaybackInfoMsgOut playback = new PlaybackInfoMsgOut();
        playback.setCurrent(currentFrame);
        playback.setTotal(numberOfFrames);
        frame.setPlayback(playback);
        sender.schedule(new Runnable() {
          @Override
          public void run() {
            if (connection.isConnected()) {
              connection.sendMessage(frame);
            }
            sendNextFrame();
          }
        }, fastForward ? 0 : delay, TimeUnit.MILLISECONDS);
      } catch (IOException e) {
        log.error("Failed to send next recording frame. ", e);
      }
    } else {
      if (currentFrame >= numberOfFrames) {
        close();
      }
    }
  }

  public synchronized void handlePlaybackControl(PlaybackCommandMsgIn playback) {
    if (playback != null && playback.getCommand() != null) {
      switch (playback.getCommand()) {
        case play:
          allowedFrame = numberOfFrames;
          fastForward = false;
          break;
        case stop:
          allowedFrame = currentFrame;
          break;
        case step:
          allowedFrame = currentFrame + 1;
          fastForward = true;
          break;
        case step10:
          allowedFrame = currentFrame + 10;
          fastForward = true;
          break;
        case step100:
          allowedFrame = currentFrame + 100;
          fastForward = true;
          break;
        case reset:
          resetStream();
          allowedFrame = 1;
          currentFrame = 0;
          break;
        default:
          break;
      }
      sendNextFrame();
    }
  }
}
