package church.universityumc;

import java.util.Calendar;
import java.util.Date;

/**
 * An activity a {@link ChurchMember} may engage in.
 *
 */
public class ActivityEngagement
{
   private ActivityType activityType;
   private Activity activity;
   private ActivityRole role;
   private Date startDate, endDate;
   private boolean noRotationDate;
   
   public ActivityEngagement( String anActivityType, 
         String anActivityName, 
         String anActivityRole, 
         Date aStartDate,
         Date anEndDate, 
         boolean aNoRotationDateFlag)
   {
      activityType = ActivityType.find( anActivityType);
      activity = Activity.find( anActivityName);
      role = ActivityRole.find( anActivityRole);
      if (aStartDate == null)
      {
         if (activityType.getStartYear() == null)
            startDate = null;
         else
         {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set( Calendar.YEAR, activityType.getStartYear());
            if (activityType.getStartMonth() != null)
               cal.set( Calendar.MONTH, activityType.getStartMonth());
            startDate = cal.getTime();
         }
      }
      else
         startDate = aStartDate;
      endDate = anEndDate; // May be null, but if so, there's nothing we can do about it.
      noRotationDate = aNoRotationDateFlag; 
   }
}
