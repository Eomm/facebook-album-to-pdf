package it.manueomm.a2pdf.utils;

import java.util.HashMap;
import java.util.Map;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;

/**
 * Retrieve and Store facebook token for calls the Facebook API
 * 
 * @author Manuel Spigolon
 *
 */
public class TokenFactory {

   private static Map<String, AccessToken> tokenStore;

   static {
      tokenStore = new HashMap<String, AccessToken>();
   }

   private TokenFactory() {
      // static class
   }

   /**
    * Instantiate an app token if needed
    * 
    * @param appId
    * @param appSecret
    * @return an app token
    * @throws FacebookOAuthException
    *            if the token isn't generated
    */
   public static AccessToken getAppToken(final String appId, final String appSecret) throws FacebookOAuthException {
      synchronized (tokenStore) {
         AccessToken token = tokenStore.get(appId);
         if (token == null) {
            token = new DefaultFacebookClient(Version.LATEST).obtainAppAccessToken(appId, appSecret);
            tokenStore.put(appId, token);
         }
         return token;
      }
   }
   
   public static AccessToken getUserToken() throws FacebookOAuthException{
      // TODO: not implement yet
      return null;
   }
   
   /**
    * Remove from the caching a token
    * @param appId
    * @return true if the token is removed
    */
   public static boolean tokenExpired(final String appId) {
      synchronized (tokenStore) {
         return tokenStore.remove(appId) != null;
      }
   }

}
