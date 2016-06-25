package it.manueomm.facefile.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.manueomm.facefile.launcher.FaceFileFx;
import it.manueomm.facefile.utils.SettingFormat;
import it.manueomm.facefile.utils.UserPreferences;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class SettingsGuiController {

   private static final Logger log = LoggerFactory.getLogger(SettingsGuiController.class);

   private FaceFileFx mainApp;

   @FXML
   private TextField savePath;

   @FXML
   private CheckBox checkPdf;
   @FXML
   private CheckBox checkJpg;
   @FXML
   private CheckBox checkHtml;

   @FXML
   private CheckBox checkClean;

   @FXML
   private ResourceBundle resources;

   public SettingsGuiController() {
   }

   /**
    * Initializes the controller class
    */
   @FXML
   private void initialize() {
      savePath.setText(UserPreferences.getSavePath());
      checkClean.setSelected(UserPreferences.isCleanOnComplete());

      for (SettingFormat sf : SettingFormat.values()) {
         switch (sf) {
         case HTML:
            checkHtml.setDisable(!sf.isActive());
            break;
         case JPG:
            checkJpg.setDisable(!sf.isActive());
            break;
         case PDF:
            checkPdf.setDisable(!sf.isActive());
            break;
         }
      }
      List<SettingFormat> formats = UserPreferences.getConvertFormats();
      for (SettingFormat sf : formats) {
         switch (sf) {
         case HTML:
            checkHtml.setSelected(true);
            break;
         case JPG:
            checkJpg.setSelected(true);
            break;
         case PDF:
            checkPdf.setSelected(true);
            break;
         }
      }
   }

   @FXML
   private void handleDirectoryChoose() {
      try {
         File defaultDirectory = new File(savePath.getText());

         DirectoryChooser chooser = new DirectoryChooser();
         chooser.setTitle(resources.getString("config.btn.choose.title"));

         if (defaultDirectory.exists() && defaultDirectory.isDirectory()) {
            chooser.setInitialDirectory(defaultDirectory);
         }

         File selectedDirectory = chooser.showDialog(mainApp.getPrimaryStage());
         this.savePath.setText(selectedDirectory.getAbsolutePath());

      } catch (Exception ex) {
         log.error("Error choosing directory", ex);
      }
   }

   @FXML
   private void handleSave() {
      log.debug("Saving...");

      List<SettingFormat> active = new ArrayList<SettingFormat>(3);
      if (checkPdf.isSelected()) {
         active.add(SettingFormat.PDF);
      }
      if (checkJpg.isSelected()) {
         active.add(SettingFormat.JPG);
      }
      if (checkHtml.isSelected()) {
         active.add(SettingFormat.HTML);
      }

      UserPreferences.setConvertFormats(active); // TODO almost one selected
      UserPreferences.setSavePath(savePath.getText());
      UserPreferences.setCleanOnComplete(checkClean.isSelected());

      this.mainApp.getSecondaryStage().close();
      log.debug("Saved");
   }

   @FXML
   private void handleClose() {
      this.mainApp.getSecondaryStage().close();
   }

   public FaceFileFx getMainApp() {
      return mainApp;
   }

   public void setMainApp(FaceFileFx mainApp) {
      this.mainApp = mainApp;
   }

}
