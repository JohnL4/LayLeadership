package church.universityumc;

import java.util.Date;

/**
 * A contact "The Church" (Lay Leadership or other recruitment efforts?) has had with a {@link ChurchMember}.
 * (Note that this is not contact info for somebody, since all that is already captured in in {@link ChurchMember}.)
 */
public class Contact
{
   private ActivityEngagement activityEngagement;
   private ChurchMember by;
   private Date when;
   private ContactResponse response;
   private String notes;
}
