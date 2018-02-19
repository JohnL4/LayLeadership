package church.universityumc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommentLevel
{
   Individual;

   private static final Pattern INDIVIDUAL_RE = Pattern.compile( "individual", Pattern.CASE_INSENSITIVE);

   /**
    * Returns the enum constant corresponding to the given string (which may or may not match the enum member name).
    * @param aString
    * @return
    * @throws EnumResolutionException If the given string cannot be mapped to an enum member.
    */
   public static CommentLevel forString( String aString) throws EnumResolutionException
   {
      Matcher matcher = INDIVIDUAL_RE.matcher( aString);
      if (matcher.matches())
         return Individual;
      else
         throw new EnumResolutionException( String.format( "No CommentLevel for \"%s\"", aString));
   }
}
