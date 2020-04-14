package com.how_hard_can_it_be.layleadership.servlet;

import com.google.gson.Gson;
import com.how_hard_can_it_be.layleadership.interfaces.LayLeadershipRepository;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/members")
public class MembersServlet extends HttpServlet
{
    @Inject
    private LayLeadershipRepository _layLeadershipRepository;

    @Override
    protected void doGet( HttpServletRequest aRequest, HttpServletResponse aResponse )
            throws IOException
    {
        var members = _layLeadershipRepository.getAllPersons();

        var gson             = new Gson();
        var membersJsonString = gson.toJson( members );
        aResponse.setContentType( "application/json" );
        aResponse.setCharacterEncoding( "UTF-8" );
        var out = aResponse.getWriter();
        out.print( membersJsonString );
        out.flush();
    }
}
