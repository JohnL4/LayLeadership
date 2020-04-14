package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.interfaces.LayLeadershipRepository;

import java.util.Collection;

public class LayLeadershipSqliteRepository implements LayLeadershipRepository
{
    @Override
    public Collection<Member> getAllPersons()
    {
        // TODO: fix.
        throw new RuntimeException( "not implemented" );
    }
}
