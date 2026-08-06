package org.webswing.toolkit;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import java.awt.print.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebPrintJob implements DocPrintJob {

  private static final int JOB_COMPLETED = 102;
  private static final int JOB_NOMOREEVENTS = 105;
  private static final int JOB_DATA_TRANSFER_COMPLETED = 106;
  private static final int JOB_FAILED = 103;
  private static final int JOB_CANCELED = 101;
  private transient List<PrintJobListener> jobListeners;
  private PrintJobAttributeSet jobAttrSet;
  private final WebPrintService service;
  private boolean printing;
  private PrinterJob job;
  private MediaSize mediaSize = MediaSize.NA.LETTER;
  private OrientationRequested orient = OrientationRequested.PORTRAIT;
  private HashPrintRequestAttributeSet reqAttrSet;

  public WebPrintJob(WebPrintService webPrintService) {
    this.service = webPrintService;
  }

  @Override
  public void addPrintJobAttributeListener(PrintJobAttributeListener listener,
      PrintJobAttributeSet attributes) {}

  @Override
  public void addPrintJobListener(PrintJobListener listener) {
    synchronized (this) {
      if (listener == null) {
        return;
      }
      if (this.jobListeners == null) {
        this.jobListeners = new ArrayList<PrintJobListener>();
      }
      this.jobListeners.add(listener);
    }
  }

  @Override
  public PrintJobAttributeSet getAttributes() {
    synchronized (this) {
      if (this.jobAttrSet == null) {
        HashPrintJobAttributeSet localHashPrintJobAttributeSet = new HashPrintJobAttributeSet();
        return AttributeSetUtilities.unmodifiableView(localHashPrintJobAttributeSet);
      }
      return this.jobAttrSet;
    }
  }

  @Override
  public PrintService getPrintService() {
    return service;
  }

  @Override
  public void print(Doc doc, PrintRequestAttributeSet attributes) throws PrintException {
    synchronized (this) {
      if (this.printing) {
        throw new PrintException("already printing");
      }
      this.printing = true;
    }
    DocFlavor flavor = doc.getDocFlavor();
    try {
      doc.getPrintData();
    } catch (IOException localIOException1) {
      notifyEvent(JOB_FAILED);
      throw new PrintException("can't get print data: " + localIOException1.toString());
    }
    if ((flavor == null) || (!this.service.isDocFlavorSupported((DocFlavor) flavor))) {
      notifyEvent(JOB_FAILED);
      throw new PrintException("invalid flavor");
    }

    initializeAttributeSets(doc, attributes);

    getAttributeValues((DocFlavor) flavor);

    String str = ((DocFlavor) flavor).getRepresentationClassName();
    if ("java.awt.print.Pageable".equals(str)) {
      try {
        pageableJob((Pageable) doc.getPrintData(), attributes);
        return;
      } catch (ClassCastException localClassCastException3) {
        notifyEvent(103);
        throw new PrintException(localClassCastException3);
      } catch (IOException localIOException3) {
        notifyEvent(103);
        throw new PrintException(localIOException3);
      }
    }
    if ("java.awt.print.Printable".equals(str)) {
      try {
        printableJob((Printable) doc.getPrintData(), attributes);
        return;
      } catch (ClassCastException localClassCastException4) {
        notifyEvent(103);
        throw new PrintException(localClassCastException4);
      } catch (IOException localIOException4) {
        notifyEvent(103);
        throw new PrintException(localIOException4);
      }
    }
    notifyEvent(103);
    throw new PrintException("unrecognized class: " + str);
  }

  private void printableJob(Printable printData, PrintRequestAttributeSet attributes)
      throws PrintException {
    try {
      synchronized (this) {
        if (this.job != null) {
          throw new PrintException("already printing");
        }
        this.job = PrinterJob.getPrinterJob();;
      }
      this.job.setPrintService(getPrintService());
      PageFormat pf = new PageFormat();
      if (this.mediaSize != null) {
        Paper localPaper = new Paper();
        localPaper.setSize(this.mediaSize.getX(25400) * 72.0D, this.mediaSize.getY(25400) * 72.0D);
        localPaper.setImageableArea(72.0D, 72.0D, localPaper.getWidth() - 144.0D,
            localPaper.getHeight() - 144.0D);
        ((PageFormat) pf).setPaper(localPaper);
      }
      if (this.orient == OrientationRequested.REVERSE_LANDSCAPE) {
        ((PageFormat) pf).setOrientation(2);
      } else if (this.orient == OrientationRequested.LANDSCAPE) {
        ((PageFormat) pf).setOrientation(0);
      }
      this.job.setPrintable(printData);
      this.job.print(attributes);
      notifyEvent(JOB_COMPLETED);
    } catch (PrinterException localPrinterException) {
      notifyEvent(JOB_FAILED);
      throw new PrintException(localPrinterException);
    }
  }

  private void pageableJob(Pageable printData, PrintRequestAttributeSet attributes)
      throws PrintException {
    try {
      synchronized (this) {
        if (this.job != null) {
          throw new PrintException("already printing");
        }
        this.job = PrinterJob.getPrinterJob();;
      }
      this.job.setPrintService(getPrintService());
      this.job.setPageable(printData);
      this.job.print(attributes);
      notifyEvent(JOB_COMPLETED);
    } catch (PrinterException localPrinterException) {
      notifyEvent(JOB_FAILED);
      throw new PrintException(localPrinterException);
    }

  }

  @Override
  public void removePrintJobAttributeListener(PrintJobAttributeListener listener) {}

  @Override
  public void removePrintJobListener(PrintJobListener listener) {
    synchronized (this) {
      if ((listener == null) || (this.jobListeners == null)) {
        return;
      }
      this.jobListeners.remove(listener);
      if (this.jobListeners.isEmpty()) {
        this.jobListeners = null;
      }
    }
  }

  private void notifyEvent(int eventType) {
    synchronized (this) {
      if (this.jobListeners != null) {
        PrintJobEvent jobEvent = new PrintJobEvent(this, eventType);
        for (int i = 0; i < this.jobListeners.size(); i++) {
          PrintJobListener listener = this.jobListeners.get(i);
          switch (eventType) {
            case JOB_CANCELED:
              listener.printJobCanceled(jobEvent);
              break;

            case JOB_FAILED:
              listener.printJobFailed(jobEvent);
              break;

            case JOB_DATA_TRANSFER_COMPLETED:
              listener.printDataTransferCompleted(jobEvent);
              break;

            case JOB_NOMOREEVENTS:
              listener.printJobNoMoreEvents(jobEvent);
              break;

            case JOB_COMPLETED:
              listener.printJobCompleted(jobEvent);
          }
        }
      }
    }
  }

  private synchronized void initializeAttributeSets(Doc paramDoc,
      PrintRequestAttributeSet paramPrintRequestAttributeSet) {
    this.reqAttrSet = new HashPrintRequestAttributeSet();
    this.jobAttrSet = new HashPrintJobAttributeSet();

    Attribute[] arrayOfAttribute;
    if (paramPrintRequestAttributeSet != null) {
      this.reqAttrSet.addAll(paramPrintRequestAttributeSet);
      arrayOfAttribute = paramPrintRequestAttributeSet.toArray();
      for (int i = 0; i < arrayOfAttribute.length; i++) {
        if (arrayOfAttribute[i] instanceof PrintJobAttribute) {
          this.jobAttrSet.add(arrayOfAttribute[i]);
        }
      }
    }

    DocAttributeSet localDocAttributeSet = paramDoc.getAttributes();
    if (localDocAttributeSet != null) {
      arrayOfAttribute = localDocAttributeSet.toArray();
      for (int j = 0; j < arrayOfAttribute.length; j++) {
        if (arrayOfAttribute[j] instanceof PrintRequestAttribute) {
          this.reqAttrSet.add(arrayOfAttribute[j]);
        }
        if (arrayOfAttribute[j] instanceof PrintJobAttribute) {
          this.jobAttrSet.add(arrayOfAttribute[j]);
        }
      }
    }

    String str = "";
    try {
      str = System.getProperty("user.name");
    } catch (SecurityException localSecurityException) {
    }
    Object localObject1;
    if ((str == null) || ("".equals(str))) {
      localObject1 =
          (RequestingUserName) paramPrintRequestAttributeSet.get(RequestingUserName.class);

      if (localObject1 != null) {
        this.jobAttrSet
            .add(new JobOriginatingUserName(((RequestingUserName) localObject1).getValue(),
                ((RequestingUserName) localObject1).getLocale()));
      } else {
        this.jobAttrSet.add(new JobOriginatingUserName("", null));
      }
    } else {
      this.jobAttrSet.add(new JobOriginatingUserName(str, null));
    }

    if (this.jobAttrSet.get(JobName.class) == null) {
      Object localObject2;
      if ((localDocAttributeSet != null)
          && (localDocAttributeSet.get(DocumentName.class) != null)) {
        localObject2 = (DocumentName) localDocAttributeSet.get(DocumentName.class);

        localObject1 = new JobName(((DocumentName) localObject2).getValue(),
            ((DocumentName) localObject2).getLocale());
        this.jobAttrSet.add((Attribute) localObject1);
      } else {
        localObject2 = "JPS Job:" + paramDoc;
        try {
          Object localObject3 = paramDoc.getPrintData();
          if (localObject3 instanceof URL) {
            localObject2 = ((URL) paramDoc.getPrintData()).toString();
          }
        } catch (IOException localIOException) {
        }
        localObject1 = new JobName((String) localObject2, null);
        this.jobAttrSet.add((Attribute) localObject1);
      }
    }

    this.jobAttrSet = AttributeSetUtilities.unmodifiableView(this.jobAttrSet);
  }

  private void getAttributeValues(DocFlavor paramDocFlavor) throws PrintException {
    Attribute[] arrayOfAttribute = this.reqAttrSet.toArray();
    for (int i = 0; i < arrayOfAttribute.length; i++) {
      Attribute localAttribute = arrayOfAttribute[i];
      Class<?> localClass = localAttribute.getCategory();
      if (localClass == Media.class) {
        if ((localAttribute instanceof MediaSizeName name)
            && (this.service.isAttributeValueSupported(localAttribute, null, null))) {
          this.mediaSize = MediaSize.getMediaSizeForName(name);
        }
      } else if (localClass == OrientationRequested.class) {
        this.orient = (OrientationRequested) localAttribute;
      }
    }
  }
}
