package com.how_hard_can_it_be.layleadership.data;

import java.util.Collection;
import java.util.ArrayList;

import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.service_interfaces.LayLeadershipRepository;
import com.how_hard_can_it_be.inject.Mock;

@Mock
public class LayLeadershipRepositoryScaffold implements LayLeadershipRepository
{
   public Collection<Member> getAllMembers()
   {
      var retval = new ArrayList<Member>();
      retval.add( new Member( 1, "John", "Lusk", "919-555-1212"
              , "john-public@how-hard-can-it-be.com", true, ""));
      retval.add( new Member( 2, "Sherry", "Didow", "919-555-1212"
              , "sherry@didow.net", true, ""));

      return retval;
   }
}