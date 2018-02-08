package church.universityumc;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/** 
 * A member of the congregation.
 *
 */
public class ChurchMember
{
   private String name;
   private int age;
   
   /**
    * The date the member's {@link age} is "as of", meaning that age is correct as of the indicated date.
    * One year later, that member will be one year older.
    */
   private Date ageAsOf;
   
   private int yearJoined;
   private String phone;
   private String email;
   private String biography;
   private Collection<Interest> interests;
   private Collection<Skill> skills;
   private Collection<ActivityEngagement> serviceHistory;
   private Collection<Contact> contactHistory;

   /**
    * In whatever format it comes from ACS in, probably "First Last".
    * @return
    */
   public String getName()
   {
      return name;
   }

   public void setName( String aName)
   {
      name = aName;
   }

   /**
    * In years.
    * @return
    */
   public int getAge()
   {
      return age;
   }

   public void setAge( int aAge)
   {
      age = aAge;
   }

   /**
    * The date the age is current as of.  For example, if we import data from ACS on 1/1/2000, then on 1/1/2001,
    * the member is a year older.
    * @return
    */
   public Date getAgeAsOf()
   {
      return ageAsOf;
   }

   public void setAgeAsOf( Date aAgeAsOf)
   {
      ageAsOf = aAgeAsOf;
   }

   /**
    * The year the member joined our congregation.
    * @return
    */
   public int getYearJoined()
   {
      return yearJoined;
   }

   public void setYearJoined( int aYearJoined)
   {
      yearJoined = aYearJoined;
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
      return biography;
   }
   
   public void addBiography( String aBiography)
   {
      // TODO: implement.
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
   public Collection<Skill> getSkills()
   {
      return skills;
   }
   
   public void addSkill( Skill aSkill)
   {
      // TODO: implement
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
      // TODO: implement
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
   }

   /**
    * Dumps this object to the given stream as text.
    * @param out
    */
   public void dumpText(PrintStream out) {
	   // TODO Auto-generated method stub

   }
}
