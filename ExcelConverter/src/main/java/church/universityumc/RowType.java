package church.universityumc;

public enum RowType
{
   /**
    * Type unknown or indeterminate or "don't care".
    */
   None,
   
   /**
    * Report formatting output (date, time, organization, rpt name, page number)
    */
   PageHeader,
   
   /**
    * Column headers for a {@link ChurchMember}.
    */
   MemberHeader,
   
   /**
    * Row is the details of a {@link ChurchMember} (age, join date, phone, email, etc.).
    */
   Member,
   
   /**
    * Marks the beginning of a "COMMENTS" section.
    */
   CommentsSectionMarker, 
   
   /**
    * Row is a "Comments" row, hopefully containing biographical info about the {@link ChurchMember}.
    */
   Comments, 
   
   /**
    * Marks the beginning of an "ACTIVITIES" section.
    */
   ActivitiesSectionMarker, 
   
   /**
    * Column headers for an Activities subsection of a {@link ChurchMember}.
    */
   ActivitiesHeader,
   
   /**
    * A row describing a single activity.  May be a {@link #Skill} in disguise (lack of dates may be the
    * discriminant).
    */
   Activity, 
   
   /**
    * A row describing a member's vocation.  Note that a row may be recognized as an {@link #Activity}, but actually
    * may be a {@link #Vocation}.
    */
   Vocation, // TODO: figure out if these things ("Persnl Minstr 06 Vocat'l") really are vocations.
   
   /**
    * A skill, as opposed to an {@link #Activity} (which has at least a start date) or a {@link #Vocation}
    * (which might somehow explicitly identified as such -- so {@link #Skill} is kind of a fallback for
    * {@link #Activity}s with no dates.
    */
   Skill,
   
   /**
    * A row summarizing the report somehow (e.g., by printing a total record count).
    */
   ReportSummary, 
   
   /**
    * An empty row (no data).
    */
   EmptyRow;
}
