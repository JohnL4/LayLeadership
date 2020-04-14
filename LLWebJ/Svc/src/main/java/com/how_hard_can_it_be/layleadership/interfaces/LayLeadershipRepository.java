package com.how_hard_can_it_be.layleadership.interfaces;

import java.util.Collection;

import com.how_hard_can_it_be.layleadership.business.Member;

public interface LayLeadershipRepository
{
   Collection<Member> getAllPersons();
}