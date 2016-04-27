package it.manueomm.facefile.utils.pdf.event;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Credits to:<br>
 * http://developers.itextpdf.com/examples/itext-action-second-edition
 * /chapter-5#225-moviecountries1.java
 * 
 * @author edited by Manuel Spigolon
 *
 */
public class PageNumberEvent extends PdfPageEventHelper {
   private Font font;
   private PdfTemplate t;
   private Image total;

   private String footerText;

   public PageNumberEvent() {
      this("");
   }

   public PageNumberEvent(String footerText) {
      super();
      this.footerText = footerText;
   }

   @Override
   public void onOpenDocument(PdfWriter writer, Document document) {
      t = writer.getDirectContent().createTemplate(30, 16);
      try {
         total = Image.getInstance(t);
         total.setRole(PdfName.ARTIFACT);
         font = new Font();
      } catch (Exception e) {
         throw new ExceptionConverter(e);
      }
   }

   @Override
   public void onEndPage(PdfWriter writer, Document document) {
      try {
         float widthPage = document.getPageSize().getWidth();
         float borderLeft = 0f;

         widthPage -= (document.leftMargin() + document.rightMargin());
         borderLeft = (document.leftMargin() + document.rightMargin()) / 2;

         PdfPTable table = new PdfPTable(3);
         table.setWidths(new int[] { 24, 24, 2 });
         table.setTotalWidth(widthPage);
         table.getDefaultCell().setFixedHeight(20);
         table.getDefaultCell().setBorder(Rectangle.BOTTOM);

         // First colum: album name
         table.addCell(new Phrase(footerText, font));

         // Second column: page's number
         table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
         table.addCell(new Phrase(String.format("%d /", writer.getPageNumber()), font));

         // Third column: total pages
         PdfPCell cell = new PdfPCell(total);
         cell.setBorder(Rectangle.BOTTOM);
         table.addCell(cell);

         // Draw the table on the end of the page
         PdfContentByte canvas = writer.getDirectContent();
         canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
         table.writeSelectedRows(0, -1, borderLeft, 30, canvas);
         canvas.endMarkedContentSequence();

      } catch (DocumentException de) {
         throw new ExceptionConverter(de);
      }
   }

   @Override
   public void onCloseDocument(PdfWriter writer, Document document) {
      ColumnText.showTextAligned(t, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber()), font), 2, 2, 0);
   }
}