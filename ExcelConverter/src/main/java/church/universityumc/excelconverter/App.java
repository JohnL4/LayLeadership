package church.universityumc.excelconverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import church.universityumc.ChurchMember;

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
   
   /**
    * Expected column header for expected columns. Used to allow a little flexibility in where the columns 
    * appear.
    */
   private static final String
      NAME = "Name",
      AGE = "Age",
      PHONE = "Preferred Phone",
      EMAIL = "E-mail",
      DATE_JOINED = "Date Joined"
      ;
   
   /**
    * Map from String column header to column index corresponding to header.
    */
   private static Map<String, Integer> headerColumnIndex; 

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

               Font font = getCellFont( workbook, cell);
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

   private static Font getCellFont( Workbook workbook, Cell cell)
   {
      Font font = workbook.getFontAt( cell.getCellStyle().getFontIndex());
      return font;
   }

   private static void dumpToExcelFile( String anInfileName, String anOutfileName) throws IOException
   {
      Collection<ChurchMember> churchMembers = buildChurchMembers( anInfileName);
      
   }

   private static void updateDatabase( String anInfileName, String aJdbcConnectionString) throws IOException
   {
      Collection<ChurchMember> churchMembers = buildChurchMembers( anInfileName);
      
   }

   /**
    * Build an internal database of {@link ChurchMember}s and all the associated collections of objects.
    * @param anInfileName
    * @return
    * @throws IOException 
    */
   private static Collection<ChurchMember> buildChurchMembers( String anInfileName) throws IOException
   {
      Collection<ChurchMember> churchMembers = new ArrayList<ChurchMember>();
      
      Workbook workbook = readFile( anInfileName);
      
      Sheet sheet = workbook.getSheetAt( 0);
      for (Row row : sheet) // TODO: label, so can do "continue <label>" instead of just "continue".
      {
         int firstCellIx = row.getFirstCellNum();
         if (firstCellIx >= 0)
         {
            Cell firstCell = row.getCell( firstCellIx);
            CellType cellType = firstCell.getCellTypeEnum();
            if (firstCellIx == 0)
            {
               if (cellType == CellType.STRING)
               {
                  String cellValue = firstCell.getStringCellValue();
                  if (cellValue.equals( "Date :") || cellValue.equals( "Time :"))
                     continue; // Next row.
                  Font font = getCellFont( workbook, firstCell);
                  // Assumption: first cell of header row will be "Name" and no church member will have a name of "Name".
                  // TODO: recognize header row by cmd-line-specified list of header labels, possibly even have a dynamic
                  // list of header values per user row?  Instead of this static property thing.
                  if (cellValue.equals( NAME) && font.getBold())
                  {
                     // Iterate across and get indexes for each header pos'n.
                     for (Cell cell : row)
                     {
                        if (cell.getCellTypeEnum() == CellType.STRING)
                        {
                           headerColumnIndex = new HashMap<String, Integer>();
                           switch (cell.getStringCellValue())
                           {
                           case NAME:
                              headerColumnIndex.put( NAME, cell.getColumnIndex());
                              break;
                           case AGE:
                              headerColumnIndex.put( AGE, cell.getColumnIndex());
                              break;
                           case PHONE:
                              headerColumnIndex.put( PHONE, cell.getColumnIndex());
                              break;
                           case EMAIL:
                              headerColumnIndex.put( EMAIL, cell.getColumnIndex());
                              break;
                           case DATE_JOINED:
                              headerColumnIndex.put( DATE_JOINED, cell.getColumnIndex());
                              break;
                           default:
                              warn( "Unexpected column header: %s\n", cell.getStringCellValue());
                              break;
                           }
                        }
                        else
                           warn( "Unexpected cell type in header row: %g", cell.getCellTypeEnum());
                     }
                     
                     continue; // Header row, skip.
                  }
               }
            }
         }
      }
      
      return churchMembers;
   }

   /**
    * Log a message to stderr.
    * @param aFormat
    * @param args
    */
   private static void warn( String aFormat, Object ... args)
   {
      System.err.printf( "WARNING: " + aFormat, args);
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
