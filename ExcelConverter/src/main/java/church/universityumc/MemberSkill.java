package church.universityumc;

/**
 * A {@link Skill} a particular {@link ChurchMember} has.
 *
 */
public class MemberSkill
{
   private Skill _skill;
   private SkillSource _source;
   
   public MemberSkill( String aSkillName, SkillSource aSkillSource)
   {
      _skill = Skill.find( aSkillName);
      _source = aSkillSource;
   }
   
   public MemberSkill( Skill aSkill, SkillSource aSkillSource)
   {
      _skill = aSkill;
      _source = aSkillSource;
   }
   
   /**
    * @return the skill
    */
   public Skill getSkill()
   {
      return _skill;
   }

   /**
    * @return the source
    */
   public SkillSource getSource()
   {
      return _source;
   }

   public String toString()
   {
      return String.format( "%s (%s)", _skill, _source);
   }
}
