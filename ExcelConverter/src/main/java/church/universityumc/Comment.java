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

   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((commentDate == null) ? 0 : commentDate.hashCode());
      result = prime * result + ((commentLevel == null) ? 0 : commentLevel.hashCode());
      result = prime * result + ((commentText == null) ? 0 : commentText.hashCode());
      result = prime * result + ((commentType == null) ? 0 : commentType.hashCode());
      result = prime * result + ((source == null) ? 0 : source.hashCode());
      return result;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals( Object obj)
   {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Comment other = (Comment) obj;
      if (commentDate == null)
      {
         if (other.commentDate != null) return false;
      }
      else if (!commentDate.equals( other.commentDate)) return false;
      if (commentLevel != other.commentLevel) return false;
      if (commentText == null)
      {
         if (other.commentText != null) return false;
      }
      else if (!commentText.equals( other.commentText)) return false;
      if (commentType != other.commentType) return false;
      if (source != other.source) return false;
      return true;
   }
   
   
}
