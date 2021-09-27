package com.example.demo.servlets;

import engine.rse.RseStock;
import engine.rse.RseUser;
import engine.rse.RseUsers;

import java.io.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.example.demo.utils.ServletUtils;
import com.example.demo.utils.SessionUtils;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static final Object userLock = new Object();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isAdminStr = request.getParameter("admin");
        String name = request.getParameter("username");
        if (name == null || name.isEmpty()) {
            response.setStatus(400);
            response.getOutputStream().println("To sign in insert username");
            return;
        }

        RseUser user;
        RseUsers allUsers = ServletUtils.getUserManager(getServletContext());

        synchronized (userLock) {
            if (allUsers.checkUserExist(name))
                user = allUsers.getUser(name);

            else {
                if (isAdminStr == null || isAdminStr.isEmpty()) {
                    response.setStatus(400);
                    response.getOutputStream().println("To sign up select user type");
                    return;
                }
                boolean isAdmin = isAdminStr.equalsIgnoreCase("admin");
                user = new RseUser(name, isAdmin);
                allUsers.addUser(user);
            }
        }
        response.getOutputStream().println("pages/userHome/userHome.html");
        SessionUtils.setUser(request,user);
    }
}