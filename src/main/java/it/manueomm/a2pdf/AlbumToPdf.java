package it.manueomm.a2pdf;

import it.manueomm.a2pdf.bean.AlbumWrapper;
import it.manueomm.a2pdf.bean.PhotoWrapper;
import it.manueomm.a2pdf.client.AppFacebookClient;
import it.manueomm.a2pdf.utils.Quality;
import it.manueomm.a2pdf.utils.pdf.PDFAlbumCreator;
import it.manueomm.a2pdf.utils.pdf.event.PageNumberEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookGraphException;
import com.restfb.types.Photo;
import com.restfb.types.Photo.Image;

/**
 * This class give you the possibily to create a PDF file of any album of
 * facebook
 * 
 * @author Manuel Spigolon
 *
 */
public class AlbumToPdf {

   private static final Logger log = LoggerFactory.getLogger(AlbumToPdf.class);

   private String appId;
   private String appSecret;

   private String workingPath;

   private FacebookClient facebookClient;

   private Quality imageQuality;


   public AlbumToPdf(String appId, String appSecret) {
      super();
      this.appId = appId;
      this.appSecret = appSecret;
      this.imageQuality = Quality.HIGH;
   }

   public AlbumToPdf(String appId, String appSecret, String workingPath) {
      this(appId, appSecret);
      this.workingPath = workingPath;
   }

   /**
    * Build a PDF file with all the images of a pulic facebook album's
    * 
    * @param albumId
    *           the pulic albumId to save
    * @param pdfFile
    *           where save the pdf
    * @throws FacebookGraphException
    *            if there are some problems with communication
    * @throws IOException
    *            if there are some problems generating the pdf
    */
   public void convertPublicAlbumToPdf(final String albumId, final File pdfFile) throws FacebookGraphException, IOException {
      AlbumWrapper album = this.readPublicAlbum(albumId);

      PDFAlbumCreator creator = new PDFAlbumCreator();
      creator.setWriterEvent(new PageNumberEvent());

      try {
         creator.buildPdf(album, pdfFile);
      } catch (Exception ex) {
         log.error("Error building album", ex);
         throw new IOException(ex);
      }
   }

   /**
    * Read the information "fields", "images,name" of the albumId
    * 
    * @param albumId
    * @return an album wrapper with list of all his photos from pagination
    * @throws FacebookGraphException
    */
   public AlbumWrapper readPublicAlbum(String albumId) throws FacebookGraphException {
      log.debug("Reading album.. " + albumId);

      // build a directory where store all the images
      File albumDirectory = new File(workingPath, albumId);
      albumDirectory.mkdir();
      final String albumPath = albumDirectory.getAbsolutePath();

      final AlbumWrapper albumReaded = new AlbumWrapper(albumId);
      final long start = System.currentTimeMillis();

      // init the client
      facebookClient = getFacebookClient();
      Connection<Photo> photos;
      String next = null;
      int paginazione = 0;
      do {

         if (next != null) {
            photos = facebookClient.fetchConnectionPage(next, Photo.class);

         } else {
            // Es:
            // https://developers.facebook.com/tools/explorer/145634995501895/?method=GET&path=187796351255392%2Fphotos%3Ffields%3Dimages&version=v2.5
            photos = facebookClient.fetchConnection( //
                  albumId + "/photos", //
                  Photo.class, //
                  Parameter.with("fields", "images,name"));
         }

         List<Photo> photosList = photos.getData();

         // TODO eventually sort code on photos

         for (Photo photo : photosList) {
            String largeUrl = chooseImage(photo.getImages()).getSource();
            File fileImage = new File(albumPath, photo.getName() + ".jpg");

            PhotoWrapper ph = new PhotoWrapper(photo, fileImage.getAbsolutePath());
            albumReaded.getPhotos().add(ph);

            try {
               // TODO set the timeout
               FileUtils.copyURLToFile(new URL(largeUrl), fileImage);
               log.debug("Saved image " + fileImage.getAbsolutePath());

            } catch (Exception ex) {
               log.error("Error saving image: " + photo.getId(), ex);
               // TODO continue?
            }
         }
         next = photos.getNextPageUrl();
         log.debug("Read page: " + ++paginazione);

      } while (photos.hasNext());

      log.info("Saved images in ms: " + (System.currentTimeMillis() - start));
      return albumReaded;
   }

   /**
    * Choose the image from the list by the image quality selected by the user
    * 
    * @param images
    * @return
    */
   private Image chooseImage(List<Image> images) {
      Image maxImage = null;
      Image minImage = null;
      int max = -1;
      int min = Integer.MAX_VALUE;

      // found the image with max and min res
      for (Image image : images) {
         if (max < image.getWidth().intValue()) {
            max = image.getWidth().intValue();
            maxImage = image;
         }
         if (min > image.getWidth().intValue()) {
            min = image.getWidth().intValue();
            minImage = image;
         }
      }

      switch (imageQuality) {
      case HIGH:
         return maxImage;

      case LOW:
         return minImage;

      case MEDIUM:
         // search the medium image
         Image avgImage = null;
         int averageMinMax = (min + max) / 2;
         int delta = Integer.MAX_VALUE;
         for (Image image : images) {
            int deltaNow = Math.abs(averageMinMax - image.getWidth().intValue());
            if (deltaNow < delta) {
               delta = deltaNow;
               avgImage = image;
            }
         }
         return avgImage;

      default:
         // this will not happen
         return images.get(0);
      }

   }

   /**
    * Create an instance of the facebook client at the version 2.5. If an
    * instance is already set, a new instance isn't created
    * 
    * @return a DefaultFacebookClient instance with the an app token build with
    *         constructor's appId and appSecret
    */
   private FacebookClient getFacebookClient() {
      if (facebookClient == null) {
         facebookClient = new AppFacebookClient(appId, appSecret);
      }
      return facebookClient;
   }

   public Quality getImageQuality() {
      return imageQuality;
   }

   public void setImageQuality(Quality imageQuality) {
      this.imageQuality = imageQuality;
   }

   public String getWorkingPath() {
      return workingPath;
   }

   public void setWorkingPath(String workingPath) {
      this.workingPath = workingPath;
   }

   public String getAppId() {
      return appId;
   }

   public String getAppSecret() {
      return appSecret;
   }

}
