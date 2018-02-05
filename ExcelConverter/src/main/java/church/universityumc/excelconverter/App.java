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
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Hello world!
 *
 */
public class App
{
   private static Options options;
   
   private static Options makeOptions()
   {
      Options options = new Options();
      options.addOption( "h", "help", false, "show help");
      options.addOption( Option.builder( "f") // "f", true, "input file");
            .desc( "input .xls or .xlsx file")
            .hasArg()
            .build());
      options.addOption( Option.builder()
            .longOpt( "dump")
            .desc( "dump input to stdout, in no particular format")
            .build());
      options.addOption( Option.builder()
            .longOpt( "xlsx")
            .desc( "output to specified .xlsx file")
            .hasArg()
            .build());
      options.addOption( Option.builder()
            .longOpt( "db")
            .desc( "update given SQL database, using specified JDBC connection string")
            .hasArg()
            .build());
      return options;
   }

   public static void main( String[] args) throws IOException, ParseException
   {
      options = makeOptions();
      CommandLine cmdLine = parseCommandLine( args);
      if (cmdLine.hasOption( 'h')) showHelp();
      String infileName = "test.xls";
      if (cmdLine.hasOption( 'f'))
      {
         infileName = cmdLine.getOptionValue( 'f');
         if (cmdLine.hasOption( "dump"))
            dumpToStdout( infileName);
         if (cmdLine.hasOption( "xslx"))
         {
            String outfileName = cmdLine.getOptionValue( "xslx");
            dumpToExcelFile( infileName, outfileName);
         }
         if (cmdLine.hasOption( "db"))
         {
            String jdbcConnectionString = cmdLine.getOptionValue( "db");
            // Magical JDBC connection
            updateDatabase( infileName, jdbcConnectionString);
         }
      }
   }

   private static CommandLine parseCommandLine( String[] args) throws ParseException
   {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmdLine = parser.parse( options, args);
      return cmdLine;
   }

   private static void showHelp()
   {
      String overview = "Read an Excel file and do something with it, one of --dump, --xslx, --db";
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp( "java -jar <this-jar-file>", overview, options, "", false);
   }

   private static void dumpToStdout( String anInfileName) throws IOException
   {
      Workbook workbook = readFile( anInfileName);
      for (Sheet sheet : workbook) // For all sheets in the workbook...
         for (Row row : sheet)
            for (Cell cell : row)
            {

               Font font = workbook.getFontAt( cell.getCellStyle().getFontIndex());
               List<String> formats = new ArrayList<String>();
               if (font.getBold()) formats.add( "bold");
               if (font.getItalic()) formats.add( "italic");
               String format;
               if (formats.isEmpty())
                  format = "";
               else
                  format = " (" + String.join( " ", formats) + ")";

               String cellValue;

               switch (cell.getCellTypeEnum())
               {
               case BLANK:
                  cellValue = "";
                  break;
               case BOOLEAN:
                  cellValue = cell.getBooleanCellValue() ? "TRUE" : "FALSE";
                  break;
               case ERROR:
                  cellValue = String.format( "(#ERROR %g)", cell.getErrorCellValue());
                  break;
               case FORMULA:
                  cellValue = String.format( "(FORMULA: %s)", cell.getCellFormula());
                  break;
               case NUMERIC:
                  cellValue = new Double( cell.getNumericCellValue()).toString();
                  break;
               case STRING:
                  cellValue = cell.getStringCellValue();
                  break;
               case _NONE:
                  cellValue = "(#NONE)";
                  break;
               default:
                  cellValue = String.format( "(UNEXPECTED CELL TYPE: %s)", cell.getCellTypeEnum());
                  break;
               }
               System.out.println( String.format( "got [%s] %s %s", cell.getCellTypeEnum(), cellValue, format));
            }
   }

   private static void dumpToExcelFile( String anInfileName, String anOutfileName)
   {
      // TODO Auto-generated method stub
      
   }

   private static void updateDatabase( String anInfileName, String aJdbcConnectionString)
   {
      // TODO Auto-generated method stub
      
   }

   private static Workbook readFile( String filename) throws IOException
   {
      try (FileInputStream fis = new FileInputStream( filename))
      {
         try
         {
            return new HSSFWorkbook( fis);
         }
         catch (Exception exc)
         {
         }
      }
      try (FileInputStream fis = new FileInputStream( filename))
      {
         return new XSSFWorkbook( fis);
      }
   }
}
