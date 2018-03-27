package church.universityumc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.System.Logger.Level;
import java.util.Optional;

/**
 * For generating log messages, with context.
 *
 */
public class Log
{
   private static final String LOGGER_NAME = "church.universityumc";

//   private static Log instance;
   
   private static ChurchMember member;
   
   private static Integer row;

   private static String EOL = System.getProperty( "line.separator");
   /**
    * Sets the current {@link ChurchMember} being processed.
    * 
    * @param aMember the member to set
    */
   public static void setMember( ChurchMember aMember)
   {
      member = aMember;
   }

   /**
    * Sets the current input row (0-based) being processed. Note that this could be a spreadsheet row or a d/b result
    * set row.
    * 
    * @param aRow
    *           the row to set
    */
   public static void setRow( Integer aRow)
   {
      row = aRow;
   }
   
   public static void warn(String aFormat,  Object... args)
   {
      StringBuilder fmt = decorateMessage( aFormat);
      System.getLogger( LOGGER_NAME).log( Level.WARNING, String.format( fmt.toString(), args));
   }

   /**
    * Warns with only the given exception's message (or result of {@link Exception#toString()} if message is null).
    * 
    * @param anException
    */
   public static void warn( Exception anException)
   {
      String msg = anException.getMessage();
      if (msg == null)
         msg = anException.toString();
      warn( msg);
   }

   public static void debug( String aFormat, Object... args)
   {
      StringBuilder fmt = decorateMessage( aFormat);
      System.getLogger( LOGGER_NAME).log( Level.DEBUG, String.format( fmt.toString(), args));
   }
   
   public static void debug( Exception anException)
   {
      StringBuilder msgSB = new StringBuilder();
      Throwable throwableWalker = anException;
      while (throwableWalker != null)
      {
         if (throwableWalker.getMessage() == null)
            msgSB.append( throwableWalker.toString());
         else
            msgSB.append( throwableWalker.getMessage());
         msgSB.append( EOL);
         StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter( sw);
         throwableWalker.printStackTrace( pw);
         pw.close();
         msgSB.append( sw.toString());
         throwableWalker = throwableWalker.getCause();
         if (throwableWalker != null) msgSB.append( String.format( "%n----------------  caused by  ----------------%n"));
      }
      debug( msgSB.toString());
   }

   /**
    * Decorates the given message with contextual info.
    * @param aFormat
    * @return Decorated message
    */
   private static StringBuilder decorateMessage( String aFormat)
   {
      StringBuilder fmt = new StringBuilder( aFormat == null ? "(no message)" : aFormat);

      if (row != null)
         fmt.append( " at data row ").append( row + 1);
      
      Optional<StackFrame> caller = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).walk( 
            stream -> stream
            .dropWhile( sf -> sf.getDeclaringClass().equals( Log.class))
            .findFirst());
      if (caller.isPresent())
      {
         StackFrame callerSF = caller.get();
         fmt.append( " (")
            .append( callerSF.getMethodName())
            .append( "(), ")
            .append( callerSF.getFileName())
            .append(  ":")
            .append(  callerSF.getLineNumber())
            .append( ")");
      }
      return fmt;
   }

}
