package church.universityumc.excelconverter.ui;

import java.io.IOException;

import church.universityumc.Log;
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
   private static Parent fxmlConfig;
   
   private static Stage configStage;
   
   private static Scene configScene;
   
   private static ConfigController self;

   private static FXMLLoader fxmlLoader;
   
   public ConfigController()
   {
      Log.debug( "new ConfigController");
   }
   
   public static void launchConfig() throws IOException
   {
      if (self == null)
      {
         fxmlLoader = new FXMLLoader( ConfigController.class.getResource( "ConfigUI.fxml"));
         fxmlConfig = fxmlLoader.load();
         configScene = new Scene( fxmlConfig);
         configScene.getStylesheets().add( ConfigController.class.getResource( "application.css").toExternalForm());
         configStage = new Stage();
         configStage.setTitle( CONFIG_TITLE);
         configStage.setScene( configScene);
         self = fxmlLoader.getController(); // Not a giant fan of this. TODO: get out of static code ASAP.  Maybe set this static member sooner and make properties non-static?
         
         configStage.show();

         //         self = new ConfigController(); // TODO: totally not the right thing to do.  FXMLLoader.load() creates another controller instance (the "real" one).
//         self.start( new Stage());
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
