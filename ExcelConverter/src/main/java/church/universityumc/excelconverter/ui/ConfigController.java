package church.universityumc.excelconverter.ui;

import java.io.IOException;

import com.oracle.tools.packager.Log;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ConfigController
{
   private static final String CONFIG_TITLE = MainController.APPLICATION_TITLE + " Configuration";

   /**
    * The config screen.
    */
   private Parent fxmlConfig;
   
   private Stage configStage;
   
   private Scene configScene;
   
   private static ConfigController self;
   
   public ConfigController()
   {
      Log.debug( "new ConfigController");
   }
   
   public static void launchConfig() throws IOException
   {
      if (self == null)
      {
         self = new ConfigController(); // TODO: totally not the right thing to do.  FXMLLoader.load() creates another controller instance (the "real" one).
         self.start( new Stage());
      }
      else
         self.restart();
   }

   public void start( Stage aConfigStage) throws IOException
   {
      configStage = aConfigStage;
      fxmlConfig = FXMLLoader.load( getClass().getResource( "ConfigUI.fxml"));
      configScene = new Scene( fxmlConfig);
      configScene.getStylesheets().add( getClass().getResource( "application.css").toExternalForm());
      configStage.setTitle( CONFIG_TITLE);
      configStage.setScene( configScene);

      configStage.show();
   }
   
   protected void restart()
   {
      configStage.show();
   }
   
   @FXML protected void close( ActionEvent anEvent)
   {
      configStage.hide();
   }
}
