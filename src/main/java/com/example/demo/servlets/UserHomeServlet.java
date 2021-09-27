package com.example.demo.servlets;

import com.google.gson.Gson;

import engine.rse.RseUser;
import engine.rse.RseUsers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.example.demo.utils.ServletUtils;
import com.example.demo.utils.SessionUtils;


@WebServlet(name = "UserHomeServlet", value = "/home-servlet")
public class UserHomeServlet extends HttpServlet {


    public void init(){
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        RseUsers allUsers  = ServletUtils.getUserManager(getServletContext());
        Gson gson = new Gson();
        String res = gson.toJson(allUsers.getUsers());

        try (PrintWriter out = response.getWriter()) {
            out.println(res);
            out.flush();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RseUser myUser  = SessionUtils.getUser(request);
        Gson gson = new Gson();
        String res = gson.toJson(myUser);

        try (PrintWriter out = response.getWriter()) {
            out.println(res);
            out.flush();
        }
    }
}