package church.universityumc;

/**
 * A {@link Skill} a particular {@link ChurchMember} has.
 *
 */
public class MemberSkill
{
   private Skill skill;
   private SkillSource source;
   
   public MemberSkill( String aSkillName, SkillSource aSkillSource)
   {
      skill = Skill.find( aSkillName);
      source = aSkillSource;
   }
   
   public MemberSkill( Skill aSkill, SkillSource aSkillSource)
   {
      skill = aSkill;
      source = aSkillSource;
   }
   
   /**
    * @return the skill
    */
   public Skill getSkill()
   {
      return skill;
   }

   /**
    * @return the source
    */
   public SkillSource getSource()
   {
      return source;
   }

   public String toString()
   {
      return String.format( "%s (%s)", skill, source);
   }
}
