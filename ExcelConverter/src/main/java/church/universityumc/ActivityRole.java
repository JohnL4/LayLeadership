package church.universityumc;

/**
 * A role a {@link ChurchMember} may play in an {@link Activity}.
 */
public enum ActivityRole
{
   /**
    * A "normal" member of this activity, committee, etc.
    */
   MEMBER,
   
   /**
    * The chairperson of the committee (or activity or whatever).
    */
   CHAIR,
   
   /**
    * Organizes but doesn't otherwise serve.
    */
   ORGANIZER
   
   // Obviously, we can make up more roles here if we need to.
}
