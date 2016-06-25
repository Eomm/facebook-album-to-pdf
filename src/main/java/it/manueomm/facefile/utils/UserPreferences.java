package it.manueomm.facefile.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import it.manueomm.facefile.launcher.FaceFileFx;

public class UserPreferences {

   public static final String KEY_FORMATS = "key.formats";
   public static final String KEY_PATH = "key.path";
   public static final String KEY_CLEAN = "key.clean";
   public static final String KEY_USER_TOKEN = "key.user-token";

   private static final String DEFAULT_FORMATS = "PDF";
   private static final String DEFAULT_PATH = "C:/";
   private static final String DEFAULT_CLEAN = "true";

   private UserPreferences() {
   }

   public static List<SettingFormat> getConvertFormats() {
      String format = getPreferece(KEY_FORMATS, DEFAULT_FORMATS);

      List<SettingFormat> ret = new ArrayList<SettingFormat>();
      if (!format.isEmpty()) {
         String[] formats = format.split(",");
         for (String f : formats) {
            ret.add(SettingFormat.get(f));
         }
      }
      return ret;
   }

   public static void setConvertFormats(List<SettingFormat> formats) {
      if (formats.size() > 0) {
         updatePreference(KEY_FORMATS, formats.toString().replaceAll("(^\\[|\\]$)", "").replace(", ", ","));
      } else {
         updatePreference(KEY_FORMATS, "");
      }
   }

   public static String getSavePath() {
      return getPreferece(KEY_PATH, DEFAULT_PATH);
   }

   public static void setSavePath(String savePath) {
      if (savePath != null && !savePath.isEmpty()) {
         updatePreference(KEY_PATH, savePath);
      }
   }

   public static String getUserToken() {
      return getPreferece(KEY_USER_TOKEN, null);
   }

	public static void setUserToken(String userToken) {
		if (userToken != null && !userToken.isEmpty()) {
			updatePreference(KEY_USER_TOKEN, userToken);
		} else {
			removePreference(KEY_USER_TOKEN);
		}
	}

   public static boolean isCleanOnComplete() {
      return new Boolean(getPreferece(KEY_CLEAN, DEFAULT_CLEAN));
   }

   public static void setCleanOnComplete(boolean isActive) {
      updatePreference(KEY_CLEAN, String.valueOf(isActive));
   }

   private static String getPreferece(String key, String defaultValue) {
      Preferences prefs = Preferences.userNodeForPackage(FaceFileFx.class);
      return prefs.get(key, defaultValue);
   }

   public static void updatePreference(String key, String value) {
      Preferences prefs = Preferences.userNodeForPackage(FaceFileFx.class);
      prefs.put(key, value);
   }
   
	public static void removePreference(String key) {
		Preferences prefs = Preferences.userNodeForPackage(FaceFileFx.class);
		prefs.remove(key);
	}

}
