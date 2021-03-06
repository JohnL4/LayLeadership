package church.universityumc.excelconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.StackWalker.StackFrame;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
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
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
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
import church.universityumc.ActivityType;
import church.universityumc.Log;
import church.universityumc.MemberData;
import church.universityumc.ParsedDate;
import church.universityumc.ChurchMember;
import church.universityumc.Comment;
import church.universityumc.EnumResolutionException;
import church.universityumc.InferredSkill;
import church.universityumc.InfoSource;
import church.universityumc.RowType;
import church.universityumc.Skill;
import church.universityumc.VocationalSkill;
import church.universityumc.excelconverter.ui.MainController;

/**
 * Hello world!
 *
 */
public class App
{

   /**
    * Expected column header for expected columns describing {@link ChurchMember}s. Used to allow a little flexibility
    * in where the columns appear.
    */
   private static final String           
      NAME = "Name", 
      AGE = "Age", 
      PHONE = "Preferred Phone",
      EMAIL = "Preferred E-mail", 
      DATE_JOINED = "Date Joined";

   /**
    * Column headers for Activity sections.
    */
   private static final String
      CATEGORY = "Category",
      ELEMENT_1 = "Element 1",
      ELEMENT_2 = "Element 2",
      ELEMENT_3 = "Element 3",
      ELEMENT_4 = "Element 4";

   private static final String  ACTIVITIES               = "ACTIVITIES";
   private static final String  COMMENTS                 = "COMMENTS";
   private static final Pattern NO_ROTATION_REGEX        = Pattern.compile( "no rotation", Pattern.CASE_INSENSITIVE);
   private static final Pattern CHURCH_COUNCIL_REGEX     = Pattern.compile( "council", Pattern.CASE_INSENSITIVE);
   
   /**
    * Identifies a row specifying a member's vocation, from the 2006 Personal Ministry Survey.
    */
   private static final String  PERSNL_MINSTR_06_VOCAT_L = "Persnl Minstr 06 Vocat'l";

   private static boolean headersInitialized;

   /**
    * Map from String column header to column index corresponding to header.
    */
   private static Map<String, Integer> memberHeaderColumnNumbers   = new HashMap<String, Integer>();
   private static Map<String, Integer> activityHeaderColumnNumbers = new HashMap<String, Integer>();

   private static final SimpleDateFormat SIMPLE_DATE_FORMAT         = new SimpleDateFormat();
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT_SLASHES = new SimpleDateFormat( "M/d/y");
   private static final Calendar         CALENDAR                   = Calendar.getInstance();

   private static JAXBContext  vocationalSkillJaxbContext;
   private static Marshaller   vocationalSkillMarshaller;
   private static Unmarshaller vocationalSkillUnmarshaller;   
   
   private static JAXBContext inferredSkillJaxbContext;
   public static Marshaller   inferredSkillMarshaller; // public for access from ActivityEngagement.toSkill();
   public static Unmarshaller inferredSkillUnmarshaller;
   
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
            .longOpt( "s16")
            .hasArg()
            .desc( "2016 survey results, as a spreadsheet file")
            .build());
      options.addOption( Option.builder()
            .longOpt( "dump")
            .desc( "dump input to console, in no particular format")
            .build());
      options.addOption( Option.builder()
            .longOpt( "parse")
            .desc( "parse input and dump to console in a somewhat structured form")
            .build()
            );
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
      options.addOption( Option.builder()
            .longOpt( "jaxb")
            .desc( "play around with JAXB -- Java Architecture to XML Bindings")
            .build());
      options.addOption( Option.builder()
            .longOpt( "nogui")
            .desc( "don't launch the GUI")
            .build());
      return options;
   }

   public static void main( String[] args) throws IOException, UnknownRowTypeException, JAXBException, ParseException
   {
      // NOTE: for GUI processing, see MainController (launched from here unless option "nogui" is given).
      // Don't need system property, since we're about to explicitly load the config file as an InputStream.
//      System.setProperty( "java.util.logging.config.file", "logging.properties");
      // Leading "/" means "top of classpath", basically.
      InputStream str = App.class.getResourceAsStream( "/logging.properties");
      if (str == null)
         System.out.printf( "java.class.path = %s%n", System.getProperty( "java.class.path"));
      else
         LogManager.getLogManager().readConfiguration( str);
      Log.warn( "test warning");
      Log.debug( "test debug");
      
      initGlobals();

      options = makeOptions();
      CommandLine cmdLine;
      try {
         cmdLine = parseCommandLine( args);
      }
      catch (UnrecognizedOptionException exc)
      {
         exc.printStackTrace();
         showHelp();
         cmdLine = null; // Satisfy compiler/IDE.
         System.exit( 1);
      }
      if (cmdLine.hasOption( 'h')) showHelp();

      String infileName; // = "test.xls";
      MemberData memberData = null;
      if (cmdLine.hasOption( 'f'))
      {
         infileName = cmdLine.getOptionValue( 'f');
         if (cmdLine.hasOption( "dump")) dumpToConsole( infileName);
         memberData = buildChurchMembers( new File( infileName));
      }
      else
         memberData = new MemberData();
      if (cmdLine.hasOption( "s16"))
      {
         updateWithSurvey2016( memberData, cmdLine.getOptionValue( "s16"));
      }
      if (cmdLine.hasOption( "xlsx"))
      {
         String outfileName = cmdLine.getOptionValue( "xlsx");
         dumpToExcelFile( memberData, new File( outfileName));
      }
      if (cmdLine.hasOption( "db"))
      {
         String jdbcConnectionString = cmdLine.getOptionValue( "db");
         // Magical JDBC connection
         updateDatabase( memberData, jdbcConnectionString);
      }
      if (cmdLine.hasOption( "jaxb")) playWithJaxb();
      if (cmdLine.hasOption( "nogui"))
         ;
      else
      {
         System.out.println( "Launching GUI...");
         MainController.launch( MainController.class, new String[] {});
      }
      System.out.println( "Done.");
      Log.debug( "Done");
   }

   private static void initGlobals() throws JAXBException, PropertyException
   {
      vocationalSkillJaxbContext = JAXBContext.newInstance( VocationalSkill.class);
      vocationalSkillMarshaller = vocationalSkillJaxbContext.createMarshaller();
      vocationalSkillMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, false);
      vocationalSkillMarshaller.setProperty( Marshaller.JAXB_FRAGMENT, true);
      vocationalSkillUnmarshaller = vocationalSkillJaxbContext.createUnmarshaller();

      inferredSkillJaxbContext = JAXBContext.newInstance( InferredSkill.class);
      inferredSkillMarshaller = inferredSkillJaxbContext.createMarshaller();
      inferredSkillMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, false);
      inferredSkillMarshaller.setProperty( Marshaller.JAXB_FRAGMENT, true);
      inferredSkillUnmarshaller = inferredSkillJaxbContext.createUnmarshaller();
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

   /**
    * Process the given ACS files into the given output file, overwriting it.
    * @param anInputFilesColl
    * @param anOutputFile
    * @throws UnknownRowTypeException 
    * @throws IOException 
    */
   public static void processAcsFiles( Collection<File> anInputFilesColl, File anOutputFile) throws IOException, UnknownRowTypeException
   {
      assert anInputFilesColl != null; // && anInputFilesColl.size() == 1 : "exactly one input file";
      assert anOutputFile != null;
      
//      File firstFile = anInputFilesColl.stream().findFirst().get();
      MemberData memberData = new MemberData();
      for (File file : anInputFilesColl)
      {
         MemberData tempMemberData = buildChurchMembers( file);
         memberData.merge( tempMemberData);
      }
      dumpToExcelFile( memberData, anOutputFile);
   }
   
   private static void dumpToConsole( String anInfileName) throws IOException
   {
      Workbook workbook = readFile( new File( anInfileName));
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

   private static void dumpToConsole( Collection<ChurchMember> churchMembers)
   {
      for (ChurchMember member : churchMembers)
      {
         System.out.printf( "%s%n", member);
         if (member.getServiceHistory() != null)
         {
            for (ActivityEngagement act : member.getServiceHistory())
            {
               System.out.printf( "    %s%n", act);
            }
         }
         if (member.getSkills() != null)
         {
            System.out.printf( "    --------%n");
            for (Skill skill : member.getSkills())
            {
               System.out.printf( "    %s%n", skill);
            }
         }
         if (member.getComments() != null)
         {
            System.out.printf( "    --------%n");
            for (Comment comment : member.getComments())
            {
               System.out.printf( "    %s%n", comment);
            }
         }
      }
   }

   private static void playWithJaxb() throws JAXBException
   {
      VocationalSkill vskill = new VocationalSkill( "Category Name", "<\"B & O\" Railroad>", "");

      System.out.println( "Marshall...");
      System.out.printf( "%s -->%n", vskill);
      StringWriter sw = new StringWriter();
      vocationalSkillMarshaller.marshal( vskill, sw);
      String elt1 = sw.toString();
      System.out.println( elt1);

      vskill.subcategory = null;
      System.out.printf( "%s -->%n", vskill);
      sw = new StringWriter();
      vocationalSkillMarshaller.marshal( vskill, sw);
      String elt2 = sw.toString();
      System.out.println( elt2);

      System.out.println( "Unmarshall...");
      StringReader sr = new StringReader( elt1);
      Object vskillObj = vocationalSkillUnmarshaller.unmarshal( sr);
      vskill = (VocationalSkill) vskillObj;
      System.out.println( vskill);

      sr = new StringReader( elt2);
      vskillObj = vocationalSkillUnmarshaller.unmarshal( sr);
      vskill = (VocationalSkill) vskillObj;
      System.out.println( vskill);

      System.out.println( "Test InferredSkill...");
      ActivityEngagement ae = new ActivityEngagement( "Activity Type", "Activity Name", "", "Activity Role");
      Skill iskill = ae.toSkill();
      System.out.println( iskill);
   }

   private static void dumpToExcelFile( MemberData aMemberData, File anOutfile) 
         throws IOException
   {
      SpreadsheetWriter ssWriter = new SpreadsheetWriter();
      ssWriter.dumpToExcelFile( aMemberData, anOutfile);
   }

   private static Font getCellFont( Workbook workbook, Cell cell)
   {
      Font font = workbook.getFontAt( cell.getCellStyle().getFontIndex());
      return font;
   }

   private static void updateDatabase( MemberData aMemberData, String aJdbcConnectionString)
         throws IOException
   {

   }

   /**
    * Build an internal database of {@link ChurchMember}s and all the associated collections of objects.
    * 
    * @param anInfile
    * @return
    * @throws IOException
    * @throws UnknownRowTypeException
    */
   private static MemberData buildChurchMembers( File anInfile)
         throws IOException, UnknownRowTypeException
   {
      MemberData retval = new MemberData();

      Workbook workbook = readFile( anInfile);

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
               case EmptyRow:
                  break;
               case PageHeader:
                  updateAcsRunDate( retval, row);
                  break;
               case ReportSummary:
                  break;
               case ActivitiesSectionMarker:
               case CommentsSectionMarker:
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
                  retval.addMember( currentChurchMember);
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
                        Skill skill = activityEngagement.toSkill(); 
                        currentChurchMember.addSkill( skill);
                     }
                     else
                        // It's an activity engagement?
                        currentChurchMember.addServiceHistory( activityEngagement);
                  }
                  break;
               case Vocation:
               {
                  Skill skill = parseSkill( row);
                  currentChurchMember.addSkill( skill);
                  break;
               }
               case Comment:
                  currentChurchMember.addComment( parseComment( row));
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
            Log.debug( exc);
            Log.warn( exc);
         }
      }
      return retval;
   }

   private static void updateWithSurvey2016( MemberData aMemberData, String aSurveySpreadsheetName)
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * Update the ACS run date (q.v.) of the given MemberData from data in the given Row.
    * @param aMemberData
    * @param aRow
    */
   private static void updateAcsRunDate( MemberData aMemberData, Row aRow)
   {
      String firstCellValue = aRow.getCell( 0).getStringCellValue();
      switch (firstCellValue)
      {
         case "Date :":
            String dateString = aRow.getCell( 1).getStringCellValue();
            Date date = parseDate( dateString);
            if (date == null)
               Log.warn( "Unable to parse ACS run date %s", dateString);
            else
               aMemberData.setAcsRunDate( date);
            break;
         case "Time :":
            String timeString = aRow.getCell( 1).getStringCellValue();
            aMemberData.setAcsRunTime( timeString);
            break;
         default:
            Log.warn( "Unable to update ACS run date");
            break;
      }
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
                     member.setFullName( cell.getStringCellValue());
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
                     member.setDateJoined( joinDate);
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
      if (activityHeaderColumnNumbers.isEmpty())
      {
         // Iterate across and get indexes for each header pos'n.
         for (Cell cell : aRow)
            activityHeaderColumnNumbers.put( cell.getStringCellValue(), cell.getColumnIndex());
      }
   }

   /**
    * Parse the given row into an {@link ActivityEngagement}.
    * @param aRow
    * @return
    * @throws UnexpectedCellTypeException 
    */
   private static ActivityEngagement parseActivity( Row aRow) throws UnexpectedCellTypeException // TODO: this needs to be a variety of things: ActivityEngagement, Skill, etc.
   {
      ActivityEngagement retval = parseSpecialActivity( aRow);
      if (retval == null)
      {
         String activityType = aRow.getCell( activityHeaderColumnNumbers.get( CATEGORY)).getStringCellValue();
         String activityName = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_1)).getStringCellValue();
         String activityEndYearString;
         Cell activityEndYearCell = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_2));
         switch (activityEndYearCell.getCellTypeEnum())
         {
            case STRING: 
               activityEndYearString = activityEndYearCell.getStringCellValue();
               break;
            case NUMERIC:
               activityEndYearString = Long.toString( Double.valueOf( activityEndYearCell.getNumericCellValue()).longValue());
               break;
            case BLANK:
               activityEndYearString = ParsedDate.NO_DATE;
               break;
            default:
               throw new UnexpectedCellTypeException( String.format( "Unexpected cell type parsing integer: %s", activityEndYearCell.getCellTypeEnum()));
         }
         String activityRole = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_3)).getStringCellValue();

         retval = new ActivityEngagement( activityType, activityName, activityEndYearString, activityRole);
      }
      return retval;
   }

   /**
    * Some activity rows require special processing; handle that here, returning null if no special handling
    * is required.
    * @param aRow
    * @return a complete ActivityEngagement only if special processing is required.
    * @throws UnexpectedCellTypeException 
    */
   private static ActivityEngagement parseSpecialActivity( Row aRow) throws UnexpectedCellTypeException
   {
      ActivityEngagement retval;
      String activityType = aRow.getCell( activityHeaderColumnNumbers.get( CATEGORY)).getStringCellValue();
      Matcher matcher = CHURCH_COUNCIL_REGEX.matcher( activityType);
      if (matcher.find())
      {
         String activityStartDateString = activityYearString( aRow, ELEMENT_1);
         String activityRole = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_2)).getStringCellValue();
         String activityEndDateString = activityYearString( aRow, ELEMENT_3);

         retval = new ActivityEngagement( ActivityType.COMMITTEE_TYPE_NAME, activityType, activityStartDateString, 
               activityEndDateString, activityRole);
      }
      else retval = null;
      return retval;
   }

   /**
    * Returns the year contained in the given column (by field name) of the given row, or {@link ParsedDate#NO_DATE}
    * if there is no year in that column.
    * @param aRow
    * @param aFieldName
    * @return
    * @throws UnexpectedCellTypeException
    */
   private static String activityYearString( Row aRow, String aFieldName) throws UnexpectedCellTypeException
   {
      String retval;
      Cell activityYearCell = aRow.getCell( activityHeaderColumnNumbers.get( aFieldName));
      switch (activityYearCell.getCellTypeEnum())
      {
         case STRING: 
            retval = activityYearCell.getStringCellValue();
            break;
         case NUMERIC:
            retval = Long.toString( Double.valueOf( activityYearCell.getNumericCellValue()).longValue());
            break;
         case BLANK:
            retval = ParsedDate.NO_DATE;
            break;
         default:
            throw new UnexpectedCellTypeException( String.format( "Unexpected cell type parsing integer: %s", activityYearCell.getCellTypeEnum()));
      }
      return retval;
   }

   /**
    * Parse the given row as a {@Skill}.
    * @param aRow
    * @return
    * @throws JAXBException 
    */
   private static Skill parseSkill( Row aRow) throws JAXBException
   {
      Skill retval;
      
      String category, elt1, elt2, elt3, elt4;
      
      category = aRow.getCell( activityHeaderColumnNumbers.get( CATEGORY)).getStringCellValue();
      elt1 = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_1)).getStringCellValue();
      elt2 = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_2)).getStringCellValue();
      elt3 = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_3)).getStringCellValue();
      elt4 = aRow.getCell( activityHeaderColumnNumbers.get( ELEMENT_4)).getStringCellValue();
      
      switch (category)
      {
         case PERSNL_MINSTR_06_VOCAT_L:
            VocationalSkill vskill = new VocationalSkill( elt1, elt2, elt3);
            StringWriter sw = new StringWriter();
            vocationalSkillMarshaller.marshal( vskill, sw);
            retval = Skill.find( sw.toString(), InfoSource.PersonalMinistrySurvey2006);
            break;
         default:
            Log.warn( "Unexpected category in Skill row: \"%s\"", category);
            retval = null;
      }
      return retval;
   }

   /**
    * Returns the next non-empty string cell value or null if there are no more values.
    * @param iter
    * @return
    */
   private static String nextOrNull( Iterator<Cell> iter)
   {
      String retval;
      if (iter.hasNext())
      {
         do
         {
            retval = iter.next().getStringCellValue();
            if (retval == null || retval.equals( "")) retval = null;
         }
         while (iter.hasNext() && retval == null);
      }
      else
         retval = null;
      return retval;
   }

   /**
    * Parse the comments section into the current {@link ChurchMember}'s data as generalized comments.
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
      comment = new Comment( parseDate( commentDate), commentLevel, commentType, commentText, InfoSource.AcsComment);
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
                     retval = RowType.Comment;
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
            else if (cellType == CellType.BLANK && allCellsBlank( aRow)) 
            {
               retval = RowType.EmptyRow;
            }
            else
               throw new UnknownRowTypeException( aRow.getRowNum());
         }
         else
            throw new UnknownRowTypeException( aRow.getRowNum());
      }
      else
         retval = RowType.EmptyRow;

      return retval;
   }

   /**
    * Returns true iff all cells in the given row are blank (null, {@link CellType.Blank}, empty string).
    * @param aRow
    * @return
    */
   private static boolean allCellsBlank( Row aRow)
   {
      boolean retval = true;
      Iterator<Cell> cellIter = aRow.cellIterator();
      while (retval && cellIter.hasNext())
      {
         Cell cell = cellIter.next();
         if (cell == null || cell.getCellTypeEnum() == CellType.BLANK)
            continue;
         if (cell.getCellTypeEnum() == CellType.STRING)
         {
            String cellValue = cell.getStringCellValue();
            if (cellValue == null || cellValue.isEmpty())
               continue;
            else
            {
               retval = false;
               break;
            }
         }
         else
         {
            retval = false;
            break;
         }
      }
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

   private static Workbook readFile( File aFile) throws IOException
   {
      try (FileInputStream fis = new FileInputStream( aFile))
      {
         try
         {
            return new HSSFWorkbook( fis);
         }
         catch (Exception exc)
         {}
      }
      try (FileInputStream fis = new FileInputStream( aFile))
      {
         return new XSSFWorkbook( fis);
      }
   }
}
