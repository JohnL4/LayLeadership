package com.how_hard_can_it_be.layleadership.servlet;

import javax.naming.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

@WebServlet( urlPatterns = "/jndiDump")
public class JndiDirectoryServlet extends HttpServlet
{
    protected void doGet( HttpServletRequest aRequest, HttpServletResponse aResponse ) throws IOException
    {
        PrintWriter out = aResponse.getWriter();
        aResponse.setCharacterEncoding( "UTF-8" );

        try
        {
            var initCtx = new InitialContext();
            var env = initCtx.getEnvironment();
            dumpEnvironment( out, env, "JNDI Root Environment");

            try
            {
                dumpBindings( out, initCtx.list( "java:comp" ), "JNDI java:comp Bindings" );
            }
            catch (Exception exc)
            {
                exc.printStackTrace( out);
            }

            var envCtx = (Context) initCtx.lookup( "java:comp/env" );

            env = envCtx.getEnvironment();
            dumpEnvironment( out, env, "JNDI java:comp/env Environment" );
            dumpBindings( out, envCtx.list( "" ), "JNDI java:comp/env Bindings" );

            var obj = envCtx.lookup( "jdbc/LayLeadership");
            out.println( "jdbc/LayLeadership --> " + obj);
        }
        catch (NamingException exc)
        {
            exc.printStackTrace( out);
        }

        out.flush();
    }

    private void dumpBindings( PrintWriter out, NamingEnumeration<NameClassPair> aListBindings, String anEnvironmentName )
            throws NamingException
    {
        if (aListBindings.hasMore())
            out.println( anEnvironmentName);
        while (aListBindings.hasMore())
        {
            try
            {
                var binding = aListBindings.next();
                out.println( String.format( "\t%s\t: %s", binding.getName(), binding.getClassName() ) );
            }
            catch (Exception exc)
            {
                out.println( String.format("\t(caught exception: %s)", exc.getMessage()));
            }
        }
    }

    private void dumpEnvironment( PrintWriter out, Hashtable<?, ?> anEnvironment,
                                  String anEnvironmentName )
    {
        var envKeys = anEnvironment.keys();
        if (envKeys.hasMoreElements())
            out.println( anEnvironmentName );
        while (envKeys.hasMoreElements())
        {
            var key = envKeys.nextElement();
            out.println( String.format( "\t%s\t: %s", key, anEnvironment.get( key ) ) );
        }
    }
}
