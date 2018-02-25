package church.universityumc.excelconverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.function.Consumer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import church.universityumc.Activity;
import church.universityumc.ActivityEngagement;
import church.universityumc.ActivityRole;
import church.universityumc.ActivityType;
import church.universityumc.ChurchMember;
import church.universityumc.Comment;
import church.universityumc.Log;
import church.universityumc.MemberSkill;
import church.universityumc.Skill;

/**
 * Writes internal data to a spreadsheet.
 */
public class SpreadsheetWriter
{
   /**
    * The style to use for all dates in the workbook.
    */
   private CellStyle dateStyle;
   
   /**
    * The style to be applied to header rows.
    */
   private CellStyle headerStyle;
   
   /**
    * The bold-face font for the workbook.
    */
   private Font boldFont;

   public void dumpToExcelFile( Collection<ChurchMember> aChurchMembersColl, String anOutfileName)
         throws IOException
   {
      try (Workbook workbook = new XSSFWorkbook())
      {
         CreationHelper creationHelper = workbook.getCreationHelper();
         dateStyle = workbook.createCellStyle();
         dateStyle.setDataFormat( creationHelper.createDataFormat().getFormat( "M/D/YYYY"));

         boldFont = workbook.createFont();
         boldFont.setBold( true);
         headerStyle = workbook.createCellStyle();
         headerStyle.setFont( boldFont);
         headerStyle.setAlignment( HorizontalAlignment.CENTER);
         
         Sheet activitiesSheet = workbook.createSheet( WorkbookUtil.createSafeSheetName( "Filter"));
         writeFilterSheet( activitiesSheet, aChurchMembersColl);

         Sheet membersSheet = workbook.createSheet( WorkbookUtil.createSafeSheetName( "Member Info"));
         writeMembersSheet( membersSheet, aChurchMembersColl);

         Sheet dataSheet = workbook.createSheet( WorkbookUtil.createSafeSheetName( "Supporting Data"));
         writeDataSheet( dataSheet);

         File outFile = new File( anOutfileName);
         if (outFile.exists())
         {
            Log.debug( "Deleting file \"%s\" before writing", anOutfileName);
            outFile.delete();
         }
         FileOutputStream out = new FileOutputStream( outFile);
         workbook.write( out);
         out.close();
      }
   }

   private void writeMembersSheet( Sheet aMembersSheet, Collection<ChurchMember> aChurchMembersColl)
   {
      String[] memberHeaders = new String[] {"Name", "Phone", "Email"}; 
      Row row = createRow( aMembersSheet, memberHeaders, EnumSet.of( FontStyle.Bold));
      row.setRowStyle( headerStyle);
      
      for (ChurchMember member : aChurchMembersColl)
      {
         createMemberDetailRow( aMembersSheet, member);
      }
      for (int i = 0; i < memberHeaders.length; i++)
      {
         aMembersSheet.autoSizeColumn( i);
      }
   }
   
   /**
    * Writes the data that's meant to be "filterable", for searching for members that meet certain criteria.
    * Examples are: have a certain skill, are in a certain age range, recently joined the church, etc.
    * 
    * @param aSheet
    * @param aChurchMembersColl
    */
   private void writeFilterSheet( Sheet aSheet, Collection<ChurchMember> aChurchMembersColl)
   {
      String[] activitiesHeaders = new String[] 
            { 
                  "Member Name", 
                  "Age",
                  "Date Joined",
                  "Activity Type", // Could be "Comment" 
                  "Start Date", // Could be date of Comment 
                  "End Date",
                  "Role", // Comment level 
                  "Activity, Skill or Comment" 
            }; 
      
      Row headerRow = createRow( aSheet, activitiesHeaders, null);
      headerRow.setRowStyle( headerStyle);

      for (ChurchMember member : aChurchMembersColl)
      {
         if (member.getServiceHistory() == null) {}
         else
         {
            for (ActivityEngagement activityEngagement : member.getServiceHistory())
            {
               Row row = createActivityPrefixRow( aSheet, member);
               appendActivityDetailRow(  row, member, activityEngagement);
            }
         }
         if (member.getSkills() == null) {}
         else
         {
            for (MemberSkill skill : member.getSkills())
            {
               Row row = createActivityPrefixRow( aSheet, member);
               appendSkillRow( row, member, skill);
            }
         }
         if (member.getComments() == null) {}
         else
         {
            for (Comment comment : member.getComments())
            {
               Row row = createActivityPrefixRow( aSheet, member);
               appendCommentDetailRow( row, member, comment);
            }
         }
      }
      for (int i = 0; i < activitiesHeaders.length; i++)
      {
         aSheet.autoSizeColumn( i);
      }
   }

   /**
    * Write the supporting-data sheet.  In columns: {@link Activity}s, {@link ActivityType}s, 
    * {@link ActivityRole}s, {@link Skill}s.
    * @param aSheet
    */
   private void writeDataSheet( Sheet aSheet)
   {
      int colNum = 0;

      writeActivityDataColumn( aSheet, 0, colNum++);
      writeActivityTypeDataColumn( aSheet, 0, colNum++);
      writeActivityRoleDataColumn( aSheet, 0, colNum++);
      writeSkillDataColumn( aSheet, 0, colNum++);
      
      Row row = aSheet.getRow( 0);
      row.setRowStyle( headerStyle);
   }

   private void writeActivityDataColumn( Sheet aSheet, int aRowNum, int aColNum)
   {
      writeCell( aSheet, aRowNum++, aColNum, "Activity");
      Iterator<Activity> iter = 
            Activity.getAll().stream()
            .sorted( new Comparator<Activity>() {
               public int compare(Activity a, Activity b) {
                  return a.getName().compareToIgnoreCase( b.getName());
               }
            })
            .iterator();
      while( iter.hasNext())
      {
         writeCell( aSheet, aRowNum++, aColNum, iter.next().getName());
      }
      aSheet.autoSizeColumn( aColNum);
   }

   private void writeActivityTypeDataColumn( Sheet aSheet, int aRowNum, int aColNum)
   {
      writeCell( aSheet, aRowNum++, aColNum, "Activity Type");
      Iterator<ActivityType> iter =
            ActivityType.getAll().stream()
            .sorted( new Comparator<ActivityType>() {
               @Override
               public int compare( ActivityType arg0, ActivityType arg1)
               {
                  return arg0.getName().compareToIgnoreCase( arg1.getName());
               }
            })
            .iterator();
      while (iter.hasNext())
         writeCell( aSheet, aRowNum++, aColNum, iter.next().getName());
      aSheet.autoSizeColumn( aColNum);
   }

   private void writeActivityRoleDataColumn( Sheet aSheet, int aRowNum, int aColNum)
   {
      writeCell( aSheet, aRowNum++, aColNum, "Activity Role");
      Iterator<ActivityRole> iter =
            ActivityRole.getAll().stream()
            .sorted( new Comparator<ActivityRole>() {
               @Override
               public int compare( ActivityRole arg0, ActivityRole arg1)
               {
                  return arg0.getName().compareToIgnoreCase( arg1.getName());
               }})
            .iterator();
      while (iter.hasNext())
         writeCell( aSheet, aRowNum++, aColNum, iter.next().getName());
      aSheet.autoSizeColumn( aColNum);
      
   }

   private void writeSkillDataColumn( Sheet aSheet, int aRowNum, int aColNum)
   {
      writeCell( aSheet, aRowNum++, aColNum, "Skill");
      Iterator<Skill> iter = 
            Skill.getAll().stream()
            .sorted( new Comparator<Skill>() {
               @Override
               public int compare( Skill arg0, Skill arg1)
               {
                  return arg0.getName().compareToIgnoreCase( arg1.getName());
               }})
            .iterator();
      while (iter.hasNext())
         writeCell( aSheet, aRowNum++, aColNum, iter.next().getName());
      aSheet.autoSizeColumn( aColNum);
   }

   /**
    * Write the given string into the indicated cell (0-based indexes) of the given sheet.
    * @param aSheet
    * @param aRowNum
    * @param aColNum
    * @param aString
    */
   private void writeCell( Sheet aSheet, int aRowNum, int aColNum, String aString)
   {
      Row row = aSheet.getRow( aRowNum);
      if (row == null)
            row = aSheet.createRow( aRowNum);
      Cell cell = row.getCell( aColNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
      cell.setCellValue( aString);
   }

   /**
    * Creates a row at the given row number and writes the given strings to it, using the given font styles.
    * <p>
    * We require an explicit row number because
    * @param aSheet
    * @param aRowNum 
    * @param aStringv
    * @param aFontStyleSet TODO: This should be a true CellStyle, not a bunch of flags for bold, italic.
    * @return the created row
    */
   private  Row createRow( Sheet aSheet, String[] aStringv, EnumSet<FontStyle> aFontStyleSet)
   {
      Log.setMember( null); // TODO: move this somewhere else, higher in the call tree
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
      int physRowCount = aSheet.getPhysicalNumberOfRows();
      int nextRow = physRowCount;
      Row row = aSheet.createRow( nextRow);
      int colNum = 0;
      for (String cellValue : aStringv)
      {
         row.createCell( colNum).setCellValue( cellValue);
         colNum++;
      }
      return row;
   }
   
   /**
    * Creates a row on the "Activities" sheet (given) having info common to the various types of rows on this sheet.
    * @param aSheet
    * @param aMember
    * @return The created row
    */
   private Row createActivityPrefixRow( Sheet aSheet, ChurchMember aMember)
   {
      Row row = createRow( aSheet, new String[] {aMember.getName()}, null);
      Cell cell;
      
      cell = row.createCell( row.getLastCellNum(), CellType.NUMERIC);
      cell.setCellValue( aMember.getAge());
      
      cell = row.createCell( row.getLastCellNum());
      if (aMember.getDateJoined() == null)
         ;
      else
      {
         cell.setCellValue( aMember.getDateJoined());
         cell.setCellStyle( dateStyle);
      }
      
      return row;
   }

   private void appendToRow( Row aRow, String[] aStringv)
   {
      int colNum = aRow.getLastCellNum();
      for (String cellValue : aStringv)
      {
         aRow.createCell( colNum).setCellValue( cellValue);
         colNum++;
      }
   }
   
   private void appendToRow( Row aRow, Date[] aDatev)
   {
      int colNum = aRow.getLastCellNum();
      for (Date date : aDatev)
      {
         Cell cell = aRow.createCell( colNum++);
         if (date == null)
            ;
         else
         {
            cell.setCellValue( date);
            cell.setCellStyle( dateStyle);
         }
      }
   }

   private  Row createMemberDetailRow( Sheet aSheet, ChurchMember aMember)
   {
      Row row = createRow( aSheet, new String[] {aMember.getName()}, null);
      Cell cell;
      
      cell = row.createCell( row.getLastCellNum());
      cell.setCellValue( aMember.getPhone());
      
      cell = row.createCell( row.getLastCellNum());
      cell.setCellValue( aMember.getEmail());
      
      return row;
   }

   private void appendActivityDetailRow( Row aRow, ChurchMember aMember, ActivityEngagement anActivityEngagement)
   {
      Date startDate = anActivityEngagement.getStartDate();
      Date endDate = anActivityEngagement.hasRotationDate() ? anActivityEngagement.getEndDate() : null;
      appendToRow( aRow, 
            new String[] 
                  {
                        anActivityEngagement.getActivityType().getName()
                  });
      appendToRow( aRow,
            new Date[]
                  {
                        startDate,
                        endDate
                  });
      appendToRow( aRow,
            new String[]
                  {
                        anActivityEngagement.getRole().getName(),
                        anActivityEngagement.getActivity().getName()
                  }); 
   }

   private void appendSkillRow( Row aRow, ChurchMember aMember, MemberSkill aSkill)
   {
      appendToRow( aRow,
            new String[]
                  {
                        "SKILL",
                        "",
                        "",
                        aSkill.getSource().toString(),
                        aSkill.getSkill().getName()
                  });
   }

   private void appendCommentDetailRow( Row aRow, ChurchMember aMember, Comment aComment)
   {
      Date commentDate = aComment.getDate();
      appendToRow( aRow,
            new String[] 
                  {
                        "COMMENT"
                  });
      appendToRow( aRow,
            new Date[]
                  {
                        commentDate
                  });
      appendToRow( aRow,
            new String[]
                  {
                        "",
                        aComment.getLevel().toString(),
                        aComment.getText()
                  });
   }
}
