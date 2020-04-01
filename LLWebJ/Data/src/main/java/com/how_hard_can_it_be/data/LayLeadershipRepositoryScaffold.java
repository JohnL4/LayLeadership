package com.how_hard_can_it_be.layleadership.data;

import java.util.Collection;
import java.util.ArrayList;

import com.how_hard_can_it_be.layleadership.interfaces.LayLeadershipRepository;
import com.how_hard_can_it_be.layleadership.business.Person;


public class LayLeadershipRepositoryScaffold implements LayLeadershipRepository
{
   public Collection<Person> getAllPersons()
   {
      var retval = new ArrayList<Person>();
      retval.add( new Person( "Mario", "It's-a me, Mario!"));
      retval.add( new Person( "Wario", "Heh heh heh."));

      return retval;
   }
}