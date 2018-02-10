package church.universityumc.excelconverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.StackWalker.StackFrame;
// import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import church.universityumc.ActivityEngagement;
import church.universityumc.ChurchMember;

/**
 * Hello world!
 *
 */
public class App
{
   /**
    * Expected column header for expected columns. Used to allow a little flexibility in where the columns appear.
    */
   private static final String           
      NAME = "Name", 
      AGE = "Age", 
      PHONE = "Preferred Phone",
      EMAIL = "Preferred E-mail", 
      DATE_JOINED = "Date Joined";

   private static final String  ACTIVITIES        = "ACTIVITIES";
   private static final String  COMMENTS          = "COMMENTS";
   private static final String  CATEGORY          = "Category";
   private static final Pattern NO_ROTATION_REGEX = Pattern.compile( "no rotation", Pattern.CASE_INSENSITIVE);

   private static boolean headersInitialized;

   /**
    * Map from String column header to column index corresponding to header.
    */
   private static Map<String, Integer>   memberHeaderColumnNumbers = new HashMap<String, Integer>();

   private static final SimpleDateFormat SIMPLE_DATE_FORMAT         = new SimpleDateFormat();
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT_SLASHES = new SimpleDateFormat( "M/d/y");
   private static final Calendar         CALENDAR                   = Calendar.getInstance();

   private static Options options;

   private static Options makeOptions()
   {
      Options options = new Options();
      options.addOption( "h", "help", false, "show help");
      options.addOption( Option.builder( "f") // "f", true, "input file");
            .desc( "input .xls or .xlsx file").hasArg().build());
      options.addOption(
            Option.builder().longOpt( "dump").desc( "dump input to stdout, in no particular format").build());
      options.addOption( Option.builder().longOpt( "xlsx").desc( "output to specified .xlsx file").hasArg().build());
      options.addOption( Option.builder().longOpt( "db")
            .desc( "update given SQL database, using specified JDBC connection string").hasArg().build());
      return options;
   }

   public static void main( String[] args) throws IOException, ParseException, UnknownRowTypeException
   {
      options = makeOptions();
      CommandLine cmdLine = parseCommandLine( args);
      if (cmdLine.hasOption( 'h')) showHelp();
      String infileName = "test.xls";
      if (cmdLine.hasOption( 'f'))
      {
         infileName = cmdLine.getOptionValue( 'f');
         if (cmdLine.hasOption( "dump")) dumpToStdout( infileName);
         if (cmdLine.hasOption( "xlsx"))
         {
            String outfileName = cmdLine.getOptionValue( "xlsx");
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

   private static void dumpToExcelFile( String anInfileName, String anOutfileName)
         throws IOException, UnknownRowTypeException
   {
      Collection<ChurchMember> churchMembers = buildChurchMembers( anInfileName);
      System.out.println( "Done.");
   }

   private static void updateDatabase( String anInfileName, String aJdbcConnectionString)
         throws IOException, UnknownRowTypeException
   {
      Collection<ChurchMember> churchMembers = buildChurchMembers( anInfileName);

   }

   /**
    * Build an internal database of {@link ChurchMember}s and all the associated collections of objects.
    * 
    * @param anInfileName
    * @return
    * @throws IOException
    * @throws UnknownRowTypeException
    */
   private static Collection<ChurchMember> buildChurchMembers( String anInfileName)
         throws IOException, UnknownRowTypeException
   {
      Collection<ChurchMember> churchMembers = new ArrayList<ChurchMember>();

      Workbook workbook = readFile( anInfileName);

      Sheet sheet = workbook.getSheetAt( 0);
      RowType previousSectionRowType = RowType.None;
      ChurchMember currentChurchMember = null;
      nextRow: for (Row row : sheet) // Label makes 'continue' stmts a little more obvious.
      {
         RowType rowType = getRowType( row, workbook, previousSectionRowType); // TODO: don't use prev. row type; use a
                                                                        // concept of "what section are we in?". So,
                                                                        // it could be something (e.g., section
                                                                        // marker) that was seen more than one row
                                                                        // ago.
         switch (rowType)
         {
            case PageHeader:
               break;
            case MemberHeader:
               if (memberHeaderColumnNumbers.size() == 0) buildMemberHeaderColumnNumbers( row);
               break;
            case Member:
               if (currentChurchMember == null)
               {}
               else
                  currentChurchMember.dumpText( System.out);
               currentChurchMember = parseMember( row);
               break;
            case ActivitiesHeader:
               buildActivitiesHeaderColumnNumbers( row);
               break;
            case Activity:
               currentChurchMember.addServiceHistory( parseActivity( row));
               break;
            case ActivitiesSectionMarker:
            case CommentsSectionMarker:
               break;
            case Comments:
               parseComments( row);
               break;
            default:
               warn( "Unexpected row type: %g", rowType);
               break;
         }
         switch (rowType)
         {
            // Important markers that can affect the processing of following rows.  Really, this should be more
            // of a state machine ("currentState = ...").
            case MemberHeader:
            case ActivitiesSectionMarker:
            case CommentsSectionMarker:
               previousSectionRowType = rowType;
               break;
            default:
               break;
         }
      }
      return churchMembers;
   }

   /**
    * Builds header-to-column map for member headers, unless it's already been built.
    * @param aRow
    */
   private static void buildMemberHeaderColumnNumbers( Row aRow)
   {
      if (memberHeaderColumnNumbers.isEmpty())
      {
         // Iterate across and get indexes for each header pos'n.
         for (Cell cell : aRow)
            memberHeaderColumnNumbers.put( cell.getStringCellValue(), cell.getColumnIndex());
      }
   }

   /**
    * Parse the given row into a {@link ChurchMember}. Row contains basic properties such as email, phone, name, age,
    * join date, etc.
    * 
    * @param aRow
    * @return
    */
   private static ChurchMember parseMember( Row aRow)
   {
      ChurchMember member = new ChurchMember();
      for (String header : memberHeaderColumnNumbers.keySet()) 
      {
         int colNum = memberHeaderColumnNumbers.get( header);
         Cell cell = aRow.getCell( colNum);
         if (cell == null) {}
         else
         {
            switch (header)
            {
               case NAME:
                  member.setName( aRow.getCell( colNum).getStringCellValue());
                  break;
               case AGE:
                  try
                  {
                     member.setAge( Integer.parseInt( aRow.getCell( colNum).getStringCellValue()));
                     member.setAgeAsOf( new Date());
                  }
                  catch (NumberFormatException exc)
                  {
                     warn( "NumberFormatException parsing age \"%s\" at row %d",
                           aRow.getCell( colNum).getStringCellValue(), aRow.getRowNum());
                  }
                  break;
               case PHONE:
                  member.setPhone( aRow.getCell( colNum).getStringCellValue());
                  break;
               case EMAIL:
                  member.setEmail( aRow.getCell( colNum).getStringCellValue());
                  break;
               case DATE_JOINED:
                  Date joinDate = parseDate( aRow.getCell( colNum).getStringCellValue());
                  if (joinDate != null)
                  {
                     Calendar cal = Calendar.getInstance();
                     cal.setTime( joinDate);
                     member.setYearJoined( cal.get( Calendar.YEAR));
                  }
                  break;
               default:
                  warn( "Member header unhandled at row %d: %s", aRow.getRowNum(), header);
                  break;
            }
         }
      }
      return member;
   }

   /**
    * Build a map from Activities column headers to column offsets.
    * 
    * @param aRow
    */
   private static void buildActivitiesHeaderColumnNumbers( Row aRow)
   {
      // We don't actually need column headers.
   }

   /**
    * Parse the given row into an {@link ActivityEngagement}.
    * @param aRow
    * @return
    */
   private static ActivityEngagement parseActivity( Row aRow)
   {
      Iterator<Cell> iter = aRow.cellIterator();
      
      String activityType = iter.next().getStringCellValue();
      String activityName = iter.next().getStringCellValue();
      String activityEndYearString = iter.next().getStringCellValue();
      String activityRole = iter.next().getStringCellValue();
      
      int activityEndYear;
      boolean hasEndYear;
      Date endDate;
      
      Matcher noRotationMatcher = NO_ROTATION_REGEX.matcher( activityEndYearString);
      if (noRotationMatcher.find())
      {
         hasEndYear = false;
         activityEndYear = 0;
         endDate = null;
      }
      else
      {
         hasEndYear = true;
         // First, try to parse as a normal date, just in case we can. Otherwise, we'll assume it's one
         // or more years separated by non-numeric characters (slash, comma, space, whatever).
         endDate = parseDate( activityEndYearString);
         if (endDate == null)
         {
            String[] endYears = activityEndYearString.split( "\\D+");
            if (endYears.length == 0)
               warn( "Unable to find end years in \"%s\" in row %d", activityEndYearString, aRow.getRowNum());
            else
               try
               {
                  // Assume last year is the true end year.
                  activityEndYear = Integer.parseInt( endYears[endYears.length - 1]);
                  Calendar cal = Calendar.getInstance();
                  cal.clear();
                  cal.set( Calendar.YEAR, activityEndYear);
                  cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR));
                  endDate = cal.getTime();
               }
               catch (NumberFormatException exc)
               {
                  warn( "Unable to parse end year \"%s\" (element 2) at row %d", activityEndYearString,
                        aRow.getRowNum());
               }
         }
      }
      // TODO: might need both an end year AND an end date in ActivityEngagement, since we might have only the year, and
      // a falsely-precise Date could be misleading.
      
      return new ActivityEngagement( activityType, activityName, activityRole, null, endDate, hasEndYear);
   }

   /**
    * Parse the comments section into the current {@link ChurchMember}'s data.
    * 
    * @param aRow
    */
   private static void parseComments( Row aRow)
   {
      // TODO Auto-generated method stub

   }

   private static RowType getRowType( Row aRow, Workbook aWorkbook, RowType aCurrentSection)
         throws UnknownRowTypeException
   {
      RowType retval;

      int firstCellIx = aRow.getFirstCellNum();
      if (firstCellIx >= 0)
      {
         Cell firstCell = aRow.getCell( firstCellIx);
         CellType cellType = firstCell.getCellTypeEnum();
         if (firstCellIx == 0)
         {
            if (cellType == CellType.STRING)
            {
               String cellValue = firstCell.getStringCellValue();
               if (cellValue.equals( "Date :") || cellValue.equals( "Time :"))
                  retval = RowType.PageHeader;
               else
               {
                  Font font = getCellFont( aWorkbook, firstCell);
                  // Assumption: first cell of header row will be "Name" and no church member will have a name of
                  // "Name".
                  if (cellValue.equals( NAME) && font.getBold())
                     retval = RowType.MemberHeader;
                  else if (cellValue.equals( ACTIVITIES) && font.getBold() && font.getItalic())
                     retval = RowType.ActivitiesSectionMarker;
                  else if (cellValue.equals( COMMENTS) && font.getBold() && font.getItalic())
                     retval = RowType.CommentsSectionMarker;
                  else if (cellValue.equals( CATEGORY) && font.getBold()
                        && aCurrentSection == RowType.ActivitiesSectionMarker)
                     retval = RowType.ActivitiesHeader;
                  else if (parseDate( cellValue) != null && !font.getBold() && !font.getItalic()
                        && aCurrentSection == RowType.CommentsSectionMarker)
                     retval = RowType.Comments;
                  else if (font.getBold() && !font.getItalic()) // Could be preceded by Activities or Comments or
                                                                // MemberHeader
                  {
                     if (cellValue.equals( "Total Records:"))
                        retval = RowType.ReportSummary;
                     else
                        retval = RowType.Member;
                  }
                  else if (! font.getBold() && ! font.getItalic())
                  {
                     if (aCurrentSection == RowType.ActivitiesSectionMarker
                           || aCurrentSection == RowType.ActivitiesHeader)
                        retval = RowType.Activity;
                     else
                        throw new UnknownRowTypeException( aRow.getRowNum());
                  }
                  else
                     throw new UnknownRowTypeException( aRow.getRowNum());
               }
            }
            else
               throw new UnknownRowTypeException( aRow.getRowNum());
         }
         else
            throw new UnknownRowTypeException( aRow.getRowNum());
      }
      else
         throw new UnknownRowTypeException( aRow.getRowNum());

      return retval;
   }

   /**
    * Attempts to parse a string as a date, returning the given Data on success or null on failure.
    * 
    * @param aDateString
    * @return
    */
   private static Date parseDate( String aDateString)
   {
      Date retval;

      try
      {
         retval = SIMPLE_DATE_FORMAT_SLASHES.parse( aDateString);
      }
      catch (java.text.ParseException exc)
      {
         try 
         {
            retval = SIMPLE_DATE_FORMAT.parse( aDateString);
         }
         catch (java.text.ParseException exc2)
         {
            retval = null;
         }
      }
      return retval;
   }

   private static void initializeMemberHeaders( Row aRow)
   {
      // TODO Auto-generated method stub
      headersInitialized = true;
   }

   private static boolean isMemberHeaderRow( Row aRow)
   {
      // TODO Auto-generated method stub
      return false;
   }

   private static boolean isPageHeaderRow( Row aRow)
   {
      boolean retval;

      int firstCellIx = aRow.getFirstCellNum();
      if (firstCellIx >= 0)
      {
         Cell firstCell = aRow.getCell( firstCellIx);
         CellType cellType = firstCell.getCellTypeEnum();
         if (firstCellIx == 0)
         {
            if (cellType == CellType.STRING)
            {
               String cellValue = firstCell.getStringCellValue();
               if (cellValue.equals( "Date :") || cellValue.equals( "Time :"))
                  retval = true;
               else
                  retval = false;
            }
            else
               retval = false;
         }
         else
            retval = false;
      }
      else
         retval = false;

      return retval;
   }

   /**
    * Log a message to stderr, followed by a newline.
    * 
    * @param aFormat
    * @param args
    */
   private static void warn( String aFormat, Object... args)
   {
      Optional<StackFrame> caller = StackWalker.getInstance().walk( stream -> stream.skip(1).findFirst());
      StringBuilder fmt = new StringBuilder();
      fmt.append( "WARNING: ").append( aFormat);
      if (caller.isPresent())
      {
         StackFrame callerSF = caller.get();
         fmt.append( " (")
            .append( callerSF.getMethodName())
            .append( "(), ")
            .append( callerSF.getFileName())
            .append(  ":")
            .append(  callerSF.getLineNumber())
            .append( ")");
      }
      fmt.append( "\n");
      System.err.printf( fmt.toString(), args);
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
         {}
      }
      try (FileInputStream fis = new FileInputStream( filename))
      {
         return new XSSFWorkbook( fis);
      }
   }
}
