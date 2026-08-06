package org.webswing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Test implements Serializable {
  public String name;
  public List<String> originalImg = new ArrayList<String>();
  public List<String> protoImg = new ArrayList<String>();

  public int originalRenderTime;
  public int protoRenderTime;

  public int originalRenderSize;
  public int protoRenderSize;
}
