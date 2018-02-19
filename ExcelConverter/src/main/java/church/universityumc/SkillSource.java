package church.universityumc;

/**
 * The source of info on a {@link Skill}.
 *
 */
public enum SkillSource
{
   /**
    * The voluntary "Personal Ministry Survey" that was conducted in 2006.
    */
   PersonalMinistrySurvey2006, 
   
   /**
    * Skill derived from an {@link ActivityEngagement} with no start date.  An engagement with no dates isn't
    * really an engagement.
    */
   UndatedEngagement;
}
