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

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "ActivityRole [_name=" + _name + "]";
   }

   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
      ActivityRole other = (ActivityRole) obj;
      if (_name == null)
      {
         if (other._name != null) return false;
      }
      else if (!_name.equals( other._name)) return false;
      return true;
   }
   
   
}
