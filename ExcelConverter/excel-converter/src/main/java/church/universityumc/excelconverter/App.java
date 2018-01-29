package church.universityumc.excelconverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        dumpExcelFile();
    }
    
    private static void dumpExcelFile() throws IOException {
       String filename = "test.xls";
       HSSFWorkbook workbook = readFile( filename);
       for (Sheet sheet : workbook)
          for (Row row : sheet)
             for (Cell cell : row)
                System.out.println( "got: " + cell.getStringCellValue() + " (" + cell.getCellStyle() + ")");
    }
    
    private static HSSFWorkbook readFile(String filename) throws IOException {
       try (FileInputStream fis = new FileInputStream(filename)) {
               return new HSSFWorkbook(fis);        // NOSONAR - should not be closed here
       }
    }       
}
