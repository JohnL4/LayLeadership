package com.how_hard_can_it_be.layleadership.servlet;

import com.google.gson.Gson;
import com.how_hard_can_it_be.layleadership.service_interfaces.LayLeadershipRepository;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/members")
public class MembersServlet extends HttpServlet
{
    @Inject
    private LayLeadershipRepository _layLeadershipRepository;

    @Override
    protected void doGet( HttpServletRequest aRequest, HttpServletResponse aResponse )
            throws IOException
    {
        PrintWriter out = aResponse.getWriter();
        try
        {
            var members = _layLeadershipRepository.getAllMembers();

            var gson             = new Gson();
            var membersJsonString = gson.toJson( members );
            aResponse.setContentType( "application/json" );
            aResponse.setCharacterEncoding( "UTF-8" );
            out.print( membersJsonString );
        }
        catch (SQLException exc)
        {
            out.print( exc.toString());
        }
        out.flush();
    }
}
