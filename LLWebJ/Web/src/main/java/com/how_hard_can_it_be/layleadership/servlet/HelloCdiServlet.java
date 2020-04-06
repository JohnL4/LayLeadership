package com.how_hard_can_it_be.layleadership.servlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet( "/hello-cdi")
public class HelloCdiServlet extends HttpServlet
{
    @Inject
    private Greeter _greeter;

    @Override
    protected void doGet( HttpServletRequest aRequest, HttpServletResponse aResponde ) throws IOException
    {
        PrintWriter out = aResponde.getWriter();
        out.print( _greeter.greet( "CDI"));
    }
}
