package it.manueomm.facefile.client;

import it.manueomm.facefile.utils.TokenFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;

/**
 * Extension of DefaultFacebookClient adding logic for manage app access token
 * 
 * @author Manuel Spigolon
 *
 */
public class AppFacebookClient extends DefaultFacebookClient {

   private String appId;
   private String appSecret;

   private AccessToken appToken;

   /**
    * Build a token with the given appid an appsecret
    * 
    * @param appId
    * @param appSecret
    */
   public AppFacebookClient(String appId, String appSecret) {
      super(Version.VERSION_2_5);
      this.appId = appId;
      this.appSecret = appSecret;

      appToken = TokenFactory.getAppToken(appId, appSecret);
      this.accessToken = appToken.getAccessToken();
   }

   public String getAppId() {
      return appId;
   }

   public String getAppSecret() {
      return appSecret;
   }

   public AccessToken getAppToken() {
      return appToken;
   }

}