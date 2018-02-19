package church.universityumc;

import java.util.HashMap;
import java.util.Map;

/**
 * A role a {@link ChurchMember} may play in an {@link Activity}.
 */
public class ActivityRole
{
   private String _name;

   private static Map<String,ActivityRole> __allActivityRoles = new HashMap<String,ActivityRole>();
   
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
      ActivityRole retval = __allActivityRoles.get(  anActivityRole);
      if (retval == null)
      {
         retval = new ActivityRole( anActivityRole);
         __allActivityRoles.put( anActivityRole, retval);
      }
      return retval;
   }
}
