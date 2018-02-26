package church.universityumc;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A skill inferred, possibly from an undated {@link ActivityEngagement}.
 */
@XmlRootElement
public class InferredSkill
{
   /**
    * The type of skill (some high-level categorization).
    */
   @XmlAttribute
   public String type;
   
   /**
    * The name of the skill (in other words, the actual skill).
    */
   @XmlAttribute
   public String activity;
   
   /**
    * The role thie skill plays, or the role a {@link ChurchMember} having this skill plays (or might play).
    */
   @XmlAttribute
   public String role;
   
   /**
    * No-arg constructor used for deserialization.
    */
   public InferredSkill() {}
   
   public InferredSkill( String aType, String anActivity, String aRole)
   {
      type = aType;
      activity = anActivity;
      role = aRole;
   }
}
