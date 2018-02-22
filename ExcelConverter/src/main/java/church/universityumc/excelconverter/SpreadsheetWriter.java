package church.universityumc.excelconverter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import church.universityumc.ActivityEngagement;
import church.universityumc.ChurchMember;
import church.universityumc.Comment;
import church.universityumc.Log;

/**
 * Writes internal data to a spreadsheet.
 */
public class SpreadsheetWriter
{
   public void dumpToExcelFile( Collection<ChurchMember> aChurchMembersColl, String anOutfileName)
         throws IOException
   {
      Workbook workbook = new XSSFWorkbook();
      Sheet membersSheet = workbook.createSheet( WorkbookUtil.createSafeSheetName( "Members"));
      
      createRow( membersSheet, new String[] {"Name", "Age", "Phone", "Email", "Date Joined"}, EnumSet.of( FontStyle.Bold));
      
      for (ChurchMember member : aChurchMembersColl)
      {
         createMemberDetailRow( membersSheet, member);
      }
      
      Sheet activitiesSheet = workbook.createSheet( WorkbookUtil.createSafeSheetName( "Activities and Comments"));
      createRow( activitiesSheet, 
            new String[] 
                  { 
                        "Member Name", 
                        "Activity Type", // Could be "Comment" 
                        "Start Date", // Could be date of Comment 
                        "End Date",
                        "Role", // Comment level 
                        "Activity, Skill or Comment" 
                  }
            , null);
   
      // Iterate through members again, writing member name and activities and comments
      for (ChurchMember member : aChurchMembersColl)
      {
         if (member.getServiceHistory() == null) {}
         else
         {
            for (ActivityEngagement activityEngagement : member.getServiceHistory())
            {
               createActivityDetailRow(  activitiesSheet, member, activityEngagement);
            }
         }
         if (member.getComments() == null) {}
         else
         {
            for (Comment comment : member.getComments())
            {
               createCommentDetailRow( activitiesSheet, member, comment);
            }
         }
      }
      
      // Create "Other Data" sheet
      // Write headers (Activities, Activity Types, Roles, Skill Categories, Skill Subcategories, Skill subsubcategories,
      // Comment levels, Comment types)
      // Iterate through each of the above classes of supporting info and write them vertically.
      
      // Write workbook to file.
      FileOutputStream out = new FileOutputStream( anOutfileName);
      workbook.write( out);
      out.close();
   }
   
   /**
    * Creates a row after existing rows and writes the given strings to it, using the given font styles.
    * @param aSheet
    * @param aStringv
    * @param aFontStyleSet TODO: This should be a true CellStyle, not a bunch of flags for bold, italic.
    * @return the created row
    */
   private  Row createRow( Sheet aSheet, String[] aStringv, EnumSet<FontStyle> aFontStyleSet)
   {
      Log.setMember( null);
      Log.setRow( -1);
      
      final EnumSet<FontStyle> specialStyles = EnumSet.of( FontStyle.Bold, FontStyle.Italic);
      
      EnumSet<FontStyle> fontStyles = aFontStyleSet;
      if (fontStyles == null) {}
      else
      {
         fontStyles.retainAll( specialStyles);
         if (fontStyles.size() > 0)
            Log.warn( "Special styles unimplemented");
      }
      int maxRow = aSheet.getLastRowNum();
      Row row = aSheet.createRow( maxRow+1);
      int colNum = 0;
      for (String cellValue : aStringv)
      {
         row.createCell( colNum).setCellValue( cellValue);
         colNum++;
      }
      return row;
   }
   
   private  Row createMemberDetailRow( Sheet aSheet, ChurchMember aMember)
   {
      Row row = createRow( aSheet, new String[] {aMember.getName()}, null);
      Cell cell;
      
      // TODO: dates etc.
      cell = row.createCell( row.getLastCellNum(), CellType.NUMERIC);
      cell.setCellValue( aMember.getAge());
      
      cell = row.createCell( row.getLastCellNum());
      cell.setCellValue( aMember.getPhone());
      
      cell = row.createCell( row.getLastCellNum());
      cell.setCellValue( aMember.getEmail());
      
      cell = row.createCell( row.getLastCellNum(), CellType.NUMERIC); // Also should work for full dates.
      cell.setCellValue( aMember.getYearJoined()); // TODO: this really should be a date, because we get full dates on input.
      // TODO: final style for date formatting (one style for all these join dates)
      
      return row;
   }

   private  Row createActivityDetailRow( Sheet aSheet, ChurchMember aMember, ActivityEngagement anActivityEngagement)
   {
      Date startDate = anActivityEngagement.getStartDate();
      Date endDate = anActivityEngagement.hasRotationDate() ? anActivityEngagement.getEndDate() : null;
      Row row = createRow( aSheet, 
            new String[] 
                  {
                        aMember.getName(),
                        anActivityEngagement.getActivityType().getName(),
                        startDate == null ? "" : startDate.toString(),
                        endDate == null ? "" : endDate.toString(),
                        anActivityEngagement.getRole().getName(),
                        anActivityEngagement.getActivity().getName()
                  }, 
            null);
      return row;
   }

   private  Row createCommentDetailRow( Sheet aSheet, ChurchMember aMember, Comment aComment)
   {
      // TODO Auto-generated method stub
      return null;
   }
}
