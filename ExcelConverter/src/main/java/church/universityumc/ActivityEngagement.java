package church.universityumc;

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
         startDate = activityType.getStartDate();
      else
         startDate = aStartDate;
      endDate = anEndDate; // May be null, but if so, there's nothing we can do about it.
      noRotationDate = aNoRotationDateFlag; 
   }
}
