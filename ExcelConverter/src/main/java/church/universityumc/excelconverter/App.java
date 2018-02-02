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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Hello world!
 *
 */
public class App
{
   private static void showHelp()
   {
      String help = "Reformat Excel file.\n\n" + "-h\thelp\n" + "-f\tinput file\n";
      System.out.println( help);
   }

   public static void main( String[] args) throws IOException, ParseException
   {
      CommandLine cmdLine = parseCommandLine( args);
      if (cmdLine.hasOption( 'h')) showHelp();
      String infileName = "test.xls";
      if (cmdLine.hasOption( 'f'))
      {
         infileName = cmdLine.getOptionValue( 'f');
         dumpExcelFile( infileName);
      }
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

   private static void dumpExcelFile( String anInfileName) throws IOException
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
