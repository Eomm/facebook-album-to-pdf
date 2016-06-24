package it.manueomm.facefile.utils;

import java.util.HashMap;
import java.util.Map;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.UserDataPermissions;

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

   public static String getUserAuthLinkForToken(final String appId, final String redirect, UserDataPermissions... permissions) throws FacebookOAuthException {
      DefaultFacebookClient client = new DefaultFacebookClient(Version.VERSION_2_5);

      ScopeBuilder scope = new ScopeBuilder();
      for (UserDataPermissions userDataPermissions : permissions) {
         scope.addPermission(userDataPermissions);
      }
      return client.getLoginDialogUrl(appId, redirect, scope);
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
