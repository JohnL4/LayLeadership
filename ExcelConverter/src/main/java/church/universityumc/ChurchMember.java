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
    * Dumps this object to the given stream as text.
    * @param out
    */
   public void dumpText(PrintStream out) {
	   // TODO Auto-generated method stub

   }
}
