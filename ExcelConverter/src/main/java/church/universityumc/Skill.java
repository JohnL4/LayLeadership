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
   private String name;
   
   private static Map<String,Skill> allSkills = new HashMap<String,Skill>();

   public Skill( String aName)
   {
      this.name = aName;
   }

   /**
    * The name of the skill.
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   public static Skill find( String aSkillname)
   {
      // TODO Auto-generated method stub
      return null;
   }
   
   
}
