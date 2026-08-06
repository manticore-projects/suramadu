package org.webswing.ext.services;

import org.webswing.model.appframe.out.ComponentTreeMsgOut;

import java.awt.Window;
import java.util.List;

public interface ToolkitFXService {

  public List<ComponentTreeMsgOut> requestNodeTree(Object node);

  public void registerStage(Object stage);

  public boolean isFXWindow(Window window);

}
