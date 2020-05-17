package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Activity;
import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.service_interfaces.LayLeadershipRepository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@ApplicationScoped
public class LayLeadershipSqliteRepository implements LayLeadershipRepository
{
    private static final String DATABASE_JNDI_NAME    = "jdbc/LayLeadership";
    private static final String PERSISTENCE_UNIT_NAME = "LayLeadership";

    // Guessing it's ok to hold on to the DataSource for a long time.
    @Resource( name = DATABASE_JNDI_NAME) // Automatically prefixes "java:comp/env" onto this resource.  SUPPOSEDLY, you can use 'lookup =' to give a complete path.
    private DataSource _dataSource;

    @PersistenceUnit( unitName = PERSISTENCE_UNIT_NAME )
    EntityManagerFactory _entityMgrFactory;

    public LayLeadershipSqliteRepository()
    {
    }

    @PostConstruct
    private void postConstruct()
    {
        if (_entityMgrFactory == null)
            // Log a warning here, once we figure out how to log.
            _entityMgrFactory = Persistence.createEntityManagerFactory( "LayLeadership" );
    }

    @Override
    public Collection<Member> getAllMembers() throws SQLException
    {
        Connection conn = null;
        try
        {
            conn = _dataSource.getConnection();
            var stmt = conn.prepareStatement(
                    ";SELECT MemberId,\n"
                    + "       FirstName,\n"
                    + "       LastName,\n"
                    + "       PhoneNumber,\n"
                    + "       EmailAddress,\n"
                    + "       Active,\n"
                    + "       Comments\n"
                    + "  FROM Member\n" );
            var                rs     = stmt.executeQuery();
            Collection<Member> retval = new ArrayList<>();
            while (rs.next())
            {
                retval.add(new Member.MemberBuilder()
                                   .setId(rs.getLong("MemberId"))
                                   .setFirstName(rs.getString("FirstName"))
                                   .setLastName(rs.getString("LastName"))
                                   .setPhoneNumber(rs.getString("PhoneNumber"))
                                   .setEmailAddress(rs.getString("EmailAddress"))
                                   .setActive(rs.getBoolean("Active"))
                                   .setComments(rs.getString("Comments"))
                                   .createMember());
            }
            return retval;
        }
        finally
        {
            if (conn != null)
                conn.close();
        }
    }

    @Override
    public Collection<Member> getAllMembersJPQL()
    {
        Collection<Member> retval = new ArrayList<>();
        EntityManager      em;

        em = _entityMgrFactory.createEntityManager();
        var tx = em.getTransaction();
        tx.begin(); // Totally unnecessary for a read.
        Collection<MemberDto> memberDtos = em.createQuery( "SELECT m FROM Member m", MemberDto.class )
                                             .getResultList();
        for (var memberDto : memberDtos)
        {
            retval.add( MemberMapper.INSTANCE.memberDtoToMember( memberDto ));
        }
        tx.commit();
        em.close();
        return retval;
    }

    @Override
    public Collection<Activity> getAllActivitiesJPQL()
    {
        Collection<Activity> retval = new ArrayList<>();
        EntityManager em;

        em = _entityMgrFactory.createEntityManager();
        var tx = em.getTransaction();
        tx.begin();
        Collection<ActivityDto> dtos = em.createQuery("SELECT a FROM Activity a", ActivityDto.class)
                .getResultList();
        for (var dto : dtos)
            retval.add( ActivityMapper.INSTANCE.activityDtoToActivity( dto));
        tx.commit();
        em.close();
        return retval;
    }

}
