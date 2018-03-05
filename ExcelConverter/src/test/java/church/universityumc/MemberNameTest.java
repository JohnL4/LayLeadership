package church.universityumc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberNameTest
{
   ChurchMember member;
   
   @BeforeEach
   void setupEach()
   {
      member = new ChurchMember();
   }
   
   @Test
   void simpleFirstLast()
   {
      member.setFullName( "John Smith");
      assertEquals( "Smith", member.getLastName());
   }
   
   @Test
   void firstMiLast()
   {
      member.setFullName( "John H. Smith");
      assertEquals( "Smith", member.getLastName());
   }
   
   @Test
   void oneSuffix()
   {
      member.setFullName( "John Smith, Jr.");
      assertEquals( "Smith", member.getLastName());
   }
   
   @Test
   void senior()
   {
      member.setFullName( "John Hussein Smith, Sr.");
      assertEquals( "Smith", member.getLastName());
   }
   
   @Test
   void twoSuffixes()
   {
      member.setFullName( "John Smith III IV");
      assertEquals( "Smith", member.getLastName());
   }
   
   @Test
   void allSuffixes()
   {
      member.setFullName( "II III IV V VI VII VIII IX X");
      assertEquals( "II III IV V VI VII VIII IX X", member.getLastName());
   }
   
   @Test
   void nullFullName()
   {
      member.setFullName( null);
      assertEquals( null, member.getLastName());
   }
   
   @Test
   void whitespaceFullName()
   {
      member.setFullName( "   ");
      assertEquals( "   ", member.getLastName());
   }
   
   @Test
   void trimNormalName()
   {
      member.setFullName( "  John   Smith   ");
      assertEquals( "Smith", member.getLastName());
   }
   
   @Test
   void trimOneName()
   {
      member.setFullName( "  John  ");
      assertEquals( "John", member.getLastName());
   }

}
