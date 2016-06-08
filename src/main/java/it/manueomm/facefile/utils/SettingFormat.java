package it.manueomm.facefile.utils;

import it.manueomm.facefile.converter.IAlbumConverter;
import it.manueomm.facefile.converter.impl.PdfConverter;

public enum SettingFormat {

   PDF(new PdfConverter()), JPG(null), HTML(null);

   private final IAlbumConverter converter;

   private SettingFormat(IAlbumConverter converter) {
      this.converter = converter;
   }

   public boolean isActive() {
      return converter != null;
   }

   public IAlbumConverter getConverter() {
      return converter;
   }


   public static SettingFormat get(String f) {
      for (SettingFormat sf : values()) {
         if (sf.name().equals(f))
            return sf;
      }
      return null;
   }

}
