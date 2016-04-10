package it.manueomm.a2pdf.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper of args
 * 
 * @author Manuel Spigolon
 *
 */
public class RequestArgs {
   private String appId;
   private String appSecret;
   private String workingPath;
   private List<String> copyAlbum;

   public String getAppId() {
      return appId;
   }

   public void setAppId(String appId) {
      this.appId = appId;
   }

   public String getAppSecret() {
      return appSecret;
   }

   public void setAppSecret(String appSecret) {
      this.appSecret = appSecret;
   }

   public String getWorkingPath() {
      return workingPath;
   }

   public void setWorkingPath(String workingPath) {
      this.workingPath = workingPath;
   }

   public List<String> getCopyAlbum() {
      if (copyAlbum == null) {
         copyAlbum = new ArrayList<String>();
      }
      return copyAlbum;
   }
}
