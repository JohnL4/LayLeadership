package com.how_hard_can_it_be.layleadership.service_interfaces;

import java.sql.SQLException;
import java.util.Collection;

import com.how_hard_can_it_be.layleadership.business.Activity;
import com.how_hard_can_it_be.layleadership.business.Member;

public interface LayLeadershipRepository
{
   Collection<Member> getAllMembers() throws SQLException;

   Collection<Member> getAllMembersJPQL();

   Collection<Activity> getAllActivitiesJPQL();

   /**
    * Awful hack to return a collection of "internal" objects from the persistence layer to a client so they can
    * do whatever they want with it.  Don't do this in real life.
    * @param anEntityName
    * @return
    */
   Collection<Object> getAllInternalObjectsJPQL( String anEntityName);
}