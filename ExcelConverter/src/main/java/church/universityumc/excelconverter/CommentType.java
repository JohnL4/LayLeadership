package church.universityumc.excelconverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommentType
{
   Biography;

   private static final Pattern BIOGRAPHICAL_RE = Pattern.compile( "biography", Pattern.CASE_INSENSITIVE);

   public static CommentType forString( String aString)
   {
      Matcher matcher = BIOGRAPHICAL_RE.matcher( aString);
      if (matcher.matches())
         return Biography;
      else
         throw new IllegalArgumentException( String.format( "No CommentType for \"%s\"", aString));
   }
}
