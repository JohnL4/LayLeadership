package church.universityumc;

/**
 * The response a {@link ChurchMember} had to contact by Lay Leadership or other recruitment effort for an
 * {@link ActivityEngagement}.
 */
public enum ContactResponse
{
   /**
    * The {@link ChurchMember} declined the invitation to engage in the activity.
    */
   DECLINED,
   
   /**
    * The {@link ChurchMember} accepted the invitation to engage in the activity.
    */
   ACCEPTED
}
