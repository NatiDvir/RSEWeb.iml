package com.example.demo.servlets;

import com.example.demo.utils.ServletUtils;
import com.example.demo.utils.SessionUtils;
import com.google.gson.Gson;
import engine.rse.MoneyActions;
import engine.rse.RseUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MoneyServlet", value = "/money-servlet")
public class MoneyServlet extends HttpServlet {
    public void init(){
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        List<MoneyActions> allActions = ServletUtils
                .getUserManager(getServletContext())
                .getUser(SessionUtils.getUser(request).getUserName())
                .getActionsList();

        Gson g = new Gson();
        String res = g.toJson(allActions);

        try (PrintWriter out = response.getWriter()) {
            out.println(res);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            int money = Integer.parseInt(req.getParameter("loadmoney"));
            ServletUtils
                    .getUserManager(getServletContext())
                    .getUser(SessionUtils.getUser(req).getUserName())
                    .loadMoney(money);

            resp.setStatus(200);
            resp.getOutputStream().println("Success!");
        }catch (Exception e){
            resp.setStatus(404);
            resp.getOutputStream().println("Enter number");
        }
    }
}
