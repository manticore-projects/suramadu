package org.webswing.toolkit;

import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.awt.HeadlessException;
import java.awt.print.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WebPrinterJob extends PrinterJob {
  private final PrintRequestAttributeSet attribs = new HashPrintRequestAttributeSet();
  private Printable printable;
  private Pageable pageable;
  private final PrintService service;

  public WebPrinterJob() {
    service = WebPrintService.getService();
  }

  @Override
  public void setPrintable(Printable painter) {
    this.printable = painter;
  }

  @Override
  public void setPrintable(Printable painter, PageFormat format) {
    setPrintable(painter);
  }

  @Override
  public void setPageable(Pageable document) throws NullPointerException {
    this.pageable = document;
  }

  @Override
  public boolean printDialog() throws HeadlessException {
    return true; // implemented in WebPrinterJobWrapper
  }

  @Override
  public PageFormat pageDialog(PageFormat page) throws HeadlessException {
    return page; // implemented in WebPrinterJobWrapper
  }

  @Override
  public PageFormat defaultPage(PageFormat page) {
    return defaultPage();
  }

  @Override
  public PageFormat defaultPage() {
    HashPrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
    attrs.add((OrientationRequested) service.getDefaultAttributeValue(OrientationRequested.class));
    attrs.add((MediaSizeName) service.getDefaultAttributeValue(Media.class));
    attrs.add((MediaPrintableArea) service.getDefaultAttributeValue(MediaPrintableArea.class));
    return WebPrinterJobWrapper.toPageFormat(attrs);
  }

  @Override
  public PageFormat validatePage(PageFormat page) {
    return page; // implemented in WebPrinterJobWrapper
  }

  @Override
  public void print(PrintRequestAttributeSet attributes) throws PrinterException {
    this.attribs.addAll(attributes);
    print();
  }

  @Override
  public void print() throws PrinterException {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      Services.getPdfService().printToPDF(out, pageable, printable, attribs);
      servePDF(out);
    } catch (IOException e) {
      AppLogger.error("IOException while printing to PDF!", e);
    }
  }

  private void servePDF(ByteArrayOutputStream out) {
    Util.getWebToolkit().getPaintDispatcher().notifyPrintPdfFile(out);
  }

  @Override
  public void setCopies(int copies) {}

  @Override
  public int getCopies() {
    return 1;
  }

  @Override
  public String getUserName() {
    return "";
  }

  @Override
  public void setJobName(String jobName) {}

  @Override
  public String getJobName() {
    return "";
  }

  @Override
  public void cancel() {}

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public PrintService getPrintService() {
    return service;
  }

  @Override
  public void setPrintService(PrintService service) throws PrinterException {}

}
