package church.universityumc.excelconverter.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import church.universityumc.Log;
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

//   @FXML private ListView<String> inputFilenamesListView; // TODO: should really be a list of Files, not Strings.
   @FXML private ListView<File> inputFilesListView;

//   private Set<String> inputFilenameSet = new HashSet<String>();
   private Set<File> inputFileSet = new HashSet<File>();
   
//   private ObservableList<String> inputFilenames = FXCollections.observableArrayList();
   private ObservableList<File> inputFiles = FXCollections.observableArrayList();
   
   private Stage primaryStage;

//   @Override
//   public void init()
//   {
//      System.out.printf( "inputFilenamesListView = %s%n", inputFilenamesListView);
//      // This is useless.  Somehow, the correct ListView gets instantiated later.
//      inputFilenamesListView = new ListView( inputFilenames);
//   }
   
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
//      System.out.printf( "inputFilenamesListView, items = %s, %s%n", inputFilenamesListView, inputFilenamesListView.getItems());
      FileChooser chooser = new FileChooser();
      chooser.setTitle( "Pick File(s) To Be Processed");
      chooser.getExtensionFilters().addAll(
            new ExtensionFilter( "Microsoft Excel Files", "*.xls", "*.xlsx"),
            new ExtensionFilter( "All Files", "*.*"));
      
      List<File> chosenFiles = chooser.showOpenMultipleDialog( primaryStage);
      
      if (chosenFiles == null)
         ;
      else
      {
         List<String> chosenFilenames = chosenFiles
               .stream()
               .map( f -> f.getName())
               .collect( Collectors.toList());
         Log.debug( String.format( "Chosen filenames: %s", String.join( " | ", chosenFilenames)));
//         inputFilenameSet.addAll( chosenFilenames);
         inputFileSet.addAll( chosenFiles);
//         List<String> fns = inputFilenameSet
//               .stream()
//               .sorted()
//               .collect( Collectors.toList());
         List<File> fs = inputFileSet
               .stream()
               .sorted( new Comparator<File>() { 
                  public int compare(File a, File b) { return a.getName().compareToIgnoreCase( b.getName()); } 
                  })
               .collect( Collectors.toList());
//         inputFilenames.setAll( fns);
         inputFiles.setAll( fs);
//         inputFilenamesListView.setItems( inputFilenames);
         inputFilesListView.setItems( inputFiles);
      }
//      System.out.printf( "inputFilenamesListView, items = %s, %s%n", inputFilenamesListView, inputFilenamesListView.getItems());
   }

   @FXML protected void handleQuitEvent( ActionEvent anEvent)
   {
      Log.debug( "Quit event");
      Platform.exit();
   }
}
