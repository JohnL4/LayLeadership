package com.how_hard_can_it_be.layleadership.data;

import java.util.Collection;
import java.util.ArrayList;

import com.how_hard_can_it_be.layleadership.business.Activity;
import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.service_interfaces.LayLeadershipRepository;
import com.how_hard_can_it_be.inject.Mock;

@Mock
public class LayLeadershipRepositoryScaffold implements LayLeadershipRepository
{
   public Collection<Member> getAllMembers()
   {
      var retval = new ArrayList<Member>();
      retval.add(new Member.MemberBuilder().setId(1)
                                           .setFirstName("John")
                                           .setLastName("Lusk")
                                           .setPhoneNumber("919-555-1212")
                                           .setEmailAddress("john-public@how-hard-can-it-be.com")
                                           .setActive(true)
                                           .setComments("")
                                           .createMember());
      retval.add(new Member.MemberBuilder().setId(2)
                                           .setFirstName("Sherry")
                                           .setLastName("Didow")
                                           .setPhoneNumber("919-555-1212")
                                           .setEmailAddress("sherry@didow.net")
                                           .setActive(true)
                                           .setComments("")
                                           .createMember());
      return retval;
   }

   @Override
   public Collection<Member> getAllMembersJPQL()
   {
      throw new RuntimeException( "not implemented");
//      return null;
   }

   @Override
   public Collection<Activity> getAllActivitiesJPQL() {
      throw new RuntimeException( "not implemented");
   }

   @Override
   public Collection<Object> getAllInternalObjectsJPQL(String anEntityName)
   {
      return null;
   }
}
