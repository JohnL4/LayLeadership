package church.universityumc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommentType
{
   /**
    * The comment is a biographical note.
    */
   Biography,
   
   /**
    * It is unknown what kind of comment this is.
    */
   Unknown;

   private static final Pattern BIOGRAPHICAL_RE = Pattern.compile( "biography", Pattern.CASE_INSENSITIVE);

   /**
    * Returns the enum constant corresponding to the given string (which may or may not match the enum member name).
    * 
    * @param aString
    * @return
    */
   public static CommentType forString( String aString)
   {
      Matcher matcher = BIOGRAPHICAL_RE.matcher( aString);
      if (matcher.matches())
         return Biography;
      else
      {
         Log.warn( "Unkown comment type for \"%s\"", aString);
         return Unknown;
      }
   }
}
