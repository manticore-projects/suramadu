package org.webswing.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class CheckSerializedSize extends OutputStream {

  /** Serialize obj and count the bytes */
  public static long getSerializedSize(Serializable obj) {
    try {
      CheckSerializedSize counter = new CheckSerializedSize();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(counter);
      objectOutputStream.writeObject(obj);
      objectOutputStream.close();
      return counter.getNBytes();
    } catch (Exception e) {
      // Serialization failed
      return -1;
    }
  }

  private long nBytes;

  private CheckSerializedSize() {}

  @Override
  public void write(int b) throws IOException {
    ++nBytes;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    nBytes += len;
  }

  public long getNBytes() {
    return nBytes;
  }
}
