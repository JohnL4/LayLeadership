package church.universityumc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ActivityType
{
   private String _name;
   
   /**
    * The year the activity starts; transient data available after a {@link #find} op, not stored as part of the {@link ActivityType}.
    */
   private Integer _startYear;
   
   /**
    * The month the activity starts; transient data available after a {@link #find} op, not stored as part of the {@link ActivityType}.
    */
   private Integer _startMonth;

   /**
    * Canonical name for a committee activity type (special case).
    */
   public static final String COMMITTEE_TYPE_NAME = "Committee";
   
   private static final Pattern YEAR_RE      = Pattern.compile( "\\d{4}");
   private static final Pattern COMMITTEE_RE = Pattern.compile( "\\bcommittees?\\b", Pattern.CASE_INSENSITIVE);
   
   private static final List<String>  MONTH_RE_STRINGS = List.of( "jan(uary)?", "feb(ruary)?", "mar(ch)?", "apr(il)?",
         "may", "june?", "july?", "aug(ust)?", "sep(t(ember)?)?", "oct(ober)?", "nov(ember)?", "dec(ember)?");
   private static final List<Pattern> MONTH_RES;
   private static final Pattern       MONTHS_RE;
         // = Pattern.compile(
         // "\\b(jan(uary)?|feb(ruary)?|mar(ch)?|apr(il)?|may|june?|july?|aug(ust)?|sep(t(ember)?)?|oct(ober)?|nov(ember)?|dec(ember)?)\\b",
         // Pattern.CASE_INSENSITIVE);

   static {
      MONTH_RES = Collections.unmodifiableList( MONTH_RE_STRINGS.stream()
            .map( s -> Pattern.compile( s, Pattern.CASE_INSENSITIVE))
            .collect( Collectors.toList()));
      String concatenatedREs = MONTH_RE_STRINGS.stream().collect( Collectors.joining( "|")); 
      MONTHS_RE = Pattern.compile( "\\b(" + concatenatedREs.toString() + ")\\b", Pattern.CASE_INSENSITIVE);
   }
   
   /**
    * Map from {@link #_name} to {@link ActivityType}, in-memory repository.
    */
   private static Map<String,ActivityType> allActivityTypes = new HashMap<String,ActivityType>();
   
   private ActivityType(String anActivityType) {
      _name = anActivityType;
   }
   
   public String getName() { return _name; }
   
   /**
    * The year the activity starts; transient data available after a {@link #find} op, not stored as part of the {@link ActivityType}.
    * @return the startYear
    */
   public Integer getStartYear()
   {
      return _startYear;
   }

   /**
    * @param startYear the startYear to set
    */
   public void setStartYear( Integer startYear)
   {
      this._startYear = startYear;
   }

   /**
    * The (0-based) month the activity starts; transient data available after a {@link #find} op, not stored as part of
    * the {@link ActivityType}.
    * 
    * @return the startMonth
    */
   public Integer getStartMonth()
   {
      return _startMonth;
   }

   /**
    * @param startMonth the startMonth to set
    */
   public void setStartMonth( Integer startMonth)
   {
      this._startMonth = startMonth;
   }

   /**
    * Parses given string into a true activity type and a start date for the activity. Also updates repository of known
    * activity types ({@link #allActivityTypes}).
    * <p>
    * Start date is not a part of the activity type, but, because of the way we store data in ACS, may be part of the
    * activity type specification. It will not be stored; this data must be transferred into an
    * {@link ActivityEngagement} object.
    * </p>
    * 
    * @param anActivityType
    * @return {@link ActivityType} with start year and month set from input string.
    */
   public static ActivityType find( String anActivityType)
   {
      ActivityType retval;
      String activityType = anActivityType;
      Integer year;
      
      Matcher yrMatcher = YEAR_RE.matcher( activityType);
      if (yrMatcher.find()) 
      {
         year = Integer.parseInt( yrMatcher.group());
         activityType = yrMatcher.replaceFirst( "");
      }
      else
         year = null;
      
      String month;
      Integer monthNum;
      Matcher monthMatcher = MONTHS_RE.matcher( activityType);
      if (monthMatcher.find())
      {
         month = monthMatcher.group();
         activityType = monthMatcher.replaceFirst( "");
         monthNum = 0;
         for (Pattern monthRE : MONTH_RES)
         {
            if (monthRE.matcher( month).matches())
               break;
            else
               monthNum++;
         }
      }
      else
         monthNum = null;
      
      retval = findActivityTypeWithStartDate( activityType, year, monthNum);

      return retval;
   }

   /**
    * Finds or creates the given activity and "decorates" it with the given year (month = January).
    * @param anActivityType
    * @param anActivityStartYear
    * @return
    */
   public static ActivityType find( String anActivityType, String anActivityStartYear)
   {
      ActivityType retval;

      Integer year = Integer.parseInt( anActivityStartYear);
      Integer monthNum = null;
      
      retval = findActivityTypeWithStartDate( anActivityType, year, monthNum);

      return retval;
   }

   /**
    * Find (or create) a previously-seen ActivityType and add a start date to it.
    * @param anActivityType
    * @param aYear - May be null
    * @param aMonthNum - May be null
    * @return
    */
   private static ActivityType findActivityTypeWithStartDate( String anActivityType, Integer aYear, Integer aMonthNum)
   {
      ActivityType retval;
      anActivityType = anActivityType.trim();
      Matcher committeeMatcher = COMMITTEE_RE.matcher( anActivityType);
      if (committeeMatcher.matches()) // Scan entire string for match, not part (we want a more-or-less exact match).
         anActivityType = COMMITTEE_TYPE_NAME;
      
      retval = allActivityTypes.get( anActivityType);
      if (retval == null)
      {
         retval = new ActivityType( anActivityType);
         allActivityTypes.put( anActivityType, retval);
      }
      retval._startYear = aYear;
      retval._startMonth = aMonthNum;
      return retval;
   }

   public static Collection<ActivityType> getAll()
   {
      return Collections.unmodifiableCollection( allActivityTypes.values());
   }
}
