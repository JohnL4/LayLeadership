package church.universityumc.excelconverter;

/**
 * Dumb tuple describing number of rows and columns required by some operation in a spreadsheet. 
 */
public class RowColumnDelta
{
   int numRows;
   
   int numColumns;
   
   public RowColumnDelta( int aNumRows, int aNumColumns)
   {
      numRows = aNumRows;
      numColumns = aNumColumns;
   }
}
