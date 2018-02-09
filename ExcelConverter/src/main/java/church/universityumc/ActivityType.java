package church.universityumc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityType
{
   private static final Pattern YEAR_RE = Pattern.compile( "\\b\\d{4}\\b");
   private static final Pattern COMMITTEE_RE = Pattern.compile( "\\bcommittees?\\b", Pattern.CASE_INSENSITIVE);
   private static final String[]            MONTH_RES        = { "(jan(uary)?", "feb(ruary)?", "mar(ch)?", "apr(il)?",
         "may", "june?", "july?", "aug(ust)?", "sep(t(ember)?)?", "oct(ober)?", "nov(ember)?", "dec(ember)?" };
   private static final Pattern MONTH_RE; // = Pattern.compile(
         // "\\b(jan(uary)?|feb(ruary)?|mar(ch)?|apr(il)?|may|june?|july?|aug(ust)?|sep(t(ember)?)?|oct(ober)?|nov(ember)?|dec(ember)?)\\b",
         // Pattern.CASE_INSENSITIVE);

   static {
      StringBuilder concatenatedREs = Arrays.asList( MONTH_RES).stream().collect( 
            () -> new StringBuilder(), 
            (sb,s) -> {if (sb.length() > 0) sb.append("|"); sb.append(s);}, 
            (sb1,sb2) -> {sb1.append(sb2);});
      concatenatedREs.insert(0, "\\b");
      concatenatedREs.append("\\b");
      MONTH_RE = Pattern.compile( concatenatedREs.toString(), Pattern.CASE_INSENSITIVE);
   }
   
   /**
    * Map from {@link #name} to {@link ActivityType}.
    */
   private static Map<String,ActivityType> allActivityTypes = new HashMap<String,ActivityType>();
   
   private String name;
   
   /**
    * We use Calendar instead of Date because some date fields may not be known.
    */
   private Calendar startDate = new GregorianCalendar(); 

   private ActivityType() {}
   
   /**
    * Parses given string into a true activity type and a start date for the activity. Start date is not a part of the
    * activity type, but, because of the way we store data in ACS, may be part of the activity type specification. It
    * will not be stored; this data must be transferred into an {@link ActivityEngagement} object.
    * 
    * @param anActivityType
    * @return
    */
   public static ActivityType find( String anActivityType)
   {
      Integer year;
      
      Matcher yrMatcher = YEAR_RE.matcher( anActivityType);
      if (yrMatcher.matches()) 
      {
         year = Integer.parseInt( yrMatcher.group());
         yrMatcher.replaceFirst( "");
      }
      else
         year = null;
      
      // TODO: turn month string into number (in range [1..12]?)
      String month;
      Integer monthNum;
      Matcher monthMatcher = MONTH_RE.matcher( anActivityType);
      if (monthMatcher.matches())
      {
         month = monthMatcher.group();
         monthMatcher.replaceFirst( "");
         monthNum = 0;
         
      }
      // TODO Auto-generated method stub -- find/add in static set.
      return null;
   }

   /**
    * In our ACS system, our activity types sometimes imply activity start dates (e.g., "2015 Committee" vs. "2017
    * committee").
    * 
    * @return
    */
   public Date getStartDate()
   {
      return startDate.getTime();
   }
}
