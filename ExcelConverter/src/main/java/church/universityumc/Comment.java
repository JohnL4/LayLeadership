package church.universityumc;

import java.util.Date;

/**
 * A comment on a church member (for example, biographical prose).
 * @author john
 *
 */
public class Comment
{

   private Date         commentDate;
   private CommentLevel commentLevel;
   private CommentType  commentType;
   private String       commentText;
   private InfoSource   source;

   /**
    * 
    * @param aCommentDate
    * @param aCommentLevel
    * @param aCommentType
    * @param aCommentText
    * @param aSource - Where this comment came from.
    * @throws EnumResolutionException if one of comment level or comment type can't be mapped to an enum value.
    */
   public Comment( Date aCommentDate, String aCommentLevel, String aCommentType, String aCommentText, InfoSource aSource) 
   {
      commentDate = aCommentDate;
      commentLevel = CommentLevel.forString( aCommentLevel);
      commentType = CommentType.forString( aCommentType);
      commentText = aCommentText;
      source = aSource;
   }

   /**
    * @return the commentDate
    */
   public Date getDate()
   {
      return commentDate;
   }

   /**
    * @return the commentLevel
    */
   public CommentLevel getLevel()
   {
      return commentLevel;
   }

   /**
    * @return the commentType
    */
   public CommentType getType()
   {
      return commentType;
   }

   /**
    * @return the commentText
    */
   public String getText()
   {
      return commentText;
   }
   
   public InfoSource getSource()
   {
      return source;
   }
   
   public String toString()
   {
      return String.format( "%tD %s (%s)", getDate(), getType(), getLevel());
   }
}
