package com.how_hard_can_it_be.layleadership.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import com.how_hard_can_it_be.layleadership.business.Person;

@WebServlet(urlPatterns = { "/json" } )
public class JsonServlet extends HttpServlet
{
   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse)
      throws IOException
   {
      var mario = new Person();
      mario.name = "Mario";
      mario.introduction = "It's-a me, Mario!";

      var gson = new Gson();
      var personJsonString = gson.toJson( mario);
      aResponse.setContentType("application/json");
      aResponse.setCharacterEncoding("UTF-8");
      var out = aResponse.getWriter();
      out.print( personJsonString);
      out.flush();
   }
}