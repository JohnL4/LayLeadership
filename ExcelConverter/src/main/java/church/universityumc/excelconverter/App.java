package church.universityumc.excelconverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.StackWalker.StackFrame;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
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
import church.universityumc.Log;
import church.universityumc.ChurchMember;
import church.universityumc.RowType;
import church.universityumc.Skill;

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

   private static final String  ACTIVITIES               = "ACTIVITIES";
   private static final String  COMMENTS                 = "COMMENTS";
   private static final String  CATEGORY                 = "Category";
   private static final Pattern NO_ROTATION_REGEX        = Pattern.compile( "no rotation", Pattern.CASE_INSENSITIVE);
   
   /**
    * Identifies a row specifying a member's vocation, from the 2006 Personal Ministry Survey.
    */
   private static final String  PERSNL_MINSTR_06_VOCAT_L = "Persnl Minstr 06 Vocat'l";

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
            .desc( "input .xls or .xlsx file")
            .hasArg()
            .build());
      options.addOption(
            Option.builder()
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

   public static void main( String[] args) throws IOException, ParseException, UnknownRowTypeException
   {
      Logger logger = System.getLogger( "church.universityumc");
//      logger.log( Level.WARNING, "test warning");
//      AppLogger.getInstance().warn( "test warning", (Object[])null);
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
      System.out.println( "Done.");
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
      for (Row row : sheet) // Label makes 'continue' stmts a little more obvious.
      {
         Log.setRow( row.getRowNum());
         try
         {
            RowType rowType = getRowType( row, workbook, previousSectionRowType);
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
                  {
                     ActivityEngagement activityEngagement = parseActivity( row);
                     if (activityEngagement.getStartDate() == null)
                     {
                        // It's a skill?
                        currentChurchMember.addSkill( activityEngagement.toSkill());
                     }
                     else
                        // It's an activity engagement?
                        currentChurchMember.addServiceHistory( parseActivity( row));
                  }
                  break;
               case Vocation:
                  currentChurchMember.addSkill( parseSkill( row));
                  break;
               case ActivitiesSectionMarker:
               case CommentsSectionMarker:
                  break;
               case Comments:
                  currentChurchMember.addComment( parseComment( row));
                  break;
               case ReportSummary:
                  break;
               default:
                  Log.warn( "Unexpected row type: %s", rowType);
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
         catch (Exception exc)
         {
            Log.warn( exc);
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
            if (cell.getCellTypeEnum() != CellType.BLANK)
            {
               switch (header)
               {
                  case NAME:
                     member.setName( cell.getStringCellValue());
                     break;
                  case AGE:
                     switch (cell.getCellTypeEnum())
                     {
                        case STRING:
                           String cellValue = cell.getStringCellValue(); 
                           try
                           {
                              if (cellValue != null && ! cellValue.equals(""))
                              {
                                 member.setAge( Integer.parseInt( cellValue));
                                 member.setAgeAsOf( new Date());
                              }
                           }
                           catch (NumberFormatException exc)
                           {
                              Log.warn( "NumberFormatException parsing age \"%s\"", cellValue);
                           }
                           break;
                        case NUMERIC:
                           member.setAge( Math.round( cell.getNumericCellValue()));
                           member.setAgeAsOf( new Date());
                           break;
                        default:
                           Log.warn( "Unexpected cell type (%s)", cell.getCellTypeEnum());
                           break;
                     }
                     break;
                  case PHONE:
                     member.setPhone( cell.getStringCellValue());
                     break;
                  case EMAIL:
                     member.setEmail( cell.getStringCellValue());
                     break;
                  case DATE_JOINED:
                     Date joinDate = parseDate( cell.getStringCellValue());
                     if (joinDate != null)
                     {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime( joinDate);
                        member.setYearJoined( cal.get( Calendar.YEAR));
                     }
                     break;
                  default:
                     Log.warn( "Member header \"%s\" unhandled", header);
                     break;
               }
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
   private static ActivityEngagement parseActivity( Row aRow) // TODO: this needs to be a variety of things: ActivityEngagement, Skill, etc.
   {
      Iterator<Cell> iter = aRow.cellIterator();
      
      String activityType = iter.next().getStringCellValue();
      String activityName = iter.next().getStringCellValue();
      String activityEndYearString = iter.next().getStringCellValue();
      String activityRole = iter.next().getStringCellValue();
      
      return new ActivityEngagement( activityType, activityName, activityEndYearString, activityRole);
   }

   /**
    * Parse the given row as a {@Skill}.
    * @param row
    * @return
    */
   private static Skill parseSkill( Row aRow)
   {
      Skill retval;
      Iterator<Cell> iter = aRow.iterator();
      
      String category, elt1, elt2, elt3, elt4;
      
      category = nextOrNull( iter);
      elt1 = nextOrNull( iter);
      elt2 = nextOrNull( iter);
      elt3 = nextOrNull( iter);
      elt4 = nextOrNull( iter);
      
      switch (category)
      {
         case PERSNL_MINSTR_06_VOCAT_L:
            StringBuilder skillnameSB = new StringBuilder( String.format( "<vocational category=\"%s\"", elt1));
            if (elt2 != null)
            {
               if (elt2.equals( "Other"))
               {
                  if (elt3 != null)
                     skillnameSB.append( String.format( " subcategory=\"%s\"", elt3));
               }
               else
               {
                  skillnameSB.append( String.format( " subcategory=\"%s\"", elt2));
                  if (elt3 != null)
                     skillnameSB.append( String.format( " subsubcategory=\"%s\"", elt3));
               }
            }
            skillnameSB.append( "/>");
            retval = Skill.find( skillnameSB.toString());
            break;
         default:
            Log.warn( "Unexpected category in Skill row: \"%s\"", category);
            retval = null;
      }
      return retval;
   }

   private static String nextOrNull( Iterator<Cell> iter)
   {
      String retval;
      if (iter.hasNext())
      {
         retval = iter.next().getStringCellValue();
         if (retval == null || retval.equals( "")) 
            retval = null;
      }
      else
         retval = null;
      return retval;
   }

   /**
    * Parse the comments section into the current {@link ChurchMember}'s data.
    * 
    * @param aRow
    */
   private static Comment parseComment( Row aRow)
   {
      Iterator<Cell> iter = aRow.iterator();
      
      String commentDate = nextOrNull( iter);
      String commentLevel = nextOrNull( iter);
      String commentType = nextOrNull( iter);
      String commentText = nextOrNull( iter);
      
      Comment comment;
      try
      {
         comment = new Comment( parseDate( commentDate), commentLevel, commentType, commentText);
      }
      catch (IllegalArgumentException exc)
      {
         Log.warn( exc);
         comment = null;
      }
      return comment;
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
                        if (cellValue.equals( PERSNL_MINSTR_06_VOCAT_L))
                           retval = RowType.Vocation;
                        else
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
    * @deprecated Use {@link Log} instead.
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
