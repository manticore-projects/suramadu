package org.webswing.ext.services;

import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.OutputStream;

public interface PdfService {

  void printToPDF(OutputStream out, Pageable pageable, Printable printable,
      PrintRequestAttributeSet attribs) throws PrinterException, IOException;

}
