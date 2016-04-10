package it.manueomm.a2pdf.bean;

import java.util.ArrayList;
import java.util.List;

public class AlbumWrapper {

   private String name;
   private List<PhotoWrapper> photos;

   public AlbumWrapper(String name) {
      super();
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<PhotoWrapper> getPhotos() {
      if (photos == null) {
         photos = new ArrayList<PhotoWrapper>();
      }
      return photos;
   }

   public void setPhotos(List<PhotoWrapper> photos) {
      this.photos = photos;
   }
}
