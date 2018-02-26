package church.universityumc;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
public class VocationalSkill
{
   @XmlAttribute
   public String category, subcategory;
   
   /**
    * No-arg constructor for deserialization.
    */
   public VocationalSkill() {}
   
   /**
    * If subcategory is "Other", subsubcategory becomes subcategory.  Otherwise, subsubcategory is ignored.
    * 
    * @param aCategory
    * @param aSubcategory
    * @param aSubsubcategory
    */
   public VocationalSkill( String aCategory, String aSubcategory, String aSubsubcategory)
   {
      category = aCategory;
      if (aSubcategory.equalsIgnoreCase( "Other"))
         subcategory = aSubsubcategory;
      else
         subcategory = aSubcategory;
      if (subcategory.equals(""))
         subcategory = null;
   }
   
   /**
    * For debugging and whatnot.
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder(super.toString());
      sb.append( "[");
      if (category == null)
         ;
      else
      {
         sb.append( String.format( "category=%s", category));
         if (subcategory == null)
            ;
         else
         {
            sb.append( String.format( ", subcategory=%s", subcategory));
         }
      }
      sb.append( "]");
      return sb.toString();
   }
}
