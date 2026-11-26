package org.webswing.toolkit.ge;

import org.webswing.util.AppLogger;
import sun.awt.FontConfiguration;
import sun.font.FileFont;
import sun.font.Font2D;
import sun.font.FontUtilities;
import sun.font.PhysicalFont;
import sun.font.SunFontManager;

import java.awt.Font;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

@SuppressWarnings("restriction")
public class WebFontManager extends SunFontManager {

  /**
   * PhysicalFont.platName: the file behind a created font (needs --add-opens
   * java.desktop/sun.font).
   */
  private static final Field PLAT_NAME = platNameField();

  private static Field platNameField() {
    try {
      Field f = PhysicalFont.class.getDeclaredField("platName");
      f.setAccessible(true);
      return f;
    } catch (ReflectiveOperationException | RuntimeException e) {
      return null;
    }
  }

  @Override
  protected FontConfiguration createFontConfiguration() {
    return createFontConfiguration(false, false);
  }

  @Override
  public FontConfiguration createFontConfiguration(boolean arg0, boolean arg1) {
    return new WebFontConfiguration(this);
  }

  @Override
  public String[] getDefaultPlatformFont() {
    // dummy value
    String[] info = new String[2];
    info[0] = "Dialog";
    info[1] = "/dialog.ttf";
    return info;
  }

  @Override
  protected String getFontPath(boolean arg0) {
    return "";
  }

  @Override
  protected void registerFontDirs(String arg0) {
    // do nothing
  }

  /**
   * GraphicsEnvironment.registerFont(Font) for fonts from Font.createFont.
   *
   * <p>
   * The JDK refuses to register a created font whose family is already installed. Under Suramadu
   * the deployment's fontConfig installs fonts (e.g. dialog = Roboto Regular), so every further
   * face of that family the application ships (Roboto Medium, Black, ...) is refused, and
   * {@code new Font("Roboto Medium", ...)} silently resolves to Dialog: wrong metrics for Swing,
   * jSVG, graphviz and anything else looking fonts up by name.
   *
   * <p>
   * When the JDK refuses and the face does not resolve by name, the font's file is registered as an
   * installed font instead. A private copy is used: the JDK deletes a created font's temp file once
   * the Font object is garbage collected.
   */
  @Override
  public boolean registerFont(Font font) {
    if (super.registerFont(font)) {
      return true;
    }
    if (font == null || PLAT_NAME == null) {
      return false;
    }
    try {
      Font2D font2D = FontUtilities.getFont2D(font);
      if (!(font2D instanceof FileFont)) {
        return false;
      }
      String face = font.getFontName(Locale.ROOT);
      if (face.equalsIgnoreCase(new Font(face, Font.PLAIN, 1).getFontName(Locale.ROOT))) {
        return false; // the face is genuinely installed already: the JDK's refusal is right
      }
      String source = (String) PLAT_NAME.get(font2D);
      if (source == null || !new File(source).isFile()) {
        return false;
      }
      File copy = File.createTempFile("webfont-", ".ttf");
      copy.deleteOnExit();
      Files.copy(new File(source).toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
      PhysicalFont registered =
          registerFontFile(copy.getPath(), null, FONTFORMAT_TRUETYPE, true, Font2D.TTF_RANK);
      if (registered != null) {
        AppLogger.info("WebFontManager: registered '" + face
            + "' as installed font (its family is already installed, so the JDK refused it)");
        return true;
      }
    } catch (Exception | LinkageError e) {
      AppLogger.warn("WebFontManager: could not register font '" + font.getFontName() + "'", e);
    }
    return false;
  }
}
