package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.service_interfaces.LayLeadershipRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.NamingException;
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
    private static final String DATABASE_JNDI_NAME = "jdbc/LayLeadership";

    // Guessing it's ok to hold on to the DataSource for a long time.
    @Resource( name = DATABASE_JNDI_NAME) // Automatically prefixes "java:comp/env" onto this resource.  SUPPOSEDLY, you can use 'lookup =' to give a complete path.
    private DataSource _dataSource;

    @PersistenceUnit( unitName = "LayLeadership")
    EntityManagerFactory _entityMgrFactory;

    public LayLeadershipSqliteRepository() throws NamingException
    {
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
                retval.add( new Member(
                        rs.getLong( "MemberId" )
                        , rs.getString( "FirstName" )
                        , rs.getString( "LastName" )
                        , rs.getString( "PhoneNumber" )
                        , rs.getString( "EmailAddress" )
                        , rs.getBoolean( "Active" )
                        , rs.getString( "Comments" ) ) );
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
        Collection<Member> retval;
        EntityManager      em;

        if (_entityMgrFactory == null)
            _entityMgrFactory = Persistence.createEntityManagerFactory( "LayLeadership" );

        em = _entityMgrFactory.createEntityManager();
        Collection<MemberDto> memberDtos = em.createQuery( "SELECT m FROM Member m", MemberDto.class )
                                             .getResultList();
        retval = new ArrayList<>();
        for (var memberDto : memberDtos)
        {
            var member = new Member( memberDto.getId(), memberDto.getFirstName(), memberDto.getLastName(),
                                     memberDto.getPhoneNumber(), memberDto.getEmailAddress(), memberDto.isActive(),
                                     memberDto.getComments() );
            retval.add( member );
        }
        return retval;
    }
}
