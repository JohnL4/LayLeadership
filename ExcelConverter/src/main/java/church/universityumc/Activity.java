package church.universityumc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity a {@link ChurchMember} may engage in, recorded as {@link ActivityEngagement}.
 *
 */
public class Activity
{
   private String name;

   private static Map<String,Activity> allActivities = new HashMap<String,Activity>();
   
   private Activity( String aName)
   {
      name = aName;
   }
   
   public String getName()
   {
      return name;
   }

   /**
    * Finds the {@link Activity} having the given name in the repository of all {@link Activity}s.  If not found,
    * creates a new one, adds it to the repository, and returns it.
    * 
    * @param anActivityName
    * @return
    */
   public static Activity find( String anActivityName)
   {
      Activity retval = allActivities.get( anActivityName);
      if (retval == null)
      {
         retval = new Activity( anActivityName);
         allActivities.put( anActivityName, retval);
      }
      return retval;
   }

   /**
    * Returns all Activities that have been "found" so far, in no particular order.
    * @return
    */
   public static Collection<Activity> getAll()
   {
      return Collections.unmodifiableCollection( allActivities.values());
   }
}
