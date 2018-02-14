package church.universityumc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    * True if the given data explicitly indicates NO date (e.g., "No rotation year").
    */
   public boolean explicitNone;
   
   /**
    * If the string passed to {@link #ParsedDate(String)} matches this pattern, there is explicitly no date.
    */
   private static final Pattern NO_ROTATION_REGEX = Pattern.compile( "no rotation", Pattern.CASE_INSENSITIVE);
   
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT         = new SimpleDateFormat();
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT_SLASHES = new SimpleDateFormat( "M/d/y");

   /**
    * Attempts to parse a date out of the given string, which is assumed to be one of:
    * <ul>
    *   <li>A string matching one of the "explicitly no date" patterns ({@link #NO_ROTATION_REGEX})
    *   <li>A full date, in either local culture format or "M/d/y" format
    *   <li>A sequence of years separated by non-digits. Result will correspond to the last year in the sequence
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
            String[] endYears = aDateSpecification.split( "\\D+");
            if (endYears.length == 0)
            {
               throw new java.text.ParseException(
                     String.format( "Unable to find years in \"%s\"", aDateSpecification), 
                     0); 
            }
            else
               try
               {
                  // Assume last year is the true end year.
                  year = Integer.parseInt( endYears[endYears.length - 1]);
               }
               catch (NumberFormatException exc)
               {
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

}
