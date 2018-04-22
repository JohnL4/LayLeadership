package church.universityumc;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
   public static final String   COMMITTEE_TYPE_NAME = "Committee";
   private static final Pattern COMMITTEE_RE        = Pattern.compile( "\\bcommittees?\\b", Pattern.CASE_INSENSITIVE);

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
      String activityType;
      Integer year;
      Integer month; // 1-based
      try
      {
         ParsedDate parsedActivityType = new ParsedDate( anActivityType);
         activityType = parsedActivityType.remainder == null ? anActivityType : parsedActivityType.remainder;
         year = parsedActivityType.year;
         month = parsedActivityType.month;
      }
      catch (ParseException exc)
      {
         Log.warn( exc);
         activityType = anActivityType;
         year = month = null;
      }
      
      retval = findActivityTypeWithStartDate( activityType, year, month == null ? null : month - 1);

      return retval;
   }

   /**
    * Finds or creates the given activity and "decorates" it with the given year (month = January).
    * @param anActivityType -- May contain start date (as month name and year), which will take precedence 
    *       over anActivityStateDate (which should be null in that case, anyway). 
    * @param anActivityStartDate
    * @return
    */
   public static ActivityType find( String anActivityType, String anActivityStartDate)
   {
      ActivityType retval;

      String activityType;
      Integer year;
      Integer monthNum;
      
      ParsedDate startDateFromType;
      try
      {
         startDateFromType = new ParsedDate( anActivityType);
         activityType = startDateFromType.remainder;
      }
      catch (ParseException exc)
      {
         startDateFromType = null;
         activityType = anActivityType;
      }
      
      ParsedDate explicitStartDate;
      try
      {
         explicitStartDate = new ParsedDate( anActivityStartDate);
      }
      catch (ParseException exc)
      {
         Log.warn( exc);
         explicitStartDate = null;
      }
      
      
      if (startDateFromType == null || startDateFromType.year == null)
         year = explicitStartDate == null ? null : explicitStartDate.year;
      else
         year = startDateFromType.year;
      
      if (startDateFromType == null || startDateFromType.month == null)
      {
         if (explicitStartDate == null || explicitStartDate.month == null)
            monthNum = null;
         else
            monthNum = explicitStartDate.month - 1;
      }
      else
         monthNum = startDateFromType.month - 1;
      
      retval = findActivityTypeWithStartDate( activityType, year, monthNum);

      return retval;
   }

   /**
    * Find (or create) a previously-seen ActivityType and add a start date to it.
    * @param anActivityType
    * @param aYear - May be null
    * @param aMonthNum - 0-based, may be null
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
