package church.universityumc;

import java.text.ParseException;
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

   /**
    * Comment levels (types) recognized.
    */
   private static final String INDIVIDUAL = "individual", FAMILY = "family";
   
   private static final Pattern COMMENT_LEVEL_RE = Pattern.compile( INDIVIDUAL + "|" + FAMILY,
         Pattern.CASE_INSENSITIVE);

   /**
    * Returns the enum constant corresponding to the given string (which may or may not match the enum member name).
    * @param aString
    * @return
    */
   public static CommentLevel forString( String aString)
   {
      CommentLevel retval;
      Matcher matcher = COMMENT_LEVEL_RE.matcher( aString);
      if (matcher.matches())
      {
            if (matcher.group().equalsIgnoreCase( INDIVIDUAL))
               retval = Individual;
            else if (matcher.group().equalsIgnoreCase( FAMILY))
               retval = Family;
            else
            {
               Log.error( new ParseException(
                     String.format( "Regex matched unexpected value in \"%s\": \"%s\"", aString, matcher.group()),
                     matcher.start()));
               retval = Unknown;
            }
      }
      else
      {
         Log.warn( "Unknown comment level for \"%s\"", aString);
         retval = Unknown;
      }
      return retval;
   }
}
