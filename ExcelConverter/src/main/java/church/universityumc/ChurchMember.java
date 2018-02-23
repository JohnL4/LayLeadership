package church.universityumc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/** 
 * A member of the congregation.
 *
 */
public class ChurchMember
{
   private String _name;
   private long _age;
   
   /**
    * The date the member's {@link age} is "as of", meaning that age is correct as of the indicated date.
    * One year later, that member will be one year older.
    */
   private Date _ageAsOf;

   private Date                           dateJoined;
   private String                         phone;
   private String                         email;
   private StringBuilder                  biography;
   private Collection<Interest>           interests;
   private Collection<MemberSkill>        skills;
   private Collection<ActivityEngagement> serviceHistory;
   private Collection<Contact>            contactHistory;
   private Collection<Comment>            comments;
   
   /**
    * In whatever format it comes from ACS in, probably "First Last".
    * @return
    */
   public String getName()
   {
      return _name;
   }

   public void setName( String aName)
   {
      _name = aName;
   }

   /**
    * In years.
    * @return
    */
   public long getAge()
   {
      return _age;
   }

   public void setAge( long anAge)
   {
      _age = anAge;
   }

   /**
    * The date the age is current as of.  For example, if we import data from ACS on 1/1/2000, then on 1/1/2001,
    * the member is a year older.
    * @return
    */
   public Date getAgeAsOf()
   {
      return _ageAsOf;
   }

   public void setAgeAsOf( Date aAgeAsOf)
   {
      _ageAsOf = aAgeAsOf;
   }


   /**
    * @return the date the member joined our congregation
    */
   public Date getDateJoined()
   {
      return dateJoined;
   }

   /**
    * @param aDateJoined 
    * @see {@link #getDateJoined()}
    */
   public void setDateJoined( Date aDateJoined)
   {
      dateJoined = aDateJoined;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone( String aPhone)
   {
      phone = aPhone;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail( String aEmail)
   {
      email = aEmail;
   }

   public String getBiography()
   {
      return biography.toString();
   }
   
   private void addBiography( String aBiography)
   {
      if (aBiography == null || aBiography.length() == 0)
         return;
      if (biography == null)
         biography = new StringBuilder();
      if (biography.length() > 0)
         biography.append( "\n");
      biography.append( aBiography);
   }

   /**
    * The types of activities (service opportunities) the member is interested in.
    * @return
    */
   public Collection<Interest> getInterests()
   {
      return interests;
   }

   /**
    * The member's skills (may or may not match with {@link #getInterests()}).
    * @return
    */
   public Collection<MemberSkill> getSkills()
   {
      return skills;
   }
   
   public void addSkill( MemberSkill aMemberSkill)
   {
      if (skills == null)
         skills = new ArrayList<MemberSkill>();
      skills.add( aMemberSkill);
   }

   /**
    * The member's service history in our congregation.
    * @return
    */
   public Collection<ActivityEngagement> getServiceHistory()
   {
      return serviceHistory;
   }
   
   public void addServiceHistory( ActivityEngagement anEngagement)
   {
      if (serviceHistory == null)
         serviceHistory = new ArrayList<ActivityEngagement>();
      serviceHistory.add( anEngagement);
   }

   /**
    * The member's history of having been contacted by Lay Leadership (or possibly other recruiting efforts).
    * @return
    */
   public Collection<Contact> getContactHistory()
   {
      return contactHistory;
   }
   
   public void addContactHistory( Contact aContact)
   {
      // TODO: implement
      throw new UnimplementedException();
   }

   /**
    * Dumps this object to the given stream as text.
    * @param out
    */
   public void dumpText(PrintStream out) {
	   // TODO Auto-generated method stub

   }

   public void addComment( Comment aComment)
   {
      if (aComment == null)
         return;
      if (comments == null)
         comments = new ArrayList<Comment>();
      comments.add( aComment);
      if (aComment.getType() == CommentType.Biography)
         addBiography( aComment.getText());
   }
   
   public Collection<Comment> getComments()
   {
      return comments; // TODO: readonly Collection?
   }
   
   public String toString()
   {
      return getName();
   }
   
}
