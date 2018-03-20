package church.universityumc.excelconverter.ui;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import church.universityumc.Log;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController extends Application {

   @FXML private ListView<File> inputFilesListView;
   
   @FXML private TextField outputFile;
   
   @FXML private Button goBtn;

   private Set<File> inputFileSet = new HashSet<File>();
   
   private ObservableList<File> inputFiles = FXCollections.observableArrayList();
   
//   private File chosenOutputFile;
   // You don't wrap a normal object in an Observable, you just declare the Observable and assign to it via the .set() method.
   private ObjectProperty<File> chosenOutputFileProperty = new SimpleObjectProperty<File>();

   private Stage primaryStage;

   @Override
   public void init()
   {
//      System.out.printf( "inputFilenamesListView = %s%n", inputFilenamesListView);
//      // This is useless.  Somehow, the correct ListView gets instantiated later.
//      inputFilenamesListView = new ListView( inputFilenames);
      if (goBtn == null)
         Log.debug( "goBtn is null");
      else
         Log.debug( goBtn.toString());
   }
   
   /**
    * This method is magically called by javafx if it exists, when the scene/controller is completely constructed.  
    * The @FXML attribute enables us to not make it public if we don't want to. 
    * @see https://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html#controllers
    * @see https://docs.oracle.com/javase/9/docs/api/javafx/fxml/doc-files/introduction_to_fxml.html#controllers (i.e., part of the API javadocs)
    */
   @FXML protected void initialize()
   {
      if (goBtn == null)
         Log.debug( "goBtn is null");
      else
      {
         Log.debug( goBtn.toString());
         goBtn.disableProperty().bind( Bindings.isNull( chosenOutputFileProperty));
      }
   }
   
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
         primaryStage.setTitle( "Excel Converter");
         primaryStage.setScene( scene);

         if (goBtn == null)
            Log.debug( "goBtn is null");
         else
            Log.debug( goBtn.toString());
         
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
      
      if (chosenFiles == null)
         ;
      else
      {
         List<String> chosenFilenames = chosenFiles
               .stream()
               .map( f -> f.getName())
               .collect( Collectors.toList());
         Log.debug( String.format( "Chosen filenames: %s", String.join( " | ", chosenFilenames)));
         inputFileSet.addAll( chosenFiles);
         List<File> fs = inputFileSet
               .stream()
               .sorted( new Comparator<File>() { 
                  public int compare(File a, File b) { return a.getName().compareToIgnoreCase( b.getName()); } 
                  })
               .collect( Collectors.toList());
         inputFiles.setAll( fs);
         inputFilesListView.setItems( inputFiles);
      }
   }
   
   @FXML protected void handleOutputFileEvent( ActionEvent anEvent)
   {
      FileChooser chooser = new FileChooser();
      chooser.setTitle( "Pick Output File");
      chooser.getExtensionFilters().addAll(
            new ExtensionFilter( "Microsoft Excel Files", "*.xlsx"),
            new ExtensionFilter( "All Files", "*.*"));
      
      File chosenOutputFile = chooser.showSaveDialog( primaryStage);
      
      if (chosenOutputFile == null)
         ;
      else
      {
         chosenOutputFileProperty.set( chosenOutputFile);
         outputFile.setText( chosenOutputFile.toString());
      }
   }
   
   /**
    * Because we can't put tooltips on disabled controls and other solutions introduce various artifacts
    * (ButtonBar: weird spacing; ToolBar: funky borders; SplitPane: probably more border issues + a divider),
    * this is our fallback, to help the user understand why a certain button or checkbox or whathaveyou
    * is disabled.
    * @param anEvent
    */
   @FXML protected void handleExplainDisabledControls( ActionEvent anEvent)
   {
      // TODO: explain each disabled control here.
      Log.warn( "unimplemented");
   }

   @FXML protected void handleQuitEvent( ActionEvent anEvent)
   {
      Log.debug( "Quit event");
      if (goBtn == null)
         Log.debug( "goBtn is still null");
      else
         Log.debug( String.format( "Now goBtn is %s", goBtn));
      Platform.exit();
   }
}
