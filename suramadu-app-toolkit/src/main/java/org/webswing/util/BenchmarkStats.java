package org.webswing.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Lightweight per-frame CSV sink for DirectDraw pipeline benchmarking.
 *
 * <p>
 * Disabled unless {@code -Dwebswing.benchmarkStatsFile=<path>} is set on the application JVM — zero
 * overhead in production (one static null check per call site).
 *
 * <p>
 * Row types (first column):
 * <ul>
 * <li>{@code FRAME,<sendTs>,<extractNanos>,<encodeNanos>,<ddBytes>,<windows>} — written by the
 * paint dispatcher when a frame is handed to the websocket. {@code sendTs} is the frame's
 * sendTimestamp (server clock, millis) and is the JOIN KEY to the matching ACK row.
 * {@code extractNanos} covers the phase under AWT treeLock + webPaintLock (dirty-area processing +
 * window/WebImage extraction — the EDT-contention cost); {@code encodeNanos} covers instruction
 * optimization, constant pooling, ZPNG and protobuf encoding (off-lock).</li>
 * <li>{@code ACK,<recvTs>,<sendTs>,<startTs>,<renderingTimeMs>,<pingMs>,<rttMs>} — written when the
 * client echoes its timestamps back. {@code renderingTimeMs} is measured on the CLIENT clock as a
 * duration (decode + canvas replay), so no clock synchronisation is involved. {@code rttMs} is
 * {@code recvTs - sendTs}, both on the SERVER clock: full round trip including network both ways
 * and client processing. Approximate network-only time: {@code rtt - renderingTime}.</li>
 * <li>{@code SCENE,<ts>,<name>} — scene markers emitted by the benchmark application, used by the
 * analyzer to segment the run.</li>
 * </ul>
 */
public final class BenchmarkStats {

  public static final String PROPERTY = "webswing.benchmarkStatsFile";

  private static final PrintWriter OUT = init();
  private static int lines;

  private static PrintWriter init() {
    String file = System.getProperty(PROPERTY);
    if (file == null || file.isBlank()) {
      return null;
    }
    try {
      PrintWriter w = new PrintWriter(
          new BufferedWriter(new FileWriter(file, Charset.defaultCharset(), false)));
      w.println("type,t1,t2,t3,t4,t5,t6");
      w.flush();
      AppLogger.info("BenchmarkStats enabled, writing to " + file);
      return w;
    } catch (IOException e) {
      AppLogger.error("BenchmarkStats: failed to open " + file + ", stats disabled", e);
      return null;
    }
  }

  public static boolean isEnabled() {
    return OUT != null;
  }

  /** Called by the paint dispatcher after the frame message is built, before it is sent. */
  public static void frame(String sendTimestamp, long extractNanos, long encodeNanos, long ddBytes,
      int windows) {
    if (OUT == null) {
      return;
    }
    write("FRAME," + sendTimestamp + "," + extractNanos + "," + encodeNanos + "," + ddBytes + ","
        + windows + ",");
  }

  /** Called by the event dispatcher when the client's timestamp echo arrives. */
  public static void ack(String sendTimestamp, String startTimestamp, String renderingTime,
      int ping) {
    if (OUT == null) {
      return;
    }
    long recv = System.currentTimeMillis();
    long rtt = -1;
    try {
      rtt = recv - Long.parseLong(sendTimestamp);
    } catch (RuntimeException ignored) {
      // sendTimestamp absent or non-numeric — keep -1
    }
    write("ACK," + recv + "," + sendTimestamp + "," + startTimestamp + "," + renderingTime + ","
        + ping + "," + rtt);
  }

  /** Scene marker, called by the benchmark application. Safe to call when disabled. */
  public static void scene(String name) {
    if (OUT == null) {
      return;
    }
    write("SCENE," + System.currentTimeMillis() + "," + name + ",,,,");
  }

  private static synchronized void write(String line) {
    OUT.println(line);
    if (++lines % 64 == 0) {
      OUT.flush();
    }
  }

  /** Flush pending rows (benchmark app calls this at scene boundaries / end of run). */
  public static synchronized void flush() {
    if (OUT != null) {
      OUT.flush();
    }
  }

  private BenchmarkStats() {}
}
