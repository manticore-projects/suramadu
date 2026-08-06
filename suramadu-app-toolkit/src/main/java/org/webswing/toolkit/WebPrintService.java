package org.webswing.toolkit;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.ServiceUIFactory;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.PrintServiceAttributeListener;
import java.util.Locale;

public class WebPrintService implements PrintService {

  static final DocFlavor[] supportedDocFlavors =
      {DocFlavor.SERVICE_FORMATTED.PAGEABLE, DocFlavor.SERVICE_FORMATTED.PRINTABLE};
  private static final Class<?>[] suppAttrCats = {Chromaticity.class, Copies.class, Fidelity.class,
      JobName.class, Media.class, MediaPrintableArea.class, OrientationRequested.class,
      PageRanges.class, RequestingUserName.class, SheetCollate.class, Sides.class};
  public static final MediaSizeName[] mediaSizes = {MediaSizeName.ISO_A3, MediaSizeName.ISO_A4,
      MediaSizeName.ISO_A5, MediaSizeName.ISO_A6, MediaSizeName.NA_LETTER, MediaSizeName.NA_LEGAL,
      MediaSizeName.LEDGER, MediaSizeName.EXECUTIVE};

  private static final WebPrintService thisService = new WebPrintService();

  public static WebPrintService getService() {
    return thisService;
  }

  @Override
  public String getName() {
    return "WebPrintService";
  }

  @Override
  public DocPrintJob createPrintJob() {
    return new WebPrintJob(this);
  }

  @Override
  public void addPrintServiceAttributeListener(PrintServiceAttributeListener listener) {}

  @Override
  public void removePrintServiceAttributeListener(PrintServiceAttributeListener listener) {}

  @Override
  public PrintServiceAttributeSet getAttributes() {
    HashPrintServiceAttributeSet set = new HashPrintServiceAttributeSet();
    set.add(ColorSupported.SUPPORTED);

    return AttributeSetUtilities.unmodifiableView(set);
  }

  @SuppressWarnings("unchecked")
  public <T extends PrintServiceAttribute> T getAttribute(Class<T> category) {
    if (category == ColorSupported.class) {
      return (T) ColorSupported.SUPPORTED;
    }
    return null;
  }

  @Override
  public DocFlavor[] getSupportedDocFlavors() {
    DocFlavor[] arrayOfDocFlavor = new DocFlavor[supportedDocFlavors.length];
    System.arraycopy(supportedDocFlavors, 0, arrayOfDocFlavor, 0, arrayOfDocFlavor.length);
    return arrayOfDocFlavor;
  }

  @Override
  public boolean isDocFlavorSupported(DocFlavor flavor) {
    DocFlavor[] arrayOfDocFlavor = getSupportedDocFlavors();
    for (int i = 0; i < arrayOfDocFlavor.length; i++) {
      if (flavor.equals(arrayOfDocFlavor[i])) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Class<?>[] getSupportedAttributeCategories() {
    Class<?>[] arrayOfClass = new Class[suppAttrCats.length];
    System.arraycopy(suppAttrCats, 0, arrayOfClass, 0, arrayOfClass.length);
    return arrayOfClass;
  }

  @Override
  public boolean isAttributeCategorySupported(Class<? extends Attribute> category) {
    for (int i = 0; i < suppAttrCats.length; i++) {
      if (category == suppAttrCats[i]) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Object getDefaultAttributeValue(Class<? extends Attribute> category) {
    if (!isAttributeCategorySupported(category)) {
      return null;
    }
    if (category == Copies.class) {
      return new Copies(1);
    }
    if (category == Chromaticity.class) {
      return Chromaticity.COLOR;
    }
    if (category == Fidelity.class) {
      return Fidelity.FIDELITY_FALSE;
    }
    String str;
    if (category == Media.class) {
      str = Locale.getDefault().getCountry();
      if ((str != null) && (("".equals(str)) || (str.equals(Locale.US.getCountry()))
          || (str.equals(Locale.CANADA.getCountry())))) {
        return MediaSizeName.NA_LETTER;
      }
      return MediaSizeName.ISO_A4;
    }
    if (category == MediaPrintableArea.class) {
      str = Locale.getDefault().getCountry();
      float margin = 1.0F;
      float w;
      float h;
      if ((str != null) && (("".equals(str)) || (str.equals(Locale.US.getCountry()))
          || (str.equals(Locale.CANADA.getCountry())))) {
        w = MediaSize.NA.LETTER.getX(MediaSize.INCH) - margin * 2;
        h = MediaSize.NA.LETTER.getY(MediaSize.INCH) - margin * 2;
      } else {
        w = MediaSize.ISO.A4.getX(MediaSize.INCH) - margin * 2;
        h = MediaSize.ISO.A4.getY(MediaSize.INCH) - margin * 2;
      }
      return new MediaPrintableArea(margin, margin, w, h, MediaSize.INCH);
    }
    if (category == OrientationRequested.class) {
      return OrientationRequested.PORTRAIT;
    }
    if (category == PageRanges.class) {
      return new PageRanges(1, Integer.MAX_VALUE);
    }
    if (category == SheetCollate.class) {
      return SheetCollate.UNCOLLATED;
    }
    if (category == Sides.class) {
      return Sides.ONE_SIDED;
    }

    return null;
  }

  @Override
  public Object getSupportedAttributeValues(Class<? extends Attribute> type,
      DocFlavor paramDocFlavor, AttributeSet attribs) {
    if (!isAttributeCategorySupported(type)) {
      return null;
    }
    Object[] supportedValues;
    if (type == Chromaticity.class) {
      supportedValues = new Chromaticity[1];
      supportedValues[0] = Chromaticity.COLOR;

      return supportedValues;
    }
    if (type == JobName.class) {
      return new JobName("", null);
    }
    if (type == RequestingUserName.class) {
      return new RequestingUserName("", null);
    }
    if (type == OrientationRequested.class) {
      if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE))
          || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE))) {

        supportedValues = new OrientationRequested[3];
        supportedValues[0] = OrientationRequested.PORTRAIT;
        supportedValues[1] = OrientationRequested.LANDSCAPE;
        return supportedValues;
      }
      return null;
    }
    if ((type == Copies.class) || (type == CopiesSupported.class)) {
      return new CopiesSupported(1, 1);
    }
    if (type == Media.class) {
      supportedValues = new Media[mediaSizes.length];
      System.arraycopy(mediaSizes, 0, supportedValues, 0, mediaSizes.length);
      return supportedValues;
    }
    if (type == Fidelity.class) {
      supportedValues = new Fidelity[2];
      supportedValues[0] = Fidelity.FIDELITY_FALSE;
      supportedValues[1] = Fidelity.FIDELITY_TRUE;
      return supportedValues;
    }
    if (type == MediaPrintableArea.class) {
      if (attribs == null) {
        return null;
      }
      MediaSize size = (MediaSize) attribs.get(MediaSize.class);
      if (size == null) {
        Media media = (Media) attribs.get(Media.class);
        if ((media != null) && (media instanceof MediaSizeName mediaName)) {
          size = MediaSize.getMediaSizeForName(mediaName);
        }
      }
      if (size == null) {
        return null;
      }
      MediaPrintableArea[] result = new MediaPrintableArea[1];
      float w = ((MediaSize) size).getX(MediaSize.INCH);
      float h = ((MediaSize) size).getY(MediaSize.INCH);
      result[0] = new MediaPrintableArea(0, 0, w, h, MediaSize.INCH);

      return result;
    }
    if (type == PageRanges.class) {
      if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE))
          || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE))) {

        supportedValues = new PageRanges[1];
        supportedValues[0] = new PageRanges(1, Integer.MAX_VALUE);
        return supportedValues;
      }
      return null;
    }
    if (type == SheetCollate.class) {
      if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE))
          || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE))) {

        supportedValues = new SheetCollate[2];
        supportedValues[0] = SheetCollate.UNCOLLATED;
        supportedValues[1] = SheetCollate.COLLATED;
        return supportedValues;
      }
      supportedValues = new SheetCollate[1];
      supportedValues[0] = SheetCollate.UNCOLLATED;
      return supportedValues;
    }
    if (type == Sides.class) {
      if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE))
          || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE))) {

        supportedValues = new Sides[3];
        supportedValues[0] = Sides.ONE_SIDED;
        return supportedValues;
      }
      return null;
    }

    return null;
  }

  @Override
  public boolean isAttributeValueSupported(Attribute paramAttribute, DocFlavor paramDocFlavor,
      AttributeSet set) {
    Class<? extends Attribute> attributeClass = paramAttribute.getCategory();
    if (!isAttributeCategorySupported(attributeClass)) {
      return false;
    }
    if (paramAttribute.getCategory() == Chromaticity.class) {
      return paramAttribute == Chromaticity.COLOR;
    }
    if (paramAttribute.getCategory() == Copies.class) {
      return ((Copies) paramAttribute).getValue() == 1;
    }
    return true;
  }

  @Override
  public AttributeSet getUnsupportedAttributes(DocFlavor paramDocFlavor,
      AttributeSet paramAttributeSet) {
    if ((paramDocFlavor != null) && (!isDocFlavorSupported(paramDocFlavor))) {
      throw new IllegalArgumentException("flavor " + paramDocFlavor + "is not supported");
    }
    if (paramAttributeSet == null) {
      return null;
    }
    HashAttributeSet localHashAttributeSet = new HashAttributeSet();
    Attribute[] arrayOfAttribute = paramAttributeSet.toArray();
    for (int i = 0; i < arrayOfAttribute.length; i++) {
      try {
        Attribute localAttribute = arrayOfAttribute[i];
        if (!isAttributeCategorySupported(localAttribute.getCategory())) {
          localHashAttributeSet.add(localAttribute);
        } else if (!isAttributeValueSupported(localAttribute, paramDocFlavor, paramAttributeSet)) {
          localHashAttributeSet.add(localAttribute);
        }
      } catch (ClassCastException localClassCastException) {
      }
    }
    if (localHashAttributeSet.isEmpty()) {
      return null;
    }
    return localHashAttributeSet;
  }

  @Override
  public ServiceUIFactory getServiceUIFactory() {
    return new ServiceUIFactory() {

      @Override
      public String[] getUIClassNamesForRole(int role) {
        return null;
      }

      @Override
      public Object getUI(int role, String ui) {
        return null;
      }
    };
  }

}
