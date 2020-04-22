package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Member;
import com.how_hard_can_it_be.layleadership.data_interfaces.LayLeadershipRepository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
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
        _dataSource = (DataSource) initialContext.lookup( "java:comp/env/" + DATABASE_JNDI_NAME);

//        try
//        {
//            try
//            {
//                var cls = Class.forName( "org.sqlite.JDBC" );
//                System.out.println( String.format( "Got class %s", cls.toString()));
//            }
//            catch (ClassNotFoundException exc)
//            {
//                exc.printStackTrace();
//            }
//            var drivers = DriverManager.getDrivers();
//            var nDrivers = 0;
//            while (drivers.hasMoreElements())
//            {
//                nDrivers++;
//                var drvr = drivers.nextElement();
//                if (drvr.acceptsURL( "jdbc:sqlite:/usr/local/var/LayLeadership/layleadership.db" ))
//                    System.out.println( String.format( "Driver %s accepts url", drvr.toString()));
//                else
//                    System.out.println( String.format( "Driver %s DOES NOT accept url", drvr.toString()));
//            }
//            System.out.println( String.format( "Found %d drivers", nDrivers));
//        }
//        catch (SQLException exc)
//        {
//            exc.printStackTrace();
//        }
    }

    @Override
    public Collection<Member> getAllMembers() throws SQLException
    {
        Connection conn = null;
        try
        {
//            conn = DriverManager.getConnection( "jdbc:sqlite:/usr/local/var/LayLeadership/layleadership.db");
//            conn.close();

            conn = _dataSource.getConnection();
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
        finally
        {
            if (conn != null)
                conn.close();
        }
    }
}
