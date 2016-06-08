package it.manueomm.facefile.launcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

public class TestUserLogin {

   public static void main(String[] args) throws Exception{
      // TODO Auto-generated method stub
      
      
      // ACCESSO TOKEN
      String tok = "CAAP7V2Bi7GEBADCPTHA5SzVmyR4KYyis1tl5V8rvvXgXbLQXBLmr1h1PhBu3ufZBG1eWFN2lQdCBxynSBjZCxZAh8ChYVSxxMj7iks6TcsnNT0Gg47hv7I76CVoKYpv7n5aczYpkqxjXpcxG8lZBPOHkZCBj4V0hPue7ZBOgO5T4YJ2QPyYsmXuWi9pZCswppAWzZCm06DpuCQZDZD";

      String clientId = "1120777627954273";
      String secret = "ec0e22fe5fd212b18d81cbd3495a2a6c";
      try {
         String t = tok;
         OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
         if (tok == null) {

         OAuthClientRequest request = OAuthClientRequest//
               .authorizationLocation("https://graph.facebook.com/oauth/authorize")//
               .setClientId(clientId)//
               .setRedirectURI("http://miorepository.altervista.org/albumtopdf/")//
               .buildQueryMessage();
         // in web application you make redirection to uri:
         System.out.println("Visit: " + request.getLocationUri() + "\nand grant permission");
         System.out.print("Now enter the OAuth code you have received in redirect uri ");
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         String code = br.readLine();
         request = OAuthClientRequest.tokenLocation("https://graph.facebook.com/oauth/access_token").setGrantType(GrantType.AUTHORIZATION_CODE)
               .setClientId(clientId).setClientSecret(secret)//
               .setRedirectURI("http://miorepository.altervista.org/albumtopdf/").setCode(code)
               .buildBodyMessage();

         // Facebook is not fully compatible with OAuth 2.0 draft 10, access
         // token response is
         // application/x-www-form-urlencoded, not json encoded so we use
         // dedicated response class for that
         // Own response class is an easy way to deal with oauth providers that
         // introduce modifications to
         // OAuth specification
         GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
            t = oAuthResponse.getAccessToken();
            System.out.println("Access Token: " + t + ", Expires in: " + oAuthResponse.getExpiresIn());
         }
         OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://graph.facebook.com/me").setAccessToken(t).buildQueryMessage();

         OAuthResourceResponse r = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
         System.out.println(r.getBody());

      } catch (OAuthProblemException e) {
         System.out.println("OAuth error: " + e.getError());
         System.out.println("OAuth error description: " + e.getDescription());
      }

      //
      // // "" ""
      // String state = UUID.randomUUID().toString();
      // OAuthClientRequest request = OAuthClientRequest//
      // .tokenProvider(OAuthProviderType.FACEBOOK)//
      // .setGrantType(GrantType.AUTHORIZATION_CODE)//
      // .setClientId("1120777627954273")//
      // // .setClientSecret("ec0e22fe5fd212b18d81cbd3495a2a6c")//
      // .setRedirectURI("http://www.example.com/redirect")//
      // .setParameter(OAuth.OAUTH_STATE, state).setCode("asd")//
      // .buildQueryMessage();
      //
      // // OAuthAuthzResponse oar =
      // // OAuthAuthzResponse.oauthCodeAuthzResponse(request);
      //
      // OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      // GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request,
      // GitHubTokenResponse.class);
      // // 
      // String a = request.getLocationUri();
      // String accessToken = oAuthResponse.getAccessToken();
      // Long expiresIn = oAuthResponse.getExpiresIn();
      //
      // System.out.println(a + " // " + accessToken + " == " + expiresIn);

   }
}
