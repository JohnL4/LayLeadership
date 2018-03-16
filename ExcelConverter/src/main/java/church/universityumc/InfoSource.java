package church.universityumc;

/**
 * Where a piece of information came from.
 */
public enum InfoSource
{
   /**
    * The voluntary "Personal Ministry Survey" that was conducted in 2006.
    */
   PersonalMinistrySurvey2006, 
   
   /**
    * Skill derived from an {@link ActivityEngagement} with no start date.  An engagement with no dates isn't
    * really an engagement.
    */
   UndatedEngagement,
   
   /**
    * Survey Monkey survey conducted in 2016.
    */
   Survey2016, 
   
   /**
    * From AccessACS "Comments" section.
    */
   AcsComment;
}
