package church.universityumc.excelconverter;

/**
 * Thrown when the Excel converter is unable to determine what kind of row it's looking at.
 */
public class UnknownRowTypeException extends Exception
{
   private int rowNum;
   
   /**
    * 
    * @param aRowNum - The number (0-based) of the row that is unrecognizable. 
    */
   public UnknownRowTypeException( int aRowNum)
   {
      super();
      rowNum = aRowNum;
   }
   
   public String getMessage()
   {
      return String.format( "Unknown row type at data row %d", rowNum + 1);
   }
}
