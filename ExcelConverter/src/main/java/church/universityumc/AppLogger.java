package church.universityumc;

import java.lang.StackWalker.StackFrame;
import java.lang.System.Logger.Level;
import java.util.Optional;

/**
 * For generating log messages, with context.
 *
 */
public class AppLogger
{
   private static final String LOGGER_NAME = "church.universityumc";

   private static AppLogger instance;
   
   private ChurchMember member;
   
   private Integer row;

   /**
    * Sets the current {@link ChurchMember} being processed.
    * 
    * @param member the member to set
    */
   public void setMember( ChurchMember member)
   {
      this.member = member;
   }

   /**
    * Sets the current input row (0-based) being processed.
    * 
    * @param row the row to set
    */
   public void setRow( Integer row)
   {
      this.row = row;
   }
   
   public void warn(String aFormat,  Object ... args)
   {
      StringBuilder fmt = new StringBuilder( aFormat);

      if (row != null)
         fmt.append( " at data row ").append( row);
      
      Optional<StackFrame> caller = StackWalker.getInstance().walk( stream -> stream.skip(1).findFirst());
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
      System.getLogger( LOGGER_NAME).log( Level.WARNING, fmt.toString(), args);
   }

   public static AppLogger getInstance()
   {
      if (instance == null)
         instance = new AppLogger();
      return instance;
   }
}
