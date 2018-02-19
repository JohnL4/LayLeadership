package church.universityumc;

import java.util.HashMap;
import java.util.Map;

/**
 * A skill a {@link ChurchMember} may have (may or may not coincide with {@link Interest}).
 * @author john
 *
 */
public class Skill
{
   private String _name;
   
   private static Map<String,Skill> __allSkills = new HashMap<String,Skill>();

   public Skill( String aName /* , SkillSource aSource */)
   {
      _name = aName;
   }

   /**
    * The name of the skill.
    * @return the name
    */
   public String getName()
   {
      return _name;
   }
   
   public static Skill find( String aSkillName)
   {
      Skill retval = __allSkills.get( aSkillName);
      if (retval == null)
      {
         retval = new Skill( aSkillName);
         __allSkills.put( aSkillName, retval);
      }
      return retval;
   }
   
   public String toString()
   {
      return getName();
   }
}
