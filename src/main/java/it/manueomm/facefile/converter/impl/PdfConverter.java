package it.manueomm.facefile.converter.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.manueomm.facefile.bean.AlbumWrapper;
import it.manueomm.facefile.converter.IAlbumConverter;
import it.manueomm.facefile.converter.pdf.PDFAlbumCreator;
import it.manueomm.facefile.converter.pdf.PageNumberEvent;
import it.manueomm.facefile.exceptions.ConvertException;

public class PdfConverter implements IAlbumConverter {

   private static final Logger log = LoggerFactory.getLogger(PdfConverter.class);

   private File outputFile;
   private boolean addPageNumber;

   public PdfConverter() {
      addPageNumber = true;
   }

   public PdfConverter(File outputFile) {
      this();
      this.outputFile = outputFile;
   }
   public PdfConverter(String outputFile) {
      this(new File(outputFile));
   }

   @Override
   public File build(AlbumWrapper album) throws ConvertException {
      try {
         if (outputFile == null) {
            if (album.getPhotos().size() > 0) {
               File firstImage = new File(album.getPhotos().get(0).getLocalFilePath());
               outputFile = new File(firstImage.getParent(), "Album " + album.getName() + ".pdf");
               log.warn("Auto setting outputFile: " + outputFile.getAbsolutePath());
            } else {
               throw new ConvertException("Cannot convert file: zero photos");
            }
         }

         PDFAlbumCreator creator = new PDFAlbumCreator();
         if (addPageNumber) {
            creator.setWriterEvent(new PageNumberEvent());
         }
         creator.buildPdf(album, outputFile);
         return outputFile;

      } catch (Exception ex) {
         log.error("Error converting album " + album, ex);
         throw new ConvertException(ex);
      }
   }

   public File getOutputFile() {
      return outputFile;
   }

   public void setOutputFile(File outputFile) {
      this.outputFile = outputFile;
   }

}
