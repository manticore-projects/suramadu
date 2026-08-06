package org.webswing.toolkit.util;

import org.webswing.toolkit.WebPrintService;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class WebPrintDialog extends JDialog {

  @Serial
  private static final long serialVersionUID = 7094056515358259815L;
  private static final ResourceBundle messages =
      ResourceBundle.getBundle("org.webswing.toolkit.util.WebPrintDialogResources");
  private JButton cancel;
  private JButton approve;
  private JPanel content;
  private int status;
  private MediaPanel mediaPanel;
  private MarginsPanel marginsPanel;
  private PrintRangePanel rangePanel;
  private final PrintService[] services;
  private final int defaultServiceIndex;
  private PrintService service;
  private final PrintRequestAttributeSet asCurrent;
  private final DocFlavor docFlavor;

  public WebPrintDialog(PrintService[] services, int defaultServiceIndex,
      PrintRequestAttributeSet as, DocFlavor docFlavor) {
    super((JFrame) null, getMsg("dialog.printtitle"), true);
    this.defaultServiceIndex = defaultServiceIndex;
    this.asCurrent = new HashPrintRequestAttributeSet(as);
    this.docFlavor = docFlavor;
    this.services = services;
    try {
      this.service = Arrays.asList(services).get(defaultServiceIndex);
    } catch (IndexOutOfBoundsException e) {
      if (services.length > 0) {
        defaultServiceIndex = 0;
        service = services[0];
      }
    }
    initDialog();
  }

  public static String getMsg(String key) {
    try {
      return removeMnemonics(messages.getString(key));
    } catch (MissingResourceException e) {
      return key;
    }
  }

  private static String removeMnemonics(String paramString) {
    int i = paramString.indexOf('&');
    int j = paramString.length();
    if ((i < 0) || (i == j - 1)) {
      return paramString;
    }
    int k = paramString.indexOf('&', i + 1);
    if (k == i + 1) {
      if (k + 1 == j) {
        return paramString.substring(0, i + 1);
      }
      return paramString.substring(0, i + 1) + removeMnemonics(paramString.substring(k + 1));
    }

    if (i == 0) {
      return removeMnemonics(paramString.substring(1));
    }
    return paramString.substring(0, i) + removeMnemonics(paramString.substring(i + 1));
  }

  @SuppressWarnings("serial")
  private void initDialog() {
    Container pane = getContentPane();
    pane.setLayout(new BorderLayout());

    content = new JPanel();
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.insets = new Insets(6, 6, 6, 6);
    constraints.weightx = 1.0D;
    constraints.weighty = 1.0D;
    content.setLayout(layout);
    mediaPanel = new MediaPanel();
    rangePanel = new PrintRangePanel();
    marginsPanel = new MarginsPanel();
    mediaPanel.addOrientationListener(marginsPanel);

    constraints.gridwidth = GridBagConstraints.REMAINDER;
    layout.setConstraints(mediaPanel, constraints);
    content.add(mediaPanel);

    constraints.gridwidth = GridBagConstraints.RELATIVE;
    layout.setConstraints(rangePanel, constraints);
    content.add(rangePanel);

    constraints.gridwidth = GridBagConstraints.REMAINDER;
    layout.setConstraints(marginsPanel, constraints);
    content.add(marginsPanel);

    pane.add(content, "Center");

    update();

    JPanel btns = new JPanel(new FlowLayout(4));
    approve = new JButton(getMsg("button.print"));
    approve.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose(1);
      }
    });
    btns.add(approve);
    getRootPane().setDefaultButton(approve);
    cancel = new JButton(getMsg("button.cancel"));
    cancel.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose(2);
      }
    });
    // cancel on esc
    if ((cancel.getInputMap(2) != null) && (cancel.getActionMap() != null)) {
      cancel.getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), "cancel");
      cancel.getActionMap().put("cancel", new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
          dispose(2);
        }
      });
    }
    btns.add(cancel);
    pane.add(btns, "South");

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        dispose(2);
      }

    });
    setResizable(false);
    pack();
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
    setLocation(x, y);
  }

  private void update() {
    mediaPanel.updateInfo();
    rangePanel.updateInfo();
    marginsPanel.updateInfo();
  }

  private String getMediaName(String media) {
    try {
      String str = media.replace(' ', '-');
      str = str.replace('#', 'n');

      return messages.getString(str);
    } catch (MissingResourceException localMissingResourceException) {
    }
    return media;
  }

  public void dispose(int paramInt) {
    status = paramInt;
    super.dispose();
  }

  public int getStatus() {
    return status;
  }

  public PrintService getService() {
    return service;
  }

  public PrintRequestAttributeSet getAttributes() {
    return asCurrent;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  class MediaPanel extends JPanel implements ItemListener {
    @Serial
    private static final long serialVersionUID = 3809387570018736561L;
    private MarginsPanel pnlMargins;
    private JComboBox cbService;
    private JComboBox cbOrientation;
    private JComboBox cbSize;
    private ArrayList<Media> allMedia = new ArrayList<Media>();

    public MediaPanel() {

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();

      setLayout(gridbag);
      setBorder(BorderFactory.createTitledBorder(getMsg("border.media")));

      String[] psnames = new String[services.length];
      for (int i = 0; i < psnames.length; i++) {
        psnames[i] = services[i].getName();
      }
      cbService = new JComboBox(psnames);
      cbService.setSelectedIndex(defaultServiceIndex);
      cbService.addItemListener(this);

      cbSize = new JComboBox(new DefaultComboBoxModel());
      cbOrientation = new JComboBox();

      c.fill = GridBagConstraints.BOTH;
      c.insets = new Insets(3, 6, 3, 6);
      c.weighty = 1.0;

      c.weightx = 0.0;
      JLabel lblService = new JLabel(getMsg("label.psname"), JLabel.TRAILING);
      lblService.setLabelFor(cbSize);
      addToGB(lblService, this, gridbag, c);
      c.weightx = 1.0;
      c.gridwidth = GridBagConstraints.REMAINDER;
      addToGB(cbService, this, gridbag, c);

      c.weightx = 0.0;
      c.gridwidth = 1;
      JLabel lblSize = new JLabel(getMsg("label.size"), JLabel.TRAILING);
      lblSize.setLabelFor(cbSize);
      addToGB(lblSize, this, gridbag, c);
      c.weightx = 1.0;
      c.gridwidth = GridBagConstraints.REMAINDER;
      addToGB(cbSize, this, gridbag, c);

      c.weightx = 0.0;
      c.gridwidth = 1;
      JLabel lblSource = new JLabel(getMsg("border.orientation") + ":", JLabel.TRAILING);
      lblSource.setLabelFor(cbOrientation);
      addToGB(lblSource, this, gridbag, c);
      c.gridwidth = GridBagConstraints.REMAINDER;
      addToGB(cbOrientation, this, gridbag, c);

      // init data
      loadMediaSizes();

      final List<OrientationRequested> orientations = new ArrayList<OrientationRequested>();
      cbOrientation.addItem(getMsg("radiobutton.portrait"));
      orientations.add(OrientationRequested.PORTRAIT);
      cbOrientation.addItem(getMsg("radiobutton.landscape"));
      orientations.add(OrientationRequested.LANDSCAPE);

      // change listener
      cbSize.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == ItemEvent.SELECTED) {
            int i = cbSize.getSelectedIndex();
            if ((i >= 0) && (i < allMedia.size())) {
              asCurrent.add((MediaSizeName) allMedia.get(i));
            }
          }
          if (pnlMargins != null) {
            pnlMargins.updateInfo();
          }
        }
      });
      cbOrientation.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == ItemEvent.SELECTED) {
            int i = cbOrientation.getSelectedIndex();
            if ((i >= 0) && (i < orientations.size())) {
              asCurrent.add((OrientationRequested) orientations.get(i));
            }
          }
          if (pnlMargins != null) {
            pnlMargins.updateInfo();
          }
        }
      });
    }

    private void loadMediaSizes() {
      cbSize.removeAllItems();
      if (service != null && service.isAttributeCategorySupported(Media.class)) {
        Object mediaArrayObject =
            service.getSupportedAttributeValues(Media.class, docFlavor, asCurrent);
        Media[] mediaArray;
        Media media;
        if (mediaArrayObject instanceof Media[] media1s) {
          mediaArray = media1s;
          for (int i = 0; i < mediaArray.length; i++) {
            media = mediaArray[i];
            if (media instanceof MediaSizeName) {
              cbSize.addItem(getMediaName(((Media) media).toString()));
              allMedia.add((Media) media);
            }
          }
        }
      }
    }

    public void updateInfo() {
      if (service != null) {
        Media currentMedia = (Media) asCurrent.get(Media.class);
        Media defaultMedia = (Media) service.getDefaultAttributeValue(Media.class);

        if ((currentMedia == null) || (!service.isAttributeValueSupported((Attribute) currentMedia,
            docFlavor, asCurrent))) {
          currentMedia = defaultMedia;
          cbSize.setSelectedIndex(allMedia.size() > 0 ? allMedia.indexOf(defaultMedia) : -1);
          asCurrent.add((Attribute) currentMedia);
        } else {
          cbSize.setSelectedIndex(allMedia.indexOf(currentMedia));
        }

        OrientationRequested or = (OrientationRequested) asCurrent.get(OrientationRequested.class);
        if (or == null || !service.isAttributeValueSupported(or, docFlavor, asCurrent)) {
          or = (OrientationRequested) service.getDefaultAttributeValue(OrientationRequested.class);
          if (or == null) {
            or = OrientationRequested.PORTRAIT;
          }
          asCurrent.add(or);
        }

        if (or == OrientationRequested.PORTRAIT) {
          cbOrientation.setSelectedIndex(0);
        } else if (or == OrientationRequested.LANDSCAPE) {
          cbOrientation.setSelectedIndex(1);
        } else {
          cbOrientation.setSelectedIndex(0);
        }
      }
    }

    private void addToGB(JComponent cbOr, MediaPanel mediaPanel, GridBagLayout gridbag,
        GridBagConstraints c) {
      gridbag.setConstraints(cbOr, c);
      this.add(cbOr);
    }

    void addOrientationListener(MarginsPanel pnl) {
      pnlMargins = pnl;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        int index = cbService.getSelectedIndex();

        if ((index >= 0) && (index < services.length)) {
          if (!services[index].equals(service)) {
            service = services[index];
            loadMediaSizes();
            updateInfo();
          }
        }
      }
    }
  }

  class MarginsPanel extends JPanel implements ActionListener, FocusListener {

    @Serial
    private static final long serialVersionUID = 6865833608103747234L;
    private final String strTitle = getMsg("border.margins");
    private JFormattedTextField leftMargin;
    private JFormattedTextField rightMargin;
    private JFormattedTextField topMargin;
    private JFormattedTextField bottomMargin;
    private JLabel lblLeft;
    private JLabel lblRight;
    private JLabel lblTop;
    private JLabel lblBottom;
    private int units = MediaPrintableArea.MM;
    // storage for the last margin values calculated, -ve is uninitialised
    private float lmVal = -1f;
    private float rmVal = -1f;
    private float tmVal = -1f;
    private float bmVal = -1f;
    // storage for margins as objects mapped into orientation for display
    private Float lmObj;
    private Float rmObj;
    private Float tmObj;
    private Float bmObj;

    public MarginsPanel() {
      super();

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weightx = 1.0;
      c.weighty = 0.0;
      c.insets = new Insets(6, 3, 6, 3);

      setLayout(gridbag);
      setBorder(BorderFactory.createTitledBorder(strTitle));

      String unitsKey = "label.millimetres";
      String defaultCountry = Locale.getDefault().getCountry();
      if (defaultCountry != null
          && ("".equals(defaultCountry) || defaultCountry.equals(Locale.US.getCountry())
              || defaultCountry.equals(Locale.CANADA.getCountry()))) {
        unitsKey = "label.inches";
        units = MediaPrintableArea.INCH;
      }
      String unitsMsg = getMsg(unitsKey);

      DecimalFormat format;
      if (units == MediaPrintableArea.MM) {
        format = new DecimalFormat("###.##");
        format.setMaximumIntegerDigits(3);
      } else {
        format = new DecimalFormat("##.##");
        format.setMaximumIntegerDigits(2);
      }

      format.setMinimumFractionDigits(1);
      format.setMaximumFractionDigits(2);
      format.setMinimumIntegerDigits(1);
      format.setParseIntegerOnly(false);
      format.setDecimalSeparatorAlwaysShown(true);
      NumberFormatter nf = new NumberFormatter(format);
      nf.setMinimum(Float.valueOf(0.0f));
      nf.setMaximum(Float.valueOf(999.0f));
      nf.setAllowsInvalid(true);
      nf.setCommitsOnValidEdit(true);

      leftMargin = new JFormattedTextField(nf);
      leftMargin.addFocusListener(this);
      leftMargin.addActionListener(this);
      leftMargin.getAccessibleContext().setAccessibleName(getMsg("label.leftmargin"));
      rightMargin = new JFormattedTextField(nf);
      rightMargin.addFocusListener(this);
      rightMargin.addActionListener(this);
      rightMargin.getAccessibleContext().setAccessibleName(getMsg("label.rightmargin"));
      topMargin = new JFormattedTextField(nf);
      topMargin.addFocusListener(this);
      topMargin.addActionListener(this);
      topMargin.getAccessibleContext().setAccessibleName(getMsg("label.topmargin"));
      bottomMargin = new JFormattedTextField(nf);
      bottomMargin.addFocusListener(this);
      bottomMargin.addActionListener(this);
      bottomMargin.getAccessibleContext().setAccessibleName(getMsg("label.bottommargin"));
      c.gridwidth = GridBagConstraints.RELATIVE;
      lblLeft = new JLabel(getMsg("label.leftmargin") + " " + unitsMsg, JLabel.LEADING);
      lblLeft.setLabelFor(leftMargin);
      addToGB(lblLeft, this, gridbag, c);

      c.gridwidth = GridBagConstraints.REMAINDER;
      lblRight = new JLabel(getMsg("label.rightmargin") + " " + unitsMsg, JLabel.LEADING);
      lblRight.setLabelFor(rightMargin);
      addToGB(lblRight, this, gridbag, c);

      c.gridwidth = GridBagConstraints.RELATIVE;
      addToGB(leftMargin, this, gridbag, c);

      c.gridwidth = GridBagConstraints.REMAINDER;
      addToGB(rightMargin, this, gridbag, c);

      // add an invisible spacing component.
      addToGB(new JPanel(), this, gridbag, c);

      c.gridwidth = GridBagConstraints.RELATIVE;
      lblTop = new JLabel(getMsg("label.topmargin") + " " + unitsMsg, JLabel.LEADING);
      lblTop.setLabelFor(topMargin);
      addToGB(lblTop, this, gridbag, c);

      c.gridwidth = GridBagConstraints.REMAINDER;
      lblBottom = new JLabel(getMsg("label.bottommargin") + " " + unitsMsg, JLabel.LEADING);
      lblBottom.setLabelFor(bottomMargin);
      addToGB(lblBottom, this, gridbag, c);

      c.gridwidth = GridBagConstraints.RELATIVE;
      addToGB(topMargin, this, gridbag, c);

      c.gridwidth = GridBagConstraints.REMAINDER;
      addToGB(bottomMargin, this, gridbag, c);
    }

    private void addToGB(JComponent cbOr, JPanel mediaPanel, GridBagLayout gridbag,
        GridBagConstraints c) {
      gridbag.setConstraints(cbOr, c);
      this.add(cbOr);
    }

    public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      updateMargins(source);
    }

    public void focusLost(FocusEvent e) {
      Object source = e.getSource();
      updateMargins(source);
    }

    @Override
    public void focusGained(FocusEvent e) {
      if (e.getSource() instanceof JFormattedTextField) {
        final JFormattedTextField ff = (JFormattedTextField) e.getSource();
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            ff.selectAll();
          }
        });
      }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateMargins(Object source) {
      if (!(source instanceof JFormattedTextField)) {
        return;
      } else {
        JFormattedTextField tf = (JFormattedTextField) source;
        Float val = (Float) tf.getValue();
        if (val == null) {
          return;
        }
        if (tf == leftMargin && val.equals(lmObj)) {
          return;
        }
        if (tf == rightMargin && val.equals(rmObj)) {
          return;
        }
        if (tf == topMargin && val.equals(tmObj)) {
          return;
        }
        if (tf == bottomMargin && val.equals(bmObj)) {
          return;
        }
      }

      Float lmTmpObj = (Float) leftMargin.getValue();
      Float rmTmpObj = (Float) rightMargin.getValue();
      Float tmTmpObj = (Float) topMargin.getValue();
      Float bmTmpObj = (Float) bottomMargin.getValue();

      float lm = lmTmpObj.floatValue();
      float rm = rmTmpObj.floatValue();
      float tm = tmTmpObj.floatValue();
      float bm = bmTmpObj.floatValue();

      /* adjust for orientation */
      Class orCategory = OrientationRequested.class;
      OrientationRequested or = (OrientationRequested) asCurrent.get(orCategory);

      if (or == null && service != null) {
        or = (OrientationRequested) service.getDefaultAttributeValue(orCategory);
      }

      float tmp;
      if (or == OrientationRequested.REVERSE_PORTRAIT) {
        tmp = lm;
        lm = rm;
        rm = tmp;
        tmp = tm;
        tm = bm;
        bm = tmp;
      } else if (or == OrientationRequested.LANDSCAPE) {
        tmp = lm;
        lm = tm;
        tm = rm;
        rm = bm;
        bm = tmp;
      } else if (or == OrientationRequested.REVERSE_LANDSCAPE) {
        tmp = lm;
        lm = bm;
        bm = rm;
        rm = tm;
        tm = tmp;
      }
      MediaPrintableArea mpa;
      if ((mpa = validateMargins(lm, rm, tm, bm)) != null) {
        asCurrent.add(mpa);
        lmVal = lm;
        rmVal = rm;
        tmVal = tm;
        bmVal = bm;
        lmObj = lmTmpObj;
        rmObj = rmTmpObj;
        tmObj = tmTmpObj;
        bmObj = bmTmpObj;
      } else {
        if (lmObj == null || rmObj == null || tmObj == null || rmObj == null) {
          return;
        } else {
          leftMargin.setValue(lmObj);
          rightMargin.setValue(rmObj);
          topMargin.setValue(tmObj);
          bottomMargin.setValue(bmObj);

        }
      }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private MediaPrintableArea validateMargins(float lm, float rm, float tm, float bm) {
      if (service != null) {

        Class mpaCategory = MediaPrintableArea.class;
        MediaPrintableArea mpaMax = null;
        MediaSize mediaSize = null;

        Media media = (Media) asCurrent.get(Media.class);
        if (media == null || !(media instanceof MediaSizeName)) {
          media = (Media) service.getDefaultAttributeValue(Media.class);
        }
        if (media != null && (media instanceof MediaSizeName msn)) {
          mediaSize = MediaSize.getMediaSizeForName(msn);
        }
        if (mediaSize == null) {
          mediaSize = new MediaSize(8.5f, 11f, Size2DSyntax.INCH);
        }

        if (media != null) {
          PrintRequestAttributeSet tmpASet = new HashPrintRequestAttributeSet(asCurrent);
          tmpASet.add(media);

          Object values = service.getSupportedAttributeValues(mpaCategory, docFlavor, tmpASet);
          if (values instanceof MediaPrintableArea[] areas && areas.length > 0) {
            mpaMax = areas[0];

          }
        }
        if (mpaMax == null) {
          mpaMax =
              new MediaPrintableArea(0f, 0f, mediaSize.getX(units), mediaSize.getY(units), units);
        }

        float wid = mediaSize.getX(units);
        float hgt = mediaSize.getY(units);
        float pax = lm;
        float pay = tm;
        float paw = wid - lm - rm;
        float pah = hgt - tm - bm;

        if (paw <= 0f || pah <= 0f || pax < 0f || pay < 0f || pax < mpaMax.getX(units)
            || paw > mpaMax.getWidth(units) || pay < mpaMax.getY(units)
            || pah > mpaMax.getHeight(units)) {
          return null;
        } else {
          return new MediaPrintableArea(lm, tm, paw, pah, units);
        }
      }
      return null;
    }

    /* This is complex as a MediaPrintableArea is valid only within
     * a particular context of media size.
     * So we need a MediaSize as well as a MediaPrintableArea.
     * MediaSize can be obtained from MediaSizeName.
     * If the application specifies a MediaPrintableArea, we accept it
     * to the extent its valid for the Media they specify. If they
     * don't specify a Media, then the default is assumed.
     *
     * If an application doesn't define a MediaPrintableArea, we need to
     * create a suitable one, this is created using the specified (or
     * default) Media and default 1 inch margins. This is validated
     * against the paper in case this is too large for tiny media.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateInfo() {
      if (service != null) {
        Class mpaCategory = MediaPrintableArea.class;
        MediaPrintableArea mpa = (MediaPrintableArea) asCurrent.get(mpaCategory);
        MediaPrintableArea mpaMax = null;
        MediaSize mediaSize = null;

        Media media = (Media) asCurrent.get(Media.class);
        if (media == null || !(media instanceof MediaSizeName)) {
          media = (Media) service.getDefaultAttributeValue(Media.class);
        }
        if (media != null && (media instanceof MediaSizeName msn)) {
          mediaSize = MediaSize.getMediaSizeForName(msn);
        }
        if (mediaSize == null) {
          mediaSize = new MediaSize(8.5f, 11f, Size2DSyntax.INCH);
        }

        if (media != null) {
          PrintRequestAttributeSet tmpASet = new HashPrintRequestAttributeSet(asCurrent);
          tmpASet.add(media);

          Object values = service.getSupportedAttributeValues(mpaCategory, docFlavor, tmpASet);
          if (values instanceof MediaPrintableArea[] areas && areas.length > 0) {
            mpaMax = areas[0];

          } else if (values instanceof MediaPrintableArea area) {
            mpaMax = area;
          }
        }
        if (mpaMax == null) {
          mpaMax =
              new MediaPrintableArea(0f, 0f, mediaSize.getX(units), mediaSize.getY(units), units);
        }

        /*
         * At this point we now know as best we can :-
         * - the media size
         * - the maximum corresponding printable area
         * - the media printable area specified by the client, if any.
         * The next step is to create a default MPA if none was specified.
         * 1" margins are used unless they are disproportionately
         * large compared to the size of the media.
         */

        float wid = mediaSize.getX(MediaPrintableArea.INCH);
        float hgt = mediaSize.getY(MediaPrintableArea.INCH);
        float maxMarginRatio = 5F;
        float xMgn;
        float yMgn;
        if (wid > maxMarginRatio) {
          xMgn = 1f;
        } else {
          xMgn = wid / maxMarginRatio;
        }
        if (hgt > maxMarginRatio) {
          yMgn = 1f;
        } else {
          yMgn = hgt / maxMarginRatio;
        }

        if (mpa == null) {
          mpa = new MediaPrintableArea(xMgn, yMgn, wid - (2 * xMgn), hgt - (2 * yMgn),
              MediaPrintableArea.INCH);
          asCurrent.add(mpa);
        }
        float pax = mpa.getX(units);
        float pay = mpa.getY(units);
        float paw = mpa.getWidth(units);
        float pah = mpa.getHeight(units);
        float paxMax = mpaMax.getX(units);
        float payMax = mpaMax.getY(units);
        float pawMax = mpaMax.getWidth(units);
        float pahMax = mpaMax.getHeight(units);

        boolean invalid = false;

        // If the paper is set to something which is too small to
        // accommodate a specified printable area, perhaps carried
        // over from a larger paper, the adjustment that needs to be
        // performed should seem the most natural from a user's viewpoint.
        // Since the user is specifying margins, then we are biased
        // towards keeping the margins as close to what is specified as
        // possible, shrinking or growing the printable area.
        // But the API uses printable area, so you need to know the
        // media size in which the margins were previously interpreted,
        // or at least have a record of the margins.
        // In the case that this is the creation of this UI we do not
        // have this record, so we are somewhat reliant on the client
        // to supply a reasonable default
        wid = mediaSize.getX(units);
        hgt = mediaSize.getY(units);
        if (lmVal >= 0f) {
          invalid = true;

          if (lmVal + rmVal > wid) {
            // margins impossible, but maintain P.A if can
            if (paw > pawMax) {
              paw = pawMax;
            }
            // try to centre the printable area.
            pax = (wid - paw) / 2f;
          } else {
            pax = lmVal >= paxMax ? lmVal : paxMax;
            paw = wid - pax - rmVal;
          }
          if (tmVal + bmVal > hgt) {
            if (pah > pahMax) {
              pah = pahMax;
            }
            pay = (hgt - pah) / 2f;
          } else {
            pay = tmVal >= payMax ? tmVal : payMax;
            pah = hgt - pay - bmVal;
          }
        }
        if (pax < paxMax) {
          invalid = true;
          pax = paxMax;
        }
        if (pay < payMax) {
          invalid = true;
          pay = payMax;
        }
        if (paw > pawMax) {
          invalid = true;
          paw = pawMax;
        }
        if (pah > pahMax) {
          invalid = true;
          pah = pahMax;
        }

        if ((pax + paw > paxMax + pawMax) || (paw <= 0f)) {
          invalid = true;
          pax = paxMax;
          paw = pawMax;
        }
        if ((pay + pah > payMax + pahMax) || (pah <= 0f)) {
          invalid = true;
          pay = payMax;
          pah = pahMax;
        }

        if (invalid) {
          mpa = new MediaPrintableArea(pax, pay, paw, pah, units);
          asCurrent.add(mpa);
        }

        /* We now have a valid printable area.
         * Turn it into margins, using the mediaSize
         */
        lmVal = pax;
        tmVal = pay;
        rmVal = mediaSize.getX(units) - pax - paw;
        bmVal = mediaSize.getY(units) - pay - pah;

        lmObj = Float.valueOf(lmVal);
        rmObj = Float.valueOf(rmVal);
        tmObj = Float.valueOf(tmVal);
        bmObj = Float.valueOf(bmVal);

        /* Now we know the values to use, we need to assign them
         * to the fields appropriate for the orientation.
         * Note: if orientation changes this method must be called.
         */
        Class orCategory = OrientationRequested.class;
        OrientationRequested or = (OrientationRequested) asCurrent.get(orCategory);

        if (or == null && service != null) {
          or = (OrientationRequested) service.getDefaultAttributeValue(orCategory);
        }

        Float tmp;

        if (or == OrientationRequested.REVERSE_PORTRAIT) {
          tmp = lmObj;
          lmObj = rmObj;
          rmObj = tmp;
          tmp = tmObj;
          tmObj = bmObj;
          bmObj = tmp;
        } else if (or == OrientationRequested.LANDSCAPE) {
          tmp = lmObj;
          lmObj = bmObj;
          bmObj = rmObj;
          rmObj = tmObj;
          tmObj = tmp;
        } else if (or == OrientationRequested.REVERSE_LANDSCAPE) {
          tmp = lmObj;
          lmObj = tmObj;
          tmObj = rmObj;
          rmObj = bmObj;
          bmObj = tmp;
        }

        leftMargin.setValue(lmObj);
        rightMargin.setValue(rmObj);
        topMargin.setValue(tmObj);
        bottomMargin.setValue(bmObj);
      }
    }
  }

  class PrintRangePanel extends JPanel implements ActionListener, FocusListener {
    @Serial
    private static final long serialVersionUID = 2524487233256169427L;
    private final String strTitle = getMsg("border.printrange");
    private final PageRanges prAll = new PageRanges(1, Integer.MAX_VALUE);
    private JRadioButton rbAll;
    private JRadioButton rbPages;
    private JFormattedTextField tfRangeFrom;
    private JFormattedTextField tfRangeTo;
    private JLabel lblRangeTo;
    private boolean prSupported;

    public PrintRangePanel() {
      super();

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();

      setLayout(gridbag);
      setBorder(BorderFactory.createTitledBorder(strTitle));

      c.fill = GridBagConstraints.BOTH;
      c.insets = new Insets(6, 3, 6, 3);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.LINE_START;

      ButtonGroup bg = new ButtonGroup();
      JPanel pnlTop = new JPanel(new BorderLayout());
      rbAll = new JRadioButton(getMsg("radiobutton.rangeall"));
      rbAll.addActionListener(this);
      rbAll.setSelected(true);
      bg.add(rbAll);
      pnlTop.add(rbAll, BorderLayout.PAGE_START);
      addToGB(pnlTop, this, gridbag, c);

      JPanel pnlBottom = new JPanel(new BorderLayout());
      JPanel pnlBottom2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
      rbPages = new JRadioButton(getMsg("radiobutton.rangepages"));
      rbPages.addActionListener(this);
      bg.add(rbPages);
      pnlBottom.add(rbPages, BorderLayout.PAGE_START);
      DecimalFormat format = new DecimalFormat("####0");
      format.setMinimumFractionDigits(0);
      format.setMaximumFractionDigits(0);
      format.setMinimumIntegerDigits(0);
      format.setMaximumIntegerDigits(5);
      format.setParseIntegerOnly(true);
      format.setDecimalSeparatorAlwaysShown(false);
      NumberFormatter nf = new NumberFormatter(format);
      nf.setMinimum(Integer.valueOf(1));
      nf.setMaximum(Integer.valueOf(Integer.MAX_VALUE));
      nf.setAllowsInvalid(true);
      nf.setCommitsOnValidEdit(true);
      tfRangeFrom = new JFormattedTextField(nf);
      tfRangeFrom.setColumns(4);
      tfRangeFrom.setEnabled(false);
      tfRangeFrom.addActionListener(this);
      tfRangeFrom.addFocusListener(this);
      tfRangeFrom.setFocusLostBehavior(JFormattedTextField.PERSIST);
      tfRangeFrom.getAccessibleContext().setAccessibleName(getMsg("radiobutton.rangepages"));
      pnlBottom2.add(tfRangeFrom);
      lblRangeTo = new JLabel(getMsg("label.rangeto"));
      lblRangeTo.setEnabled(false);
      pnlBottom2.add(lblRangeTo);
      NumberFormatter nfto;
      try {
        nfto = (NumberFormatter) nf.clone();
      } catch (CloneNotSupportedException e) {
        nfto = new NumberFormatter();
      }
      tfRangeTo = new JFormattedTextField(nfto);
      tfRangeTo.setColumns(4);
      tfRangeTo.setEnabled(false);
      tfRangeTo.addFocusListener(this);
      tfRangeTo.getAccessibleContext().setAccessibleName(getMsg("label.rangeto"));
      pnlBottom2.add(tfRangeTo);
      pnlBottom.add(pnlBottom2, BorderLayout.PAGE_END);
      addToGB(pnlBottom, this, gridbag, c);
    }

    private void addToGB(JComponent cbOr, JPanel mediaPanel, GridBagLayout gridbag,
        GridBagConstraints c) {
      gridbag.setConstraints(cbOr, c);
      this.add(cbOr);
    }

    public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();

      setupRangeWidgets();

      if (source == rbAll) {
        asCurrent.add(prAll);
      } else {
        updateRangeAttribute();
      }

    }

    public void focusLost(FocusEvent e) {
      Object source = e.getSource();

      if ((source == tfRangeFrom) || (source == tfRangeTo)) {
        updateRangeAttribute();
      }
    }

    public void focusGained(FocusEvent e) {
      Object source = e.getSource();
      if ((source == tfRangeFrom) || (source == tfRangeTo)) {
        final JFormattedTextField ff = (JFormattedTextField) source;
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            ff.selectAll();
          }
        });
      }
    }

    private void setupRangeWidgets() {
      boolean rangeEnabled = rbPages.isSelected() && prSupported;
      tfRangeFrom.setEnabled(rangeEnabled);
      tfRangeTo.setEnabled(rangeEnabled);
      lblRangeTo.setEnabled(rangeEnabled);
    }

    private void updateRangeAttribute() {
      String strFrom = tfRangeFrom.getText();
      String strTo = tfRangeTo.getText();

      int min;
      int max;

      try {
        min = Integer.parseInt(strFrom);
      } catch (NumberFormatException e) {
        min = 1;
      }

      try {
        max = Integer.parseInt(strTo);
      } catch (NumberFormatException e) {
        max = min;
      }

      if (min < 1) {
        min = 1;
        tfRangeFrom.setValue(Integer.valueOf(1));
      }

      if (max < min) {
        max = min;
        tfRangeTo.setValue(Integer.valueOf(min));
      }

      PageRanges pr = new PageRanges(min, max);
      asCurrent.add(pr);
    }

    @SuppressWarnings({"restriction", "rawtypes", "unchecked"})
    public void updateInfo() {
      if (service != null) {
        Class prCategory = PageRanges.class;
        prSupported = false;

        if (service.isAttributeCategorySupported(prCategory)) {
          prSupported = true;
        }

        sun.print.SunPageSelection select = sun.print.SunPageSelection.ALL;
        int min = 1;
        int max = 1;

        PageRanges pr = (PageRanges) asCurrent.get(prCategory);
        if (pr != null) {
          if (!pr.equals(prAll)) {
            select = sun.print.SunPageSelection.RANGE;

            int[][] members = pr.getMembers();
            if ((members.length > 0) && (members[0].length > 1)) {
              min = members[0][0];
              max = members[0][1];
            }
          }
        }

        if (select == sun.print.SunPageSelection.ALL) {
          rbAll.setSelected(true);
        } else { // RANGE
          rbPages.setSelected(true);
        }
        tfRangeFrom.setValue(Integer.valueOf(min));
        tfRangeTo.setValue(Integer.valueOf(max));
        rbAll.setEnabled(prSupported);
        rbPages.setEnabled(prSupported);
        setupRangeWidgets();
      }
    }
  }

  public static void main(String[] args) {
    // new WebPrintDialog(new PrintService[] { WebPrintService.getService() }, 0, new
    // HashPrintRequestAttributeSet(), null).setVisible(true);
    new WebPrintDialog(
        new PrintService[] {WebPrintService.getService(),
            PrintServiceLookup.lookupDefaultPrintService()},
        0, new HashPrintRequestAttributeSet(), null).setVisible(true);
    // new WebPrintDialog(PrintServiceLookup.lookupPrintServices(null,null), 0, new
    // HashPrintRequestAttributeSet(), null).setVisible(true);
  }

}
