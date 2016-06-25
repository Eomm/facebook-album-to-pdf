package it.manueomm.facefile.launcher;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.manueomm.facefile.gui.FaceGuiController;
import it.manueomm.facefile.gui.SettingsGuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FaceFileFx extends Application {

   private static final Logger log = LoggerFactory.getLogger(FaceFileFx.class);

   private Stage primaryStage;
   private Stage secondaryStage;

   private ResourceBundle commonBundle;

   public static void main(String[] args) {
      launch(args);
   }

	@Override
	public void start(Stage primaryStage) {
      this.primaryStage = primaryStage;
      try {
         // Load root layout from fxml file.
         commonBundle = ResourceBundle.getBundle("bundles.faceFileGui", Locale.getDefault());
			ResourceBundle appSettings = PropertyResourceBundle.getBundle("config.settings");

         FXMLLoader loader = new FXMLLoader();
         loader.setResources(commonBundle);
         loader.setLocation(FaceFileFx.class.getResource("/it/manueomm/facefile/gui/FaceFileGui.fxml"));
         BorderPane rootLayout = (BorderPane) loader.load();

         FaceGuiController controller = loader.getController();
			controller.setAppId(appSettings.getString("appid"));
			controller.setAppSecr(appSettings.getString("secretid"));
			controller.setCallbackUrl(appSettings.getString("callbackUrl"));
         controller.setMainApp(this);

         this.primaryStage.setTitle(commonBundle.getString("app.name"));
			this.primaryStage.getIcons().add(new Image("/images/icon.png"));
         this.primaryStage.setScene(new Scene(rootLayout));
         this.primaryStage.show();

      } catch (Exception e) {
         log.error("Error loading the gui", e);
         System.exit(-1);
      }
   }

   public void loadSettingGui() {
      try {
         FXMLLoader loader = new FXMLLoader();
         loader.setResources(commonBundle);
         loader.setLocation(FaceFileFx.class.getResource("/it/manueomm/facefile/gui/SettingsGui.fxml"));
         Pane page = (Pane) loader.load();

         SettingsGuiController controller = loader.getController();
         controller.setMainApp(this);

         secondaryStage = new Stage();
         secondaryStage.setTitle(commonBundle.getString("menu.configuration"));
         secondaryStage.initModality(Modality.WINDOW_MODAL);
         secondaryStage.initOwner(primaryStage);
         secondaryStage.setScene(new Scene(page));
         secondaryStage.show();

      } catch (Exception e) {
         log.error("Error loading the settings gui", e);
         System.exit(-1);
      }
   }

   public Stage getPrimaryStage() {
      return primaryStage;
   }

   public Stage getSecondaryStage() {
      return secondaryStage;
   }

   public ResourceBundle getCommonBundle() {
      return commonBundle;
   }

}
