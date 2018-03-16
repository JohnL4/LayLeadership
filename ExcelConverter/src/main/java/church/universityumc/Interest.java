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
   private InfoSource source;
   
   /**
    * Similar to {@link Skill#allSkills}.
    */
   private static Map<Interest,Interest> allInterests= new HashMap<Interest,Interest>();

   private Interest( String aName, InfoSource aSource)
   {
      name = aName;
      source = aSource;
   }
   
   /**
    * The name of the Interest.
    * @return the name
    */
   public String getName()
   {
      return name;
   }
   
   /**
    * Similar to {@link Skill#getSource()}.
    */
   public InfoSource getSource()
   {
      return source;
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
      result = prime * result + ((source == null) ? 0 : source.hashCode());
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
      Interest other = (Interest) obj;
      if (name == null)
      {
         if (other.name != null) return false;
      }
      else if (!name.equals( other.name)) return false;
      if (source != other.source) return false;
      return true;
   }

   public static Interest find( String aName, InfoSource aSource)
   {
      Interest tempInterest = new Interest( aName, aSource);
      Interest retval = allInterests.get( tempInterest);
      if (retval == null)
      {
         allInterests.put( tempInterest, tempInterest);
         retval = tempInterest;
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
