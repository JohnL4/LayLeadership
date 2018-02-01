package church.universityumc.excelconverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Hello world!
 *
 */
public class App 
{
   private static void showHelp() {
      String help =
            "Reformat Excel file.\n\n"
            + "-h\thelp\n"
            + "-f\tinput file\n"     
            ;
      System.out.println( help);
   }
   
    public static void main( String[] args ) throws IOException, ParseException
    {
       CommandLine cmdLine = parseCommandLine( args);
       if (cmdLine.hasOption( 'h'))
          showHelp();
       String infileName = "test.xls";
       if (cmdLine.hasOption( 'f'))
          infileName = cmdLine.getOptionValue( 'f');
       dumpExcelFile( infileName);
    }
    
    
    private static CommandLine parseCommandLine( String[] args) throws ParseException
    {
       Options options = new Options();
       options.addOption( "h", "show help");
       options.addOption( "f", true, "input file");
       
       CommandLineParser parser = new DefaultParser();
       CommandLine cmdLine = parser.parse( options, args);
       return cmdLine;
    }
    
    private static void dumpExcelFile(String anInfileName) throws IOException {
       HSSFWorkbook workbook = readFile( anInfileName);
       for (Sheet sheet : workbook) // For all sheets in the workbook...
          for (Row row : sheet) // For all rows in the sheet...
             for (Cell cell : row) { // For all cells in the row...
                Font font = workbook.getFontAt( cell.getCellStyle().getFontIndex());
                List<String> formats = new ArrayList<String>();
                if (font.getBold())
                   formats.add( "bold");
                if (font.getItalic())
                   formats.add( "italic");
                String format;
                if (formats.isEmpty())
                   format = "";
                else
                   format = " (" + String.join( " ", formats) + ")";
                System.out.println( "got: " + cell.getStringCellValue() + " " + format /* + "\t" + font */ );
             }
    }
    
    
    
    private static HSSFWorkbook readFile(String filename) throws IOException {
       try (FileInputStream fis = new FileInputStream(filename)) {
               return new HSSFWorkbook(fis);        // NOSONAR - should not be closed here
       }
    }       
}
