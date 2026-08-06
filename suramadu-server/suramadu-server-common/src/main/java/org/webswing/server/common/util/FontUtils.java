package org.webswing.server.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.webswing.Constants;
import org.webswing.server.common.model.SwingConfig;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

public class FontUtils {

  private static final List<String> logicalNames =
      Arrays.asList("monospaced", "serif", "sansserif", "dialoginput", "dialog");
  private static final List<String> styles = Arrays.asList("bolditalic", "italic", "bold", "plain");
  private static final String defaultChargroup =
      System.getProperty("os.name", "").startsWith("Windows") ? "alpbabet" : "latin-1";

  public static String createFontConfiguration(SwingConfig appConfig, VariableSubstitutor subs)
      throws IOException {
    if (appConfig.getFontConfig() != null && appConfig.getFontConfig().size() > 0) {
      StringBuilder fontConfig = new StringBuilder("version=1\n");
      StringBuilder metadata = new StringBuilder();
      fontConfig.append("sequence.allfonts=").append(defaultChargroup).append("\n");
      Map<String, File> fonts = buildFontMap(appConfig.getFontConfig(), subs);
      Map<File, String> fontNames = resolveFontNames(new HashSet<File>(fonts.values()));
      String defaultFont = findDefaultFontKey(fonts, false);
      String defaultMonospace = findDefaultFontKey(fonts, true);
      for (String logicalFont : logicalNames) {
        for (String style : styles) {
          String key = findFont(logicalFont, style, defaultFont, defaultMonospace, fonts);
          File file = fonts.get(key);
          String fullName = fontNames.get(file);
          fontConfig.append(logicalFont).append(".").append(style).append(".")
              .append(defaultChargroup).append("=").append(fullName).append("\n");
          // directDraw font to file mapping:
          metadata.append("#@@").append(logicalFont).append(".").append(style).append("=")
              .append(file.getAbsolutePath()).append("\n");
        }
      }
      for (File fontFile : fontNames.keySet()) {
        String fontName = fontNames.get(fontFile);
        String fontName_ = fontName.replace(' ', '_');
        fontConfig.append("filename.").append(fontName_).append("=")
            .append(StringEscapeUtils.escapeJava(fontFile.getCanonicalPath())).append("\n");
        metadata.append("#@@").append(fontName).append("=").append(fontFile.getAbsolutePath())
            .append("\n");
      }
      fontConfig.append("\n").append(metadata);

      String tempDir = System.getProperty(Constants.TEMP_DIR_PATH);
      File configfile = new File(URI.create(
          tempDir + URLEncoder.encode(subs.replace("fontconfig-${clientId}.properties"), "UTF-8")));
      FileUtils.writeStringToFile(configfile, fontConfig.toString());

      return configfile.getAbsolutePath();
    } else {
      return null;
    }
  }

  private static String findFont(String logicalFont, String style, String defaultFont,
      String defaultMonospace, Map<String, File> fonts) {
    if (fonts.containsKey(logicalFont + " " + style)) {// check if exact font defined
      return logicalFont + " " + style;
    } else if (fonts.containsKey(logicalFont + " plain")) {// check if plain font exist
      return logicalFont + " plain";
    } else if (isMonospaceFont(logicalFont)) {
      return defaultMonospace;
    }
    return defaultFont;
  }

  private static Map<String, File> buildFontMap(Map<String, String> fontConfig,
      VariableSubstitutor subs) {
    Map<String, File> result = new HashMap<String, File>();
    for (String key : fontConfig.keySet()) {
      String keyValue = subs.replace(key).toLowerCase().trim();
      if (isLogicalFont(keyValue) && logicalNames.contains(keyValue)) {// if no style is specified
                                                                       // use plain
        keyValue = keyValue + " plain";
      }
      File fontFile = new File(subs.replace(fontConfig.get(key)).trim());
      if (!fontFile.exists()) {
        throw new RuntimeException("Loading font " + keyValue + " failed . Font file "
            + fontFile.getAbsolutePath() + " not found.");
      }
      if (!fontFile.isFile()) {
        throw new RuntimeException("Loading font " + keyValue + " failed . Font file "
            + fontFile.getAbsolutePath() + " is not a file.");
      }
      result.put(keyValue, fontFile);
    }
    return result;
  }

  @SuppressWarnings("restriction")
  private static Map<File, String> resolveFontNames(Set<File> fontFiles) {
    Map<File, String> result = new HashMap<File, String>();
    for (File file : fontFiles) {
      try {
        sun.font.TrueTypeFont ttfFile =
            new sun.font.TrueTypeFont(file.getAbsolutePath(), null, 0, false);
        String name = ttfFile.getFullName();
        result.put(file, name);
      } catch (Exception e) {
        throw new RuntimeException("Loading TTF font " + file + " failed .", e);
      }
    }
    return result;
  }

  private static String findDefaultFontKey(Map<String, File> fontConfig, boolean preferMonospace) {
    String result = "";
    for (String key : fontConfig.keySet()) {
      if (isLogicalFont(key)) {
        String[] logicalFont = key.split(" ");
        String fontName = logicalFont[0];
        String fontStyle = logicalFont.length > 1 ? logicalFont[1] : "plain";
        String resultName = result.split(" ")[0];
        String resultStyle = result.split(" ").length > 1 ? result.split(" ")[1] : "plain";
        int score = logicalNames.indexOf(fontName) * 10 + styles.indexOf(fontStyle);
        int resultScore = logicalNames.indexOf(resultName) * 10 + styles.indexOf(resultStyle);
        if (preferMonospace) {
          score = isMonospaceFont(key) ? score * 10 : score;
          resultScore = isMonospaceFont(result) ? resultScore * 10 : resultScore;
        }
        if (score > resultScore) {
          result = key;
        }
      } else if (result.isEmpty()) {
        result = key;
      }
    }
    return result.isEmpty() ? null : result;
  }

  private static boolean isLogicalFont(String keyValue) {
    for (String logicalName : logicalNames) {
      if (keyValue.startsWith(logicalName)) {
        String remainder = keyValue.substring(logicalName.length()).trim();
        if (remainder.isEmpty() || styles.contains(remainder)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isMonospaceFont(String keyValue) {
    if (isLogicalFont(keyValue)
        && (keyValue.startsWith("monospaced") || keyValue.startsWith("dialoginput"))) {
      return true;
    }
    return false;
  }

  private FontUtils() {}
}
