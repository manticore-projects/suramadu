package org.webswing.toolkit;

import org.webswing.Constants;
import org.webswing.toolkit.util.WebPrintDialog;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import javax.swing.JOptionPane;
import java.awt.AWTError;
import java.awt.HeadlessException;
import java.awt.print.*;
import java.util.Objects;

public class WebPrinterJobWrapper extends PrinterJob {
  public static final int DPI = 72;

  private PrinterJob delegate;
  private final PrintRequestAttributeSet attribs = new HashPrintRequestAttributeSet();

  public WebPrinterJobWrapper() {
    String nm = System.getProperty(Constants.PRINTER_JOB_CLASS, null);
    try {
      delegate = (PrinterJob) Class.forName(nm).newInstance();
    } catch (ClassNotFoundException e) {
      throw new AWTError("PrinterJob not found: " + nm);
    } catch (InstantiationException e) {
      throw new AWTError("Could not instantiate PrinterJob: " + nm);
    } catch (IllegalAccessException e) {
      throw new AWTError("Could not access PrinterJob: " + nm);
    }
    setPageFormat(defaultPage());
  }

  private void setPageFormat(PageFormat defaultPage) {
    PrintRequestAttributeSet a = toAttributes(defaultPage);
    attribs.addAll(a);
  }

  @Override
  public void setPrintable(Printable painter) {
    delegate.setPrintable(painter);
  }

  @Override
  public void setPrintable(Printable painter, PageFormat format) {
    delegate.setPrintable(painter, format);
    setPageFormat(format);
  }

  @Override
  public void setPageable(Pageable document) throws NullPointerException {
    delegate.setPageable(document);
  }

  @Override
  public void setPrintService(PrintService service) throws PrinterException {
    delegate.setPrintService(service);
  }

  @Override
  public boolean printDialog() throws HeadlessException {
    PrintService[] services = wrappedLookupPrintServices(null, null);
    int defaultServiceIndex = getIndexOfPrintService(services);
    WebPrintDialog dialog = new WebPrintDialog(services, defaultServiceIndex, attribs, null);
    dialog.setVisible(true);
    boolean confirmed = dialog.getStatus() == 1 ? true : false;
    if (confirmed) {
      try {
        delegate.setPrintService(dialog.getService());
      } catch (PrinterException e) {
        JOptionPane.showMessageDialog(null,
            "There is a problem with selected Print Service.\n" + e.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      PrintRequestAttributeSet a = dialog.getAttributes();
      attribs.addAll(a);
    }
    return confirmed;
  }

  private int getIndexOfPrintService(PrintService[] services) {
    PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
    if (defaultService != null) {
      for (int i = 0; i < services.length; i++) {
        PrintService s = services[i];
        if (s.getName().equals(defaultService.getName())) {
          return i;
        }
      }
    }
    return -1;

  }

  public boolean printDialog(PrintRequestAttributeSet attributes) throws HeadlessException {
    Objects.requireNonNull(attributes, "attributes");
    this.attribs.addAll(attributes);
    boolean result = printDialog();
    attributes.addAll(attribs);
    return result;
  }

  @Override
  public PageFormat pageDialog(PageFormat page) throws HeadlessException {
    setPageFormat(page);
    printDialog();
    return toPageFormat(attribs);
  }

  public PageFormat pageDialog(PrintRequestAttributeSet attributes) throws HeadlessException {

    Objects.requireNonNull(attributes, "attributes");
    this.attribs.addAll(attributes);
    printDialog();
    return toPageFormat(attribs);
  }

  @Override
  public PageFormat defaultPage(PageFormat page) {
    return delegate.defaultPage(page);
  }

  @Override
  public PageFormat validatePage(PageFormat page) {
    return delegate.validatePage(page);
  }

  @Override
  public void print() throws PrinterException {
    delegate.print(this.attribs);
  }

  @Override
  public void print(PrintRequestAttributeSet attributes) throws PrinterException {
    this.attribs.addAll(attributes);
    delegate.print(attributes);
  }

  @Override
  public void setCopies(int copies) {
    delegate.setCopies(copies);
  }

  @Override
  public int getCopies() {
    return delegate.getCopies();
  }

  @Override
  public String getUserName() {
    return delegate.getUserName();
  }

  @Override
  public void setJobName(String jobName) {
    delegate.setJobName(jobName);
  }

  @Override
  public String getJobName() {
    return delegate.getJobName();
  }

  @Override
  public void cancel() {
    delegate.cancel();
  }

  @Override
  public boolean isCancelled() {
    return delegate.isCancelled();
  }

  @Override
  public PrintService getPrintService() {
    return delegate.getPrintService();
  }

  private PrintRequestAttributeSet toAttributes(PageFormat format) {
    HashPrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
    OrientationRequested orientation =
        format.getOrientation() == PageFormat.LANDSCAPE ? OrientationRequested.LANDSCAPE
            : OrientationRequested.PORTRAIT;
    Paper p = format.getPaper();
    MediaSizeName size = findMedia(p.getWidth(), p.getHeight());
    float x = (float) p.getImageableY() / DPI;
    float y = (float) p.getImageableX() / DPI;
    float w = (float) p.getImageableWidth() / DPI;
    float h = (float) p.getImageableHeight() / DPI;
    MediaPrintableArea margins = new MediaPrintableArea(x, y, w, h, MediaPrintableArea.INCH);
    attrs.add(size);
    attrs.add(orientation);
    attrs.add(margins);
    return attrs;
  }

  public static MediaSizeName findMedia(double width, double height) {
    MediaSizeName[] mediaArray = WebPrintService.mediaSizes;
    MediaSizeName result = WebPrintService.mediaSizes[0];
    float w = (float) (width / DPI);
    float h = (float) (height / DPI);
    if ((w <= 0.0F) || (h <= 0.0F)) {
      throw new IllegalArgumentException("args must be +ve values");
    }

    double d1 = w * w + h * h;

    float f1 = w;
    float f2 = h;

    for (int i = 0; i < mediaArray.length; i++) {
      MediaSizeName mediaSizeName = (MediaSizeName) mediaArray[i];
      MediaSize mSize = MediaSize.getMediaSizeForName(mediaSizeName);
      float[] size = mSize.getSize(MediaSize.INCH);
      if ((w == size[0]) && (h == size[1])) {
        result = mediaSizeName;
        break;
      }
      f1 = w - size[0];
      f2 = h - size[1];
      double d2 = f1 * f1 + f2 * f2;
      if (d2 < d1) {
        d1 = d2;
        result = mediaSizeName;
      }
    }

    return result;
  }

  public static PrintService[] wrappedLookupPrintServices(DocFlavor flavor,
      AttributeSet attributes) {
    String delegateClass = System.getProperty(Constants.PRINTER_JOB_CLASS, null);
    if (delegateClass.equals(WebPrinterJob.class.getCanonicalName())) {
      return new PrintService[] {WebPrintService.getService()};
    } else {
      PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, attributes);
      if (services == null) {
        services = new PrintService[] {};
      }
      return services;
    }
  }

  public static PageFormat toPageFormat(PrintRequestAttributeSet a) {
    OrientationRequested orientation = (OrientationRequested) a.get(OrientationRequested.class);
    MediaSizeName name = (MediaSizeName) a.get(Media.class);
    MediaPrintableArea area = (MediaPrintableArea) a.get(MediaPrintableArea.class);
    PageFormat result = new PageFormat();
    if (orientation != null) {
      if (orientation == OrientationRequested.LANDSCAPE) {
        result.setOrientation(PageFormat.LANDSCAPE);
      } else {
        result.setOrientation(PageFormat.PORTRAIT);
      }
    }
    Paper p = new Paper();
    if (name != null) {
      MediaSize msz = MediaSize.getMediaSizeForName(name);
      if (msz != null) {
        double paperWid = msz.getX(MediaSize.INCH) * DPI;
        double paperHgt = msz.getY(MediaSize.INCH) * DPI;
        p.setSize(paperWid, paperHgt);
        if (area == null) {
          p.setImageableArea(DPI, DPI, paperWid - 1 * DPI, paperHgt - 1 * DPI);
        }
      }
    }
    if (area != null) {
      float[] printableArea = area.getPrintableArea(MediaPrintableArea.INCH);
      for (int i = 0; i < printableArea.length; i++) {
        printableArea[i] = printableArea[i] * DPI;
      }
      p.setImageableArea(printableArea[0], printableArea[1], printableArea[2], printableArea[3]);
    }
    result.setPaper(p);
    return result;
  }

}
