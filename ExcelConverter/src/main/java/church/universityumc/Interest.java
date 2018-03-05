package church.universityumc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An interest (in service or whatever) that a {@link ChurchMember} may have.
 * TODO: Maybe we need a MemberInterest class that also includes an interest-source (e.g., "Survey2016").
 * @author john
 *
 */
public class Interest
{
   private String name;
   
   private static Map<String,Interest> allInterests= new HashMap<String,Interest>();

   private Interest( String aName)
   {
      name = aName;
   }
   
   /**
    * The name of the Interest.
    * @return the name
    */
   public String getName()
   {
      return name;
   }
   
   public static Interest find( String anInterestName)
   {
      Interest retval = allInterests.get( anInterestName);
      if (retval == null)
      {
         retval = new Interest( anInterestName);
         allInterests.put( anInterestName, retval);
      }
      return retval;
   }
   
   public String toString()
   {
      return getName();
   }

   public static Collection<Interest> getAll()
   {
      return Collections.unmodifiableCollection( allInterests.values());
   }
   
}
