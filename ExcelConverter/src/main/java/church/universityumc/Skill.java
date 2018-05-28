package church.universityumc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A skill a {@link ChurchMember} may have (may or may not coincide with {@link Interest}).
 * @author john
 *
 */
public class Skill
{
   private String name;
   private InfoSource source;
   
   /**
    * This stupid-looking map is so we can have one object representing each (name,source) tuple. Eventually, we expect
    * this to go into its own database table (for no particularly good reason other than to have normalized data), and
    * each object will probably get its own d/b id.
    */
   private static Map<Skill,Skill> allSkills = new HashMap<Skill,Skill>();

   private Skill( String aName, InfoSource aSource)
   {
      name = aName;
      source = aSource;
   }

   /**
    * The name of the skill.
    * @return the name
    */
   public String getName()
   {
      return name;
   }
   
   /**
    * The source of information in which the skill is defined.
    * @return
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
      Skill other = (Skill) obj;
      if (name == null)
      {
         if (other.name != null) return false;
      }
      else if (!name.equals( other.name)) return false;
      if (source != other.source) return false;
      return true;
   }
   
   /**
    * Find a Skill having the given properties, making one, if necessary.
    * @param aName
    * @param aSource
    */
   public static Skill find( String aName, InfoSource aSource)
   {
      Skill tempSkill = new Skill( aName, aSource);
      Skill retval = allSkills.get( aName);
      if (retval == null)
      {
         allSkills.put( tempSkill, tempSkill);
         retval = tempSkill;
      }
      return retval;
   }
   
   public String toString()
   {
      return getName();
   }

   public static Collection<Skill> getAll()
   {
      return Collections.unmodifiableCollection( allSkills.values());
   }
   
   
}
