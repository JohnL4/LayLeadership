package church.universityumc.excelconverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommentLevel
{
   Individual;

   private static final Pattern INDIVIDUAL_RE = Pattern.compile( "individual", Pattern.CASE_INSENSITIVE);

   public static CommentLevel forString( String aString)
   {
      Matcher matcher = INDIVIDUAL_RE.matcher( aString);
      if (matcher.matches())
         return Individual;
      else
         throw new IllegalArgumentException( String.format( "No CommentLevel for \"%s\"", aString));
   }
}
