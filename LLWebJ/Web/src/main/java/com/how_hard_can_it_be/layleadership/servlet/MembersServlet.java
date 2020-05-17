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
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse)
            throws IOException
    {
        PrintWriter out = aResponse.getWriter();
        aResponse.setContentType("application/json");
        aResponse.setCharacterEncoding("UTF-8");
        var gson = new Gson();
        String jsonString;

        var members = _layLeadershipRepository.getAllMembersJPQL();

        jsonString = gson.toJson(members);
        out.println("Memebers via JPQL:");
        out.println();
        out.println(jsonString);
        out.println();

        var activities = _layLeadershipRepository.getAllActivitiesJPQL();

        jsonString = gson.toJson(activities);
        out.println("Activities via JPQL:");
        out.println();
        out.println(jsonString);
        out.println();

        out.flush();
    }
}
