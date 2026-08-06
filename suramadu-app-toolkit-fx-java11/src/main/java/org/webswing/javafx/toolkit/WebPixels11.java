package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Pixels;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class WebPixels11 extends Pixels {
  public WebPixels11(int width, int height, ByteBuffer data) {
    super(width, height, data);
  }

  public WebPixels11(int width, int height, IntBuffer data) {
    super(width, height, data);
  }

  public WebPixels11(int width, int height, IntBuffer data, float scalex, float scaley) {
    super(width, height, data, scalex, scaley);
  }

  @Override
  protected void _fillDirectByteBuffer(ByteBuffer bb) {}

  @Override
  protected void _attachInt(long ptr, int w, int h, IntBuffer ints, int[] array, int offset) {}

  @Override
  protected void _attachByte(long ptr, int w, int h, ByteBuffer bytes, byte[] array, int offset) {}
}
