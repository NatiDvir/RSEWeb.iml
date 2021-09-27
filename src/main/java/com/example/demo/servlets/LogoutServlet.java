package com.example.demo.servlets;

import com.example.demo.utils.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LogoutServlet", value = "/logout-servlet")
public class LogoutServlet extends HttpServlet{
    public void init(){
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/json");
        SessionUtils.clearSession(request);


        String res = "index.html";

        try (PrintWriter out = response.getWriter()) {
            out.println(res);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionUtils.clearSession(request);
        String res = "index.html";
        response.getOutputStream().println(res);
    }
}