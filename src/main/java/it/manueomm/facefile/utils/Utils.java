package it.manueomm.facefile.utils;

import it.manueomm.facefile.exceptions.NotFoundException;

/**
 * Generic utils
 *
 * @author Manuel Spigolon
 *
 */
public class Utils {
   private Utils() {
   }

   /**
    *
    * @param url
    * @return
    * @throws NotFoundException
    */
   public static String findAlbumId(String url) throws NotFoundException {
      if (url == null || url.isEmpty()) {
         throw new NotFoundException("URL is empty");
      }

      String albumId = null;
		int from = url.indexOf("a."); // old format
      if (from > -1) {
         String[] split = url.split("=a.");
			if (split.length > 0) {
				int to = split[1].indexOf('.');
				albumId = split[1].substring(0, to);
			} else {
				albumId = url;
			}
      } else {
			from = url.indexOf("album_id="); // new format
			if (from > -1) {
				albumId = url.substring(from + 9, url.length());
			} else {
				// i suppose the user has entered an album id
				albumId = url;
			}
      }

      // TODO check if the id is only [a-z0-9]
      return albumId;
   }

}
