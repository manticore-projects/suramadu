package org.webswing.benchmark;

import org.webswing.util.BenchmarkStats;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Self-driving DirectDraw benchmark application.
 *
 * <p>
 * Deterministic (fixed seed), no user input required: open the app URL in a browser and it cycles
 * through scenes, emitting SCENE markers into the {@code BenchmarkStats} CSV (enable with
 * {@code -Dwebswing.benchmarkStatsFile=<path>} in the app's vmArgs). The DirectDraw pipeline only
 * runs with a connected, acking client — this app drives itself, but a browser session must be open
 * for anything to be measured.
 *
 * <p>
 * Scenes (each {@code -Dbenchmark.sceneSeconds=N}, default 30, scroll step every
 * {@code -Dbenchmark.stepMs=N}, default 10):
 * <ul>
 * <li>{@code TEXT_TABLE} — 100k-row table, strings + right-aligned numbers, auto-scrolled. The
 * canonical VBox workload: strings, rects, no images.</li>
 * <li>{@code ICON_TABLE} — same table plus a raster-icon column cycling through 32 distinct 20px
 * icons: exercises the ImageConst hash + LRU cache (hit-heavy).</li>
 * <li>{@code VECTOR} — panel repainting ~600 seeded shapes (paths, ellipses, strokes, gradients)
 * per frame: instruction-building and constant-pool stress, no rasters.</li>
 * <li>{@code IMAGE_FLOOD} — a fresh random raster image every frame: worst case, every frame a
 * cache MISS → full hash + ZPNG encode.</li>
 * <li>{@code IDLE} — nothing dirty: baseline noise floor (should produce near-zero frames).</li>
 * </ul>
 * After one full cycle it prints {@code BENCHMARK DONE} and idles (exiting would kill the session).
 * Run a saturation variant by adding {@code -Dwebswing.drawDelayMs=1} to vmArgs.
 */
public class Benchmark {

  private static final int ROWS = 100_000;
  private static final int COLS = 8;
  private static final int ICON_VARIANTS = 32;
  private static final int SCENE_SECONDS = Integer.getInteger("benchmark.sceneSeconds", 30);
  private static final int STEP_MS = Integer.getInteger("benchmark.stepMs", 10);
  private static final long SEED = 42L;

  private JFrame frame;
  private JScrollPane scroll;
  private Timer driver;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Benchmark().start());
  }

  private void start() {
    frame = new JFrame("Suramadu DirectDraw Benchmark");
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setLocation(0, 0);
    frame.setVisible(true);
    runScenes(new String[] {"TEXT_TABLE", "ICON_TABLE", "VECTOR", "IMAGE_FLOOD", "IDLE"}, 0);
  }

  private void runScenes(String[] scenes, int index) {
    if (driver != null) {
      driver.stop();
      driver = null;
    }
    if (index >= scenes.length) {
      BenchmarkStats.scene("DONE");
      BenchmarkStats.flush();
      System.out.println("BENCHMARK DONE");
      frame.getContentPane().removeAll();
      frame.getContentPane().add(new JLabel("Benchmark done.", SwingConstants.CENTER));
      frame.revalidate();
      frame.repaint();
      return;
    }
    String scene = scenes[index];
    System.out.println("BENCHMARK SCENE " + scene + " START");
    BenchmarkStats.scene(scene);
    BenchmarkStats.flush();

    frame.getContentPane().removeAll();
    switch (scene) {
      case "TEXT_TABLE" -> installTable(false);
      case "ICON_TABLE" -> installTable(true);
      case "VECTOR" -> installVector();
      case "IMAGE_FLOOD" -> installImageFlood();
      case "IDLE" -> installIdle();
      default -> throw new IllegalArgumentException(scene);
    }
    frame.revalidate();
    frame.repaint();

    Timer end = new Timer(SCENE_SECONDS * 1000, e -> runScenes(scenes, index + 1));
    end.setRepeats(false);
    end.start();
  }

  // ── TEXT_TABLE / ICON_TABLE ─────────────────────────────────────────

  private void installTable(boolean icons) {
    JTable table = new JTable(new BenchModel(icons));
    table.setRowHeight(24);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    if (icons) {
      table.getColumnModel().getColumn(0).setCellRenderer(new IconRenderer(makeIcons()));
    }
    DefaultTableCellRenderer right = new DefaultTableCellRenderer();
    right.setHorizontalAlignment(SwingConstants.RIGHT);
    for (int c = icons ? 1 : 0; c < COLS; c++) {
      if (c % 2 == 1) {
        table.getColumnModel().getColumn(c).setCellRenderer(right);
      }
    }
    scroll = new JScrollPane(table);
    scroll.getVerticalScrollBar().setUnitIncrement(24);
    frame.getContentPane().add(scroll, BorderLayout.CENTER);

    final int max = ROWS * 24;
    driver = new Timer(STEP_MS, null);
    driver.addActionListener(e -> {
      JScrollBar bar = scroll.getVerticalScrollBar();
      int step = Math.max(24, max / (SCENE_SECONDS * 1000 / STEP_MS) / 2);
      int next = bar.getValue() + (down ? step : -step);
      if (next >= bar.getMaximum() - bar.getVisibleAmount()) {
        down = false;
      } else if (next <= 0) {
        down = true;
      }
      bar.setValue(next);
    });
    down = true;
    driver.start();
  }

  private boolean down = true;

  private static final class BenchModel extends AbstractTableModel {
    private final boolean icons;

    BenchModel(boolean icons) {
      this.icons = icons;
    }

    @Override
    public int getRowCount() {
      return ROWS;
    }

    @Override
    public int getColumnCount() {
      return COLS;
    }

    @Override
    public String getColumnName(int c) {
      return icons && c == 0 ? "Icon" : "Column " + c;
    }

    @Override
    public Object getValueAt(int r, int c) {
      if (icons && c == 0) {
        return r % ICON_VARIANTS;
      }
      // deterministic, non-repeating-looking content without storing 800k strings
      long v = (r * 31L + c) * 2654435761L;
      return c % 2 == 1 ? String.format("%,d.%02d", Math.abs(v % 100_000_000L), Math.abs(v % 100))
          : "Instrument-" + Long.toHexString(Math.abs(v));
    }
  }

  private static final class IconRenderer extends DefaultTableCellRenderer {
    private final Icon[] icons;

    IconRenderer(Icon[] icons) {
      this.icons = icons;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
      setIcon(icons[(Integer) value]);
      return this;
    }
  }

  /** 32 distinct raster icons — distinct pixel content, so distinct ImageConst cache entries. */
  private static Icon[] makeIcons() {
    Icon[] result = new Icon[ICON_VARIANTS];
    Random rnd = new Random(SEED);
    for (int i = 0; i < ICON_VARIANTS; i++) {
      BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = img.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor(new Color(rnd.nextInt(0xFFFFFF)));
      g.fillOval(1, 1, 18, 18);
      g.setColor(Color.WHITE);
      g.drawString(Integer.toString(i % 10), 7, 15);
      g.dispose();
      result[i] = new ImageIcon(img);
    }
    return result;
  }

  // ── VECTOR ──────────────────────────────────────────────────────────

  private void installVector() {
    JPanel panel = new JPanel() {
      private int tick;

      @Override
      protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Random rnd = new Random(SEED + tick);
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < 600; i++) {
          g.setColor(new Color(rnd.nextInt(0xFFFFFF)));
          int x = rnd.nextInt(Math.max(1, w - 60));
          int y = rnd.nextInt(Math.max(1, h - 60));
          switch (i % 4) {
            case 0 -> g.fillRect(x, y, 20 + rnd.nextInt(40), 10 + rnd.nextInt(20));
            case 1 -> g.drawOval(x, y, 20 + rnd.nextInt(40), 20 + rnd.nextInt(40));
            case 2 -> {
              g.setStroke(new BasicStroke(1 + rnd.nextInt(3)));
              g.drawLine(x, y, x + rnd.nextInt(60), y + rnd.nextInt(60));
            }
            default -> {
              g.setPaint(new GradientPaint(x, y, Color.WHITE, x + 40f, y + 20f,
                  new Color(rnd.nextInt(0xFFFFFF))));
              g.fillRoundRect(x, y, 40, 20, 8, 8);
            }
          }
        }
        tick++;
      }
    };
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    driver = new Timer(STEP_MS, e -> panel.repaint());
    driver.start();
  }

  // ── IMAGE_FLOOD ─────────────────────────────────────────────────────

  private void installImageFlood() {
    JPanel panel = new JPanel() {
      private int tick;

      @Override
      protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        // a fresh image every frame: guaranteed ImageConst cache miss (hash + ZPNG every time)
        Random rnd = new Random(SEED + tick);
        BufferedImage img = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 300; y++) {
          for (int x = 0; x < 400; x++) {
            img.setRGB(x, y, rnd.nextInt());
          }
        }
        g1.drawImage(img, (getWidth() - 400) / 2, (getHeight() - 300) / 2, null);
        tick++;
      }
    };
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    driver = new Timer(STEP_MS * 5, e -> panel.repaint());
    driver.start();
  }

  // ── IDLE ────────────────────────────────────────────────────────────

  private void installIdle() {
    frame.getContentPane().add(new JLabel("IDLE — measuring noise floor", SwingConstants.CENTER));
  }
}
