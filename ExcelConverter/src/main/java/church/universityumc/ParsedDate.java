package church.universityumc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Dumb tuple containing the results of attempting to parse a date out of a string.
 * @author john
 *
 */
public class ParsedDate
{
   public Integer year;
   
   /**
    * 1-based (1 = January)
    */
   public Integer month;
   
   /**
    * Day of the month.
    */
   public Integer day;
   
   /**
    * May be null, but, if not, will match {@link #year}, {@link #month}, {@link #day} (which will not be null).
    */
   public Date date;
   
   /**
    * The remainder of whatever string was used to construct this object, after all the date-specifying stuff
    * has been stripped out and used to build the date.  Whitespace will be preserved (insofar as possible).
    */
   public String remainder;
   
   /**
    * True if the given data explicitly indicates NO date (e.g., "No rotation year").
    */
   public boolean explicitNone;
   
   /**
    * A string signifying that there is explicitly no date to be parsed, and which is guaranteed to match 
    * {@link #NO_ROTATION_REGEX}.
    */
   public static final String NO_DATE = "No rotation date";
   
   /**
    * If the string passed to {@link #ParsedDate(String)} matches this pattern, there is explicitly no date.
    */
   private static final Pattern NO_ROTATION_REGEX = Pattern.compile( "no rotation", Pattern.CASE_INSENSITIVE);

   private static final SimpleDateFormat SIMPLE_DATE_FORMAT         = new SimpleDateFormat();
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT_SLASHES = new SimpleDateFormat( "M/d/y");

   private static final Pattern       YEAR_RE          = Pattern.compile( "\\d{4}");
   private static final List<String>  MONTH_RE_STRINGS = List.of( "jan(uary)?", "feb(ruary)?", "mar(ch)?", "apr(il)?",
         "may", "june?", "july?", "aug(ust)?", "sep(t(ember)?)?", "oct(ober)?", "nov(ember)?", "dec(ember)?");
   private static final List<Pattern> MONTH_RES;
   private static final Pattern       MONTHS_RE;

   static {
      MONTH_RES = Collections.unmodifiableList( MONTH_RE_STRINGS.stream()
            .map( s -> Pattern.compile( s, Pattern.CASE_INSENSITIVE))
            .collect( Collectors.toList()));
      String concatenatedREs = MONTH_RE_STRINGS.stream().collect( Collectors.joining( "|")); 
      MONTHS_RE = Pattern.compile( "\\b(" + concatenatedREs.toString() + ")\\b", Pattern.CASE_INSENSITIVE);
   }
   

   /**
    * Attempts to parse a date out of the given string, which is assumed to be one of:
    * <ul>
    *   <li>A string matching one of the "explicitly no date" patterns ({@link #NO_ROTATION_REGEX})
    *   <li>A full date, in either local culture format or "M/d/y" format
    *   <li>A sequence of years separated by non-digits. Result will correspond to the last year in the sequence
    *   <li>A 4-digit year optionally preceded by a month name (e.g., "Feb" or "February").
    * </ul>  
    * @param aDateSpecification
    * @throws ParseException - If thrown, indicates that none of the "explicitly no date" patterns matched,
    *   AND no date could be parsed out of the string.
    */
   public ParsedDate( String aDateSpecification) throws ParseException
   {
      Matcher noRotationMatcher = NO_ROTATION_REGEX.matcher( aDateSpecification);
      if (noRotationMatcher.find())
      {
         explicitNone = true;
         noRotationMatcher.reset();
         remainder = noRotationMatcher.replaceAll( "");
      }
      else
      {
         explicitNone = false;
         // First, try to parse as a normal date, just in case we can. Otherwise, we'll assume it's one
         // or more years separated by non-numeric characters (slash, comma, space, whatever).
         date = parseDate( aDateSpecification);
         if (date == null)
         {
            if (aDateSpecification == null || aDateSpecification.equals( ""))
               throw new java.text.ParseException( "Unable to parse date from null or empty string", 0);
            Matcher yearMatcher = YEAR_RE.matcher( aDateSpecification);
            List<String> endYears = yearMatcher.results()
               .filter( mr -> mr.start() < mr.end())
               .map( mr -> aDateSpecification.substring( mr.start(), mr.end()))
               .collect( Collectors.toList());
            if (endYears.size() == 0)
            {
               // End of the line: we tried everything we know at this point.
               throw new java.text.ParseException(
                     String.format( "Unable to find years in \"%s\"", aDateSpecification), 
                     0); 
            }
            else if (endYears.size() == 1)
            {
               // Might be month name + year, so try that.
               parseAsMonthNameAndYear( aDateSpecification);
            }
            else
               // Multiple years
               try
               {
                  // Assume last year is the true end year.
                  year = Integer.parseInt( endYears.get( endYears.size() - 1));
                  yearMatcher.reset();
                  remainder = yearMatcher.replaceAll( ""); // We consume ALL the years.
               }
               catch (NumberFormatException exc)
               {
                  // Really, this should never happen, given how we built endYears.
                  throw new java.text.ParseException( 
                        String.format( "Unable to parse year \"%s\"", aDateSpecification), 0);
               }
         }
         else
         {
            Calendar cal = Calendar.getInstance();
            cal.setTime( date);
            year = cal.get( Calendar.YEAR);
            month = cal.get( Calendar.MONTH) + 1;
            day = cal.get( Calendar.DAY_OF_MONTH);
         }
      }
   }
   
   /**
    * Parse given string as numeric year and month name (for example "Feb" or "February").  Pulls date-specifying material out of string
    * and stores remainder in {@link #remainder}.  Both year and month name are optional.
    * @param aMonthNameYearString
    * @return
    */
   private void parseAsMonthNameAndYear( String aMonthNameYearString)
   {
      // (Code lifted from ActivityType constructor.) 
      String monthNameYearString = aMonthNameYearString;
      Matcher yrMatcher = YEAR_RE.matcher( monthNameYearString);
      if (yrMatcher.find()) 
      {
         year = Integer.parseInt( yrMatcher.group());
         monthNameYearString = yrMatcher.replaceFirst( "");
      }
      else
         year = null;
      
      String monthName;
      Matcher monthMatcher = MONTHS_RE.matcher( monthNameYearString);
      if (monthMatcher.find())
      {
         monthName = monthMatcher.group();
         monthNameYearString = monthMatcher.replaceFirst( "");
         month = 1;
         for (Pattern monthRE : MONTH_RES)
         {
            if (monthRE.matcher( monthName).matches())
               break;
            else
               month++;
         }
         assert month < 13 : "Since we found a month name, we should have been able to assign a month number to it";
      }
      else
         month = null;
      remainder = monthNameYearString;
//      return year != null; 
   }
   
   /**
    * Attempts to parse a string as a date in format "M/d/y" or the current locale format, returning the 
    * given Data on success or null on failure.
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

}
