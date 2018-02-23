package church.universityumc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A role a {@link ChurchMember} may play in an {@link Activity}.
 */
public class ActivityRole
{
   private String _name;

   private static Map<String,ActivityRole> allActivityRoles = new HashMap<String,ActivityRole>();
   
   private ActivityRole( String anActivityRoleName)
   {
      _name = anActivityRoleName;
   }

   public String getName()
   {
      return _name;
   }
   
   public static ActivityRole find( String anActivityRole)
   {
      ActivityRole retval = allActivityRoles.get(  anActivityRole);
      if (retval == null)
      {
         retval = new ActivityRole( anActivityRole);
         allActivityRoles.put( anActivityRole, retval);
      }
      return retval;
   }

   public static Collection<ActivityRole> getAll()
   {
      return Collections.unmodifiableCollection( allActivityRoles.values());
   }
}
