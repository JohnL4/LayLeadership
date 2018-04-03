package church.universityumc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommentLevel
{
   /**
    * The comments applies to an individual (probably the current {@link ChurchMember}).
    */
   Individual,
   
   /**
    * Comment pertains to the member's entire family.  This may be a comment entered in for one member, but
    * describing other family members.
    */
   Family,
   
   /**
    * The level at which this comment applies is unknown.
    */
   Unknown;

   private static final Pattern INDIVIDUAL_RE = Pattern.compile( "individual", Pattern.CASE_INSENSITIVE);

   /**
    * Returns the enum constant corresponding to the given string (which may or may not match the enum member name).
    * @param aString
    * @return
    */
   public static CommentLevel forString( String aString)
   {
      Matcher matcher = INDIVIDUAL_RE.matcher( aString);
      if (matcher.matches())
         return Individual;
      else
      {
         Log.warn( "Unknown comment level for \"%s\"", aString);
         return Unknown;
      }
   }
}
