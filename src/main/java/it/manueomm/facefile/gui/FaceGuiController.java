package it.manueomm.facefile.gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.exception.FacebookGraphException;

import it.manueomm.facefile.FaceAlbumReader;
import it.manueomm.facefile.exceptions.ConvertException;
import it.manueomm.facefile.exceptions.NotFoundException;
import it.manueomm.facefile.launcher.FaceFileFx;
import it.manueomm.facefile.utils.SettingFormat;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
   private ResourceBundle resources;

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
   }

   @FXML
   private void handleConvert() {
      btnConvert.setDisable(true);
      final String album;
      final FaceAlbumReader reader;

      try {
         album = Utils.findAlbumId(downloadLink.getText());

         // TODO
         reader = new FaceAlbumReader("1120777627954273", "ec0e22fe5fd212b18d81cbd3495a2a6c", UserPreferences.getSavePath());
         List<SettingFormat> formats = UserPreferences.getConvertFormats();
         for (SettingFormat settingFormat : formats) {
            reader.addConverter(settingFormat.getConverter());
         }

         userLog("msg.start", album);
         new Thread(new Runnable() {

            @Override
            public void run() {
               try {
                  List<File> done = null;
                  if (isPublic.isSelected()) {
                     done = reader.convertPublicAlbum(album);
                  } else {
                     // TODO private album not implement yet
                  }

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

}
