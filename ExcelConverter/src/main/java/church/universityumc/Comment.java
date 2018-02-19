package church.universityumc;

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

   /**
    * 
    * @param aCommentDate
    * @param aCommentLevel
    * @param aCommentType
    * @param aCommentText
    * @throws EnumResolutionException if one of comment level or comment type can't be mapped to an enum value.
    */
   public Comment( Date aCommentDate, String aCommentLevel, String aCommentType, String aCommentText) 
         throws EnumResolutionException
   {
      _commentDate = aCommentDate;
      _commentLevel = CommentLevel.forString( aCommentLevel);
      _commentType = CommentType.forString( aCommentType);
      _commentText = aCommentText;
   }

   /**
    * @return the commentDate
    */
   public Date getDate()
   {
      return _commentDate;
   }

   /**
    * @return the commentLevel
    */
   public CommentLevel getLevel()
   {
      return _commentLevel;
   }

   /**
    * @return the commentType
    */
   public CommentType getType()
   {
      return _commentType;
   }

   /**
    * @return the commentText
    */
   public String getText()
   {
      return _commentText;
   }
}
