package church.universityumc;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** 
 * A member of the congregation.
 *
 */
public class ChurchMember
{
   private String                  lastName;
   private String                  fullName;
   private long                    age;
   private Date                    dateJoined;
   private String                  phone;
   private String                  email;
   private Set<String>             biography;
   private Set<ActivityEngagement> serviceHistory; // TODO: set + compareTo()
   private Set<Skill>              skills;
   private Set<Interest>           interests;
   private Set<Comment>            comments;
   private HashSet<Contact>        contactHistory;
   
   /**
    * The date the member's {@link age} is "as of", meaning that age is correct as of the indicated date.
    * One year later, that member will be one year older.
    */
   private Date ageAsOf;
   
   private static final Pattern NAME_SPLITTER = Pattern.compile( ",?\\s+");
   
   // I think "the 10th" is enough, don't you?
   private static final Pattern SUFFIX_RE = Pattern.compile( "Jr\\.?|Sr\\.?|III?|I?VI?I?I?|I?X", Pattern.CASE_INSENSITIVE);
   
   private static String EOL = System.getProperty( "line.separator");

   /**
    * The member's original full name, as received from AccessACS.
    * @return
    */
   public String getFullName()
   {
      return fullName;
   }

   /**
    * Parses given full name to get {@link #lastName}. In the absence of well-known suffixes, the last name is
    * recognized as the last white-separated word, otherwise it's the last word before any suffixes.
    * 
    * @param aFullName
    */
   public void setFullName(String aFullName)
   {
      fullName = aFullName;
      
      if (aFullName == null || aFullName.length() == 0)
         lastName = aFullName;
      else
      {
         String[] nameParts = NAME_SPLITTER.split( aFullName);
         int n = nameParts.length;
         int i = n - 1;
         while (i >= 0 && SUFFIX_RE.matcher( nameParts[i]).matches())
            i--;
         if (i < 0)
            // The entire name is all suffixes? Ok, so be it.
            lastName = aFullName;
         else
            lastName = nameParts[i];
      }
   }

   public String getLastName()
   {
      return lastName;
   }

   public void setLastName( String lastName)
   {
      this.lastName = lastName;
   }

   /**
    * In years.
    * @return
    */
   public long getAge()
   {
      return age;
   }

   public void setAge( long anAge)
   {
      age = anAge;
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

   /**
    * Returns all the biography strings that have been entered, in arbitrary order.
    * @return
    */
   public String getBiographyString()
   {
      return biography.stream().collect( Collectors.joining( EOL));
   }
   
   private void addBiography( String aBiography)
   {
      if (aBiography == null || aBiography.length() == 0)
         return;
      if (biography == null)
         biography = Collections.synchronizedSet( new HashSet<String>());
      String bio = aBiography.intern(); // Faster set operation if we can compare just on hashcode and not character by character.
      biography.add( bio);
   }

   /**
    * The types of activities (service opportunities) the member is interested in.
    * @return
    */
   public Collection<Interest> getInterests()
   {
      return Collections.unmodifiableCollection( interests);
   }

   public void addInterest( Interest anInterest)
   {
      if (interests == null)
         interests = Collections.synchronizedSet( new HashSet<Interest>());
      interests.add( anInterest);
   }
   
   /**
    * The member's skills (may or may not match with {@link #getInterests()}).
    * @return
    */
   public Collection<Skill> getSkills()
   {
      if (skills == null)
         return null;
      else
         return Collections.unmodifiableCollection( skills);
   }
   
   public void addSkill( Skill aSkill)
   {
      if (skills == null)
         skills = Collections.synchronizedSet( new HashSet<Skill>());
      skills.add( aSkill);
   }

   /**
    * The member's service history in our congregation.
    * @return
    */
   public Collection<ActivityEngagement> getServiceHistory()
   {
      if (serviceHistory == null)
         return null;
      else
         return Collections.unmodifiableCollection( serviceHistory);
   }
   
   public void addServiceHistory( ActivityEngagement anEngagement)
   {
      if (serviceHistory == null)
         serviceHistory = Collections.synchronizedSet( new HashSet<ActivityEngagement>());
      serviceHistory.add( anEngagement);
   }

   /**
    * The member's history of having been contacted by Lay Leadership (or possibly other recruiting efforts).
    * @return
    */
   public Collection<Contact> getContactHistory()
   {
      return Collections.unmodifiableCollection( contactHistory);
   }
   
   public void addContactHistory( Contact aContact)
   {
      // TODO: implement
      throw new UnimplementedException();
   }

   public void addComment( Comment aComment)
   {
      if (aComment == null)
         return;
      if (comments == null)
         comments = Collections.synchronizedSet( new HashSet<Comment>());
      comments.add( aComment);
      if (aComment.getType() == CommentType.Biography)
         addBiography( aComment.getText());
   }
   
   public Collection<Comment> getComments()
   {
      if (comments == null)
         return null;
      else
         return Collections.unmodifiableCollection( comments);
   }
   
   /**
    * Dumps this object to the given stream as text.
    * @param out
    */
   public void dumpText(PrintStream out) {
       // TODO Auto-generated method stub

   }

   public String toString()
   {
      return getFullName();
   }

   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (age ^ (age >>> 32));
      result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
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
      ChurchMember other = (ChurchMember) obj;
      if (age != other.age) return false;
      if (fullName == null)
      {
         if (other.fullName != null) return false;
      }
      else if (!fullName.equals( other.fullName)) return false;
      return true;
   }

   /**
    * Merge the given ChurchMember into this ChurchMember, copying new data.
    * @param aMember
    */
   public void merge( ChurchMember aMember)
   {
      // Don't need to merge any of the basic demographics for the member because they come from the same source
      // and should be duplicates.
      
      if ( aMember.biography != null)      aMember.biography      .stream().forEach( b -> addBiography( b));
      if ( aMember.serviceHistory != null) aMember.serviceHistory .stream().forEach( ae -> addServiceHistory( ae));
      if ( aMember.skills != null)         aMember.skills         .stream().forEach( sk -> addSkill( sk));
      if ( aMember.interests != null)      aMember.interests      .stream().forEach( i -> addInterest( i));
      if ( aMember.comments != null)       aMember.comments       .stream().forEach( c -> addComment( c));
   }
   
}
