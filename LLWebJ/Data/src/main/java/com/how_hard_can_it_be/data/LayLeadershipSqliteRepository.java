package com.how_hard_can_it_be.data;

import com.how_hard_can_it_be.layleadership.business.Person;
import com.how_hard_can_it_be.layleadership.interfaces.LayLeadershipRepository;

import java.util.Collection;

public class LayLeadershipSqliteRepository implements LayLeadershipRepository
{
    @Override
    public Collection<Person> getAllPersons()
    {
        // TODO: fix.
        throw new RuntimeException( "not implemented" );
    }
}
