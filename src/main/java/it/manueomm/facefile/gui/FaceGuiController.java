package it.manueomm.facefile.gui;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.exception.FacebookGraphException;
import com.restfb.scope.UserDataPermissions;

import it.manueomm.facefile.FaceAlbumReader;
import it.manueomm.facefile.converter.INotifier;
import it.manueomm.facefile.exceptions.ConvertException;
import it.manueomm.facefile.exceptions.NotFoundException;
import it.manueomm.facefile.launcher.FaceFileFx;
import it.manueomm.facefile.utils.SettingFormat;
import it.manueomm.facefile.utils.TokenFactory;
import it.manueomm.facefile.utils.UserPreferences;
import it.manueomm.facefile.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class FaceGuiController {

   private static final Logger log = LoggerFactory.getLogger(FaceGuiController.class);

   private FaceFileFx mainApp;

   @FXML
   private TextField downloadLink;

   @FXML
   private CheckBox isPublic;

   @FXML
   private Button btnConvert;

   @FXML
   private TextArea viewLogs;
   private SimpleDateFormat logFormat;

   @FXML
   private MenuItem logout;
   
   @FXML
   private ResourceBundle resources;

   private String facebookToken;

	private String appId;
	private String appSecr;
	private String callbackUrl;

   public FaceGuiController() {
   }

   /**
    * Initializes the controller class
    */
   @FXML
   private void initialize() {
      logFormat = new SimpleDateFormat(resources.getString("dt.format"));

      // Autoscroll to bottom on update text
      viewLogs.textProperty().addListener(new ChangeListener<Object>() {
         @Override
         public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            viewLogs.setScrollTop(Double.MAX_VALUE);
         }
      });

      // Autoselect text on focus
      downloadLink.focusedProperty().addListener(new ChangeListener<Object>() {
         @Override
         public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  if (downloadLink.isFocused() && !downloadLink.getText().isEmpty()) {
                     downloadLink.selectAll();
                  }
               }
            });
         }
      });

		facebookToken = UserPreferences.getUserToken();
		if (facebookToken == null) {
			logout.setDisable(true);
		}
   }

   @FXML
   private void handleConvert() {
      btnConvert.setDisable(true);
      final String album;
      final FaceAlbumReader reader;

      try {
         album = Utils.findAlbumId(downloadLink.getText());

         if (!isPublic.isSelected() && facebookToken == null) {
            // Check if there is a saved token
            facebookToken = UserPreferences.getUserToken();

				// TODO check if is still a valid token!!

            if (facebookToken == null) {
               // I need a token from the user
               TextInputDialog dialog = new TextInputDialog();
               dialog.setTitle(resources.getString("token.title"));
               dialog.setHeaderText(resources.getString("token.header"));
               dialog.setContentText(resources.getString("token.text"));
               final String accessUrl = TokenFactory.getUserAuthLinkForToken(appId, callbackUrl, UserDataPermissions.USER_PHOTOS);

               try {
                  Desktop.getDesktop().browse(new URI(accessUrl));
               } catch (Exception e) {
                  log.error("Error opening the browser!", e);
                  userLogError("Browser", accessUrl);
               }

               Optional<String> result = dialog.showAndWait();
               if (result.isPresent()) {
                  facebookToken = result.get();
               } else {
                  return;
               }
            }
         }

			if (facebookToken != null && !isPublic.isSelected()) {
            reader = new FaceAlbumReader(facebookToken, UserPreferences.getSavePath());
            UserPreferences.setUserToken(facebookToken);
				logout.setDisable(false);

         } else {
            reader = new FaceAlbumReader(appId, appSecr, UserPreferences.getSavePath());
         }

         List<SettingFormat> formats = UserPreferences.getConvertFormats();
         for (SettingFormat settingFormat : formats) {
            reader.addConverter(settingFormat.getConverter());
         }
         
			reader.setNotifier(new INotifier() {
				@Override
				public void update(Type notifyType, String message) {
					if (notifyType == Type.INFO) {
						userLog("msg.update", message);
					} else {
						userLogError("Exception", album, message);
					}
				}
			});

         new Thread(new Runnable() {
            @Override
            public void run() {
               userLog("msg.start", album);
               try {
                  List<File> done = reader.convertAlbum(album);

                  for (File outFile : done) {
                     userLog("msg.created", outFile.getAbsolutePath());
                  }
                  userLog("msg.end", album);

               } catch (FacebookGraphException ex) {
                  log.error("## Error converting album", ex);
                  userLogError("FacebookGraphException", album, ex.getMessage());

               } catch (ConvertException ex) {
                  log.error("## Error converting album", ex);
                  userLogError("ConvertException", album, ex.getMessage());

               } catch (Exception ex) {
                  log.error("## Generic Error converting album", ex);
                  userLogError("Exception", album, ex.getMessage());
               }
               btnConvert.setDisable(false);
            }
         }).start();

      } catch (NotFoundException ex) {
         userLogError(ex.getClass().getSimpleName());
         btnConvert.setDisable(false);
      }
   }
   
   @FXML
   private void handleLogout() {
	   UserPreferences.setUserToken(null);
		logout.setDisable(true);
   }

   @FXML
   private void handleSettings() {
      mainApp.loadSettingGui();
   }

   @FXML
   private void handleAbout() {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle(resources.getString("menu.about"));
      alert.setHeaderText(resources.getString("about.header"));
      alert.setContentText(resources.getString("about.credits"));
      alert.showAndWait();
   }

   @FXML
   private void handleExit() {
      System.exit(0);
   }

   /**
    * Log with internationalizations.
    *
    * @param key
    * @param string
    */
   private void userLog(String key, String... string) {
      userLog(String.format(resources.getString(key), string));
   }

   private void userLogError(String key, String... string) {
      userLog(String.format(resources.getString("ex." + key), string));
   }

   private void userLog(String message) {
      viewLogs.appendText(logFormat.format(new Date()) + ") " + message + '\n');
   }

   public FaceFileFx getMainApp() {
      return mainApp;
   }

   public void setMainApp(FaceFileFx mainApp) {
      this.mainApp = mainApp;
   }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecr() {
		return appSecr;
	}

	public void setAppSecr(String appSecr) {
		this.appSecr = appSecr;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

}
