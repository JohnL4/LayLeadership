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

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "Activity [name=" + name + "]";
   }

   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals( Object obj)
   {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Activity other = (Activity) obj;
      if (name == null)
      {
         if (other.name != null) return false;
      }
      else if (!name.equals( other.name)) return false;
      return true;
   }
   
   
}
