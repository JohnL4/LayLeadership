package church.universityumc;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActivityType
{
   private static final Pattern YEAR_RE = Pattern.compile( "\\b\\d{4}\\b");
   private static final Pattern COMMITTEE_RE = Pattern.compile( "\\bcommittees?\\b", Pattern.CASE_INSENSITIVE);
   private static final Pattern MONTH_RE = Pattern.compile(
         "\\b(jan(uary)?|feb(ruary)?|mar(ch)?|apr(il)?|may|june?|july?|aug(ust)?|sep(t(ember)?)?|oct(ober)?|nov(ember)?|dec(ember)?)\\b",
         Pattern.CASE_INSENSITIVE);

   /**
    * Map from {@link #name} to {@link ActivityType}.
    */
   private static Map<String,ActivityType> allActivityTypes = new HashMap<String,ActivityType>();
   
   private String name;
   private Calendar startDate;

   public static ActivityType find( String anActivityType)
   {
      // Pick out start year (and month?), "Committee" as type (or not).  Everything successfully picked out, strip out.
      // May wind up with empty string but recognized as "Committee", so that's the type.
      // TODO Auto-generated method stub -- find/add in static set.
      return null;
   }

   /**
    * In our ACS system, our activity types sometimes imply activity start dates (e.g., "2015 Committee" vs "2017 committee").
    * @return
    */
   public Date getStartDate()
   {
      // TODO Auto-generated method stub
      return null;
   }
}
