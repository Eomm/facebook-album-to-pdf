package it.manueomm.facefile.client;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;

import it.manueomm.facefile.utils.TokenFactory;

/**
 * Extension of DefaultFacebookClient adding logic for manage app access token
 *
 * @author Manuel Spigolon
 *
 */
public class AppFacebookClient extends DefaultFacebookClient {

   private String appId;
   private String appSecret;

   private AccessToken token;

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

      token = TokenFactory.getAppToken(appId, appSecret);
      this.accessToken = token.getAccessToken();
   }

   public AppFacebookClient(String userToken) {
      super(userToken, Version.VERSION_2_5);

      token = AccessToken.fromQueryString("access_token=" + userToken);
      this.accessToken = token.getAccessToken();
   }

   public String getAppId() {
      return appId;
   }

   public String getAppSecret() {
      return appSecret;
   }

   public AccessToken getToken() {
      return token;
   }

}