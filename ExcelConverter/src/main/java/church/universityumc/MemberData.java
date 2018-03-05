package church.universityumc;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * "Internal database" of church members built from AccessACS output (and possibly other sources).
 */
public class MemberData
{
   private Calendar acsRunDate = Calendar.getInstance();
   
   private Collection<ChurchMember> members = new ArrayList<ChurchMember>();

   private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance( DateFormat.SHORT);
   
   /**
    * Timestamp corresponding to when the input AccessACS data was generated.
    * @return the acsRunDate
    */
   public Date getAcsRunDate()
   {
      return acsRunDate.getTime();
   }

   /**
    * @param anAcsRunDate the acsRunDate to set, complete date/time.
    * @see #getAcsRunDate()
    */
   public void setAcsRunDate( Date anAcsRunDate)
   {
      acsRunDate.setTime( anAcsRunDate);
   }
   
   /**
    * Updates ACS run date <em>time</em> portion to match given time string. On parse failure, does nothing (except log
    * failure).
    * 
    * @param anHH_MM_AMPM_Time String in short time format (HH:MM AM/PM)
    */
   public void setAcsRunTime( String anHH_MM_AMPM_Time)
   {
      try
      {
         Date time = TIME_FORMAT.parse( anHH_MM_AMPM_Time);
         Calendar cal = Calendar.getInstance();
         cal.setTime( time); // No idea what yr-mo-da will be set and don't care
         acsRunDate.set( Calendar.HOUR_OF_DAY, cal.get( Calendar.HOUR_OF_DAY));
         acsRunDate.set( Calendar.MINUTE, cal.get( Calendar.MINUTE));
      }
      catch (ParseException exc)
      {
         Log.warn( exc);
      }
   }
   
   /**
    * @return Unmodifiable collection of members
    */
   public Collection<ChurchMember> getMembers()
   {
      return Collections.unmodifiableCollection( members);
   }
   
   /**
    * Add a member to the database.  <em>Kind</em> of an accessor.
    * @param aMember
    */
   public void addMember(ChurchMember aMember)
   {
      members.add( aMember);
   }
   
}
