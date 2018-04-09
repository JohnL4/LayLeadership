package church.universityumc.excelconverter.ui;

import java.io.IOException;

import church.universityumc.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfigController
{
   private static final String CONFIG_TITLE = MainController.APPLICATION_TITLE + " Configuration";

   @FXML private TreeView<String> configTreeView;
   
   /**
    * The config screen.
    */
   private static Parent fxmlConfig;
   
   private Stage parentStage;
   
   private Stage configStage;
   
   private Scene configScene;
   
   private static ConfigController self;

   private static FXMLLoader fxmlLoader;
   
   public ConfigController()
   {
      Log.debug( "new ConfigController");
   }
   
   public static void launchConfig(Stage aParentStage) throws IOException
   {
      if (self == null)
      {
         fxmlLoader = new FXMLLoader( ConfigController.class.getResource( "ConfigUI.fxml"));
         fxmlConfig = fxmlLoader.load();
         self = fxmlLoader.getController();

         self.parentStage = aParentStage;

         self.configScene = new Scene( fxmlConfig);
         self.configScene.getStylesheets().add( ConfigController.class.getResource( "application.css").toExternalForm());

         self.configStage = new Stage();
         self.configStage.initOwner( aParentStage);
         self.configStage.initStyle( StageStyle.UTILITY);
         self.configStage.initModality( Modality.APPLICATION_MODAL);
         self.configStage.setTitle( CONFIG_TITLE);
         self.configStage.setScene( self.configScene);
         
         self.setDumbActivities();
         
         self.configStage.show();
      }
      else
         self.restart();
   }

   private void setDumbActivities()
   {
      if (configTreeView == null)
         Log.warn( "configTreeView is null");
      else
      {
         TreeItem<String> ti = new TreeItem<String>( "Activitites");
         ti.setExpanded( true);
         TreeItem<String> cc = new TreeItem<String>( "Church Council");
         cc.setExpanded( true);
         TreeItem<String> sd = new TreeItem<String>( "Start date");
         
         configTreeView.setRoot( ti);
         ti.getChildren().setAll( cc);
         cc.getChildren().setAll( sd);
      }
   }

   protected void restart()
   {
      configStage.show();
   }
   
   @FXML protected void close( ActionEvent anEvent)
   {
      configStage.hide();
   }
   
   @FXML protected void loadConfig( ActionEvent anEvent)
   {
      Log.warn( "loadConfig not implemented");
   }
   
   @FXML protected void saveConfig( ActionEvent anEvent)
   {
      Log.warn( "saveConfig not implemented");
   }
   
   @FXML protected void insertNewActivity( ActionEvent anEvent)
   {
      Log.warn( "unimplemented");
   }
   
   @FXML protected void deleteSelectedItem( ActionEvent anEvent)
   {
      Log.warn( "unimplemented");
   }
   
   @FXML protected void useConfig( ActionEvent anEvent)
   {
      Log.warn( "useConfig not implemented");
   }
}
