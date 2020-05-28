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

@WebServlet(urlPatterns = "/data")
public class DataServlet extends HttpServlet
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

        dumpMembers(out, gson);
        dumpActivities(out, gson);
        dumpEnumRefs( out, gson);

        out.flush();
    }

    private void dumpMembers(PrintWriter out, Gson aGson)
    {
        String jsonString;

        var members = _layLeadershipRepository.getAllMembersJPQL();

        jsonString = aGson.toJson(members);
        out.println("Memebers via JPQL:");
        out.println();
        out.println(jsonString);
        out.println();
    }

    private void dumpActivities(PrintWriter out, Gson aGson)
    {
        String jsonString;

        var activities = _layLeadershipRepository.getAllActivitiesJPQL();

        jsonString = aGson.toJson(activities);
        out.println("Activities via JPQL:");
        out.println();
        out.println(jsonString);
        out.println();
    }

    private void dumpEnumRefs( PrintWriter out, Gson aGson)
    {
        String jsonString;

//        var enumRefs = _layLeadershipRepository.getAllEnumReferencesJPQL();


    }
}
