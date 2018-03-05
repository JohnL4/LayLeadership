package church.universityumc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A skill a {@link ChurchMember} may have (may or may not coincide with {@link Interest}).
 * @author john
 *
 */
public class Skill
{
   private String name;
   
   private static Map<String,Skill> allSkills = new HashMap<String,Skill>();

   private Skill( String aName /* , SkillSource aSource */)
   {
      name = aName;
   }

   /**
    * The name of the skill.
    * @return the name
    */
   public String getName()
   {
      return name;
   }
   
   public static Skill find( String aSkillName)
   {
      Skill retval = allSkills.get( aSkillName);
      if (retval == null)
      {
         retval = new Skill( aSkillName);
         allSkills.put( aSkillName, retval);
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
