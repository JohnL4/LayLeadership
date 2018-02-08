package church.universityumc.excelconverter;

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
    * A row describing a single activity.
    */
   Activity;
}
