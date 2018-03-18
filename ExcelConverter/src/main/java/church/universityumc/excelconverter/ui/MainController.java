package church.universityumc.excelconverter.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController extends Application {

   @FXML private ListView<String> inputFilenames;

   private Set<String> inputFilenameSet = new HashSet<String>();
   
   private Stage primaryStage;
   
   @Override
   public void start( Stage primaryStage)
   {
      this.primaryStage = primaryStage;
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
      FileChooser chooser = new FileChooser();
      chooser.setTitle( "Pick File(s) To Be Processed");
      chooser.getExtensionFilters().addAll(
            new ExtensionFilter( "Microsoft Excel Files", "*.xls", "*.xlsx"),
            new ExtensionFilter( "All Files", "*.*"));
      List<File> chosenFiles = chooser.showOpenMultipleDialog( primaryStage);
      List<String> chosenFilenames = chosenFiles.stream().map( f -> f.getName()).collect( Collectors.toList());
      System.out.printf( "Chosen files: %s%n", String.join( " | ", chosenFilenames));
      inputFilenameSet.addAll( chosenFilenames);
      List<String> fns = inputFilenameSet.stream().collect( Collectors.toList());
      inputFilenames.setItems( FXCollections.observableList( fns));
   }

   @FXML protected void handleQuitEvent( ActionEvent anEvent)
   {
      System.out.println( "Quit event");
      Platform.exit();
   }
}
