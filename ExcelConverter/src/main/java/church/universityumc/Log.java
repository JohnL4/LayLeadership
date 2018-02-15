package church.universityumc;

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
   
   public static void warn(String aFormat,  Object ... args)
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
      System.getLogger( LOGGER_NAME).log( Level.WARNING, String.format( fmt.toString(), args));
   }

//   public static Log getInstance()
//   {
//      if (instance == null)
//         instance = new Log();
//      return instance;
//   }
}
