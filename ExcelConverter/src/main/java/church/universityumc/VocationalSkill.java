package church.universityumc;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
public class VocationalSkill
{
   @XmlAttribute
   public String category, subcategory, subsubcategory;
   
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
            if (subsubcategory == null)
               ;
            else
               sb.append( String.format( ", subsubcategory=%s", subsubcategory));
         }
      }
      sb.append( "]");
      return sb.toString();
   }
}
