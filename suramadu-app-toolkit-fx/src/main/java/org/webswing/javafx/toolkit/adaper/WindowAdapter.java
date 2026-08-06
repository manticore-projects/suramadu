package org.webswing.javafx.toolkit.adaper;

import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.List;

/**
 * Created by vikto on 03-Mar-17.
 */
public interface WindowAdapter {
  Window getThis();

  void setVisible(boolean b);

  void dispose();

  Container getContentPane();

  Rectangle getBounds();

  void setBounds(int x, int y, int width, int height);

  void setLocation(int x, int y);

  void setResizable(boolean resizable);

  void requestFocus();

  void setFocusable(boolean isFocusable);

  void setTitle(String title);

  void setEnabled(boolean enabled);

  void setMinimumSize(Dimension dimension);

  void setMaximumSize(Dimension dimension);

  void toFront();

  void toBack();

  void setModal(boolean b);

  Insets getInsets();

  void setIconImages(List<? extends Image> icons);

  void addComponentListener(ComponentListener componentListener);

  void addWindowListener(WindowListener windowListener);

  void addWindowFocusListener(WindowFocusListener windowFocusListener);

  boolean isShowing();
}
