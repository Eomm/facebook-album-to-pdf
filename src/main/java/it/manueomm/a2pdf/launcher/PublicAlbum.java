package it.manueomm.a2pdf.launcher;

import it.manueomm.a2pdf.AlbumToPdf;
import it.manueomm.a2pdf.bean.RequestArgs;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.exception.FacebookGraphException;

/**
 * Copyright (C) 2016 Manuel Spigolon<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Manuel Spigolon
 *
 */
public class PublicAlbum {

   private static final Logger log = LoggerFactory.getLogger(PublicAlbum.class);

   /**
    * Main program for getting facebook's album to PDF
    * 
    * @param args
    *           [0] = appId
    * @param args
    *           [1] = appSecret
    * @param args
    *           [2] = working path (where save the images and PDF)
    * @param args
    *           [...] = the album's ids to generate the PDF
    */
   public static void main(String[] args) {
      log.info("Facebook Album to PDF..");

      try {
         RequestArgs request = checkArgs(args);

         AlbumToPdf converter = new AlbumToPdf(request.getAppId(), request.getAppSecret(), request.getWorkingPath());
         for (String album : request.getCopyAlbum()) {
            try {
               log.info("## Start converting album id:" + album);
               File pdf = new File(request.getWorkingPath(), "Album - " + album + ".pdf");
               converter.convertPublicAlbumToPdf(album, pdf);
               log.info("## Created PDF album: " + pdf.getAbsolutePath());            
            
            } catch (FacebookGraphException fex) {
               log.error("## Error reading facebook album id: " + album, fex);
            } catch (IOException ex) {
               log.error("## Error creating pdf", ex);
            }
         }

      } catch (Exception ex) {
         log.error("Error in main application", ex);
      }
   }

   public static RequestArgs checkArgs(String[] args) throws Exception {
      if (args == null) {
         // TODO print help /?
         throw new NullPointerException("args cant be empty");
      }

      if (args.length < 4) {
         // TODO print help /?
         throw new Exception("it cant be less than 4 parameter");
      }

      // check arguments
      RequestArgs request = new RequestArgs();
      for (int i = 0; i < args.length; i++) {
         if (i == 0)
            request.setAppId(args[i]);
         else if (i == 1)
            request.setAppSecret(args[i]);
         else if (i == 2){
            request.setWorkingPath(args[i]);
            
            File test = new File(request.getWorkingPath());
            if(!test.isDirectory()){
               throw new IOException("The parameter "+request.getWorkingPath()+" isn't a valid directory");
            }
            if(!test.exists()){
               test.mkdirs();
            }
            
         } else
            request.getCopyAlbum().add(args[i]);
      }

      return request;
   }

}
