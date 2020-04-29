package com.how_hard_can_it_be.layleadership.service_interfaces;

import java.sql.SQLException;
import java.util.Collection;

import com.how_hard_can_it_be.layleadership.business.Member;

public interface LayLeadershipRepository
{
   Collection<Member> getAllMembers() throws SQLException;
}