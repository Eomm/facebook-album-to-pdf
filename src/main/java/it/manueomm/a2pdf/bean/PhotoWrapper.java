package it.manueomm.a2pdf.bean;

import com.restfb.types.Photo;

public class PhotoWrapper {

   private Photo photo;
   private String localFilePath;

   public PhotoWrapper(Photo photo) {
      super();
      this.photo = photo;
   }

   public PhotoWrapper(Photo photo, String localFilePath) {
      super();
      this.photo = photo;
      this.localFilePath = localFilePath;
   }

   public Photo getPhoto() {
      return photo;
   }

   public void setPhoto(Photo photo) {
      this.photo = photo;
   }

   public String getLocalFilePath() {
      return localFilePath;
   }

   public void setLocalFilePath(String localFilePath) {
      this.localFilePath = localFilePath;
   }

}
