package church.universityumc.excelconverter;

import java.util.Date;

/**
 * A comment on a church member (for example, biographical prose).
 * @author john
 *
 */
public class Comment
{

   private Date _commentDate;
   private CommentLevel _commentLevel;
   private CommentType _commentType;
   private String _commentText;
   
   public Comment( Date aCommentDate, String aCommentLevel, String aCommentType, String aCommentText)
   {
      _commentDate = aCommentDate;
      _commentLevel = CommentLevel.forString( aCommentLevel);
      _commentType = CommentType.forString( aCommentType);
      _commentText = aCommentText;
   }

}
