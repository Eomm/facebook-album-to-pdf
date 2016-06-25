package it.manueomm.facefile.converter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.FileAlreadyExistsException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import it.manueomm.facefile.bean.AlbumWrapper;
import it.manueomm.facefile.bean.PhotoWrapper;

/**
 * Utility for build a pdf document by an AlbumWrapper
 *
 * @author Manuel Spigolon
 *
 */
public class PDFAlbumCreator {

   private Document templateDocument;
   private PdfPageEvent writerEvent;

   public PDFAlbumCreator() {
      this(new Document());
   }

   /**
    *
    * @param templateDocument
    *           the custom template document
    */
   public PDFAlbumCreator(Document templateDocument) {
      super();
      this.templateDocument = templateDocument;
   }

   /**
    * Create a pdf document with all the image in the album.
    *
    * @param album
    * @param pdfOutput
    * @throws Exception
    *            if the pdf file already exist, or error generating the pdf
    */
   public void buildPdf(final AlbumWrapper album, final File pdfOutput) throws Exception {

      if (pdfOutput.exists()) {
         throw new FileAlreadyExistsException("The album " + pdfOutput.getAbsolutePath() + " already exists");
      }

      float pageWidth = templateDocument.getPageSize().getWidth() - templateDocument.leftMargin() - templateDocument.rightMargin();

      PdfWriter writer = PdfWriter.getInstance(templateDocument, new FileOutputStream(pdfOutput));
      if (writerEvent != null) {
         writer.setPageEvent(writerEvent);
      }

      templateDocument.open();

      for (PhotoWrapper photo : album.getPhotos()) {
         // XXX useit when the image is too large
         int reduction = 0;

         Image img = Image.getInstance(photo.getLocalFilePath());
         img.setAlignment(Image.MIDDLE);

         float scaler = ((pageWidth - reduction) / img.getWidth()) * 100;
         img.scalePercent(scaler);

         templateDocument.add(img);
      }

      templateDocument.close();

   }

   public Document getTemplateDocument() {
      return templateDocument;
   }

   public void setTemplateDocument(Document templateDocument) {
      this.templateDocument = templateDocument;
   }

   public PdfPageEvent getWriterEvent() {
      return writerEvent;
   }

   public void setWriterEvent(PdfPageEvent writerEvent) {
      this.writerEvent = writerEvent;
   }

}
