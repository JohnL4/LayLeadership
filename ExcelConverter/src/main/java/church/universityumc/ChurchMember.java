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
   
   private int                            _yearJoined;
   private String                         _phone;
   private String                         _email;
   private StringBuilder                         _biography;
   private Collection<Interest>           _interests;
   private Collection<MemberSkill>        _skills;
   private Collection<ActivityEngagement> _serviceHistory;
   private Collection<Contact>            _contactHistory;
   private Collection<Comment>            _comments;
   
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
    * The year the member joined our congregation.
    * @return
    */
   public int getYearJoined()
   {
      return _yearJoined;
   }

   public void setYearJoined( int aYearJoined)
   {
      _yearJoined = aYearJoined;
   }

   public String getPhone()
   {
      return _phone;
   }

   public void setPhone( String aPhone)
   {
      _phone = aPhone;
   }

   public String getEmail()
   {
      return _email;
   }

   public void setEmail( String aEmail)
   {
      _email = aEmail;
   }

   public String getBiography()
   {
      return _biography.toString();
   }
   
   private void addBiography( String aBiography)
   {
      if (aBiography == null || aBiography.length() == 0)
         return;
      if (_biography == null)
         _biography = new StringBuilder();
      if (_biography.length() > 0)
         _biography.append( "\n");
      _biography.append( aBiography);
   }

   /**
    * The types of activities (service opportunities) the member is interested in.
    * @return
    */
   public Collection<Interest> getInterests()
   {
      return _interests;
   }

   /**
    * The member's skills (may or may not match with {@link #getInterests()}).
    * @return
    */
   public Collection<MemberSkill> getSkills()
   {
      return _skills;
   }
   
   public void addSkill( MemberSkill aMemberSkill)
   {
      if (_skills == null)
         _skills = new ArrayList<MemberSkill>();
      _skills.add( aMemberSkill);
   }

   /**
    * The member's service history in our congregation.
    * @return
    */
   public Collection<ActivityEngagement> getServiceHistory()
   {
      return _serviceHistory;
   }
   
   public void addServiceHistory( ActivityEngagement anEngagement)
   {
      if (_serviceHistory == null)
         _serviceHistory = new ArrayList<ActivityEngagement>();
      _serviceHistory.add( anEngagement);
   }

   /**
    * The member's history of having been contacted by Lay Leadership (or possibly other recruiting efforts).
    * @return
    */
   public Collection<Contact> getContactHistory()
   {
      return _contactHistory;
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
      if (_comments == null)
         _comments = new ArrayList<Comment>();
      _comments.add( aComment);
      if (aComment.getType() == CommentType.Biography)
         addBiography( aComment.getText());
   }
}
