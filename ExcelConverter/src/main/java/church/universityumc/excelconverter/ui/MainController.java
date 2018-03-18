package church.universityumc.excelconverter.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController extends Application {

   @Override
   public void start( Stage primaryStage)
   {
      Parent fxmlRoot;
      try
      {
         fxmlRoot = FXMLLoader.load( getClass().getResource( "MainUI.fxml"));
         Scene scene = new Scene( fxmlRoot /* ,400,400 */);
         scene.getStylesheets().add( getClass().getResource( "application.css").toExternalForm());
         primaryStage.setScene( scene);
         primaryStage.show();
      }
      catch (IOException exc)
      {
         exc.printStackTrace();
      }
   }
   
   @FXML protected void handleInputFileEvent( ActionEvent anEvent)
   {
      
   }

   @FXML protected void handleQuitEvent( ActionEvent anEvent)
   {
      System.out.println( "Quit event");
      Platform.exit();
   }
}
