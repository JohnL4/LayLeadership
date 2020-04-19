package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.data_interfaces.LayLeadershipRepository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public class LayLeadershipSqliteRepository implements LayLeadershipRepository
{
    private static final String DATABASE_JNDI_NAME = "jdbc/LayLeadership";

    private DataSource _dataSource;

    public LayLeadershipSqliteRepository() throws NamingException
    {
        // Guessing it's ok to hold on to the DataSource for a long time.
        var initialContext = new InitialContext(  );
        var envContext = (Context) initialContext.lookup( "java:comp/env");
        _dataSource = (DataSource) envContext.lookup( DATABASE_JNDI_NAME);
    }

    @Override
    public Collection<Member> getAllMembers() throws SQLException
    {
        var conn = _dataSource.getConnection();
        var stmt = conn.prepareStatement( ";SELECT MemberId,\n"
                               + "       FirstName,\n"
                               + "       LastName,\n"
                               + "       PhoneNumber,\n"
                               + "       EmailAddress,\n"
                               + "       Active,\n"
                               + "       Comments\n"
                               + "  FROM Member\n" );
        var          rs     = stmt.executeQuery();
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
}
