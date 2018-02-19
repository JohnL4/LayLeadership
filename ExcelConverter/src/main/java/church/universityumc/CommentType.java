package church.universityumc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommentType
{
   Biography;

   private static final Pattern BIOGRAPHICAL_RE = Pattern.compile( "biography", Pattern.CASE_INSENSITIVE);

   /**
    * Returns the enum constant corresponding to the given string (which may or may not match the enum member name).
    * 
    * @param aString
    * @return
    * @throws EnumResolutionException If the given string cannot be mapped to an enum member.
    */
   public static CommentType forString( String aString) throws EnumResolutionException
   {
      Matcher matcher = BIOGRAPHICAL_RE.matcher( aString);
      if (matcher.matches())
         return Biography;
      else
         throw new EnumResolutionException( String.format( "No CommentType for \"%s\"", aString));
   }
}
