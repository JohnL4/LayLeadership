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
}
