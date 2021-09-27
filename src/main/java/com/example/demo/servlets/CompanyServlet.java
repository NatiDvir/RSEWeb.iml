package com.example.demo.servlets;

import com.example.demo.utils.ServletUtils;
import com.example.demo.utils.SessionUtils;
import com.google.gson.Gson;
import engine.rse.RseItem;
import engine.rse.RseStock;
import engine.rse.RseStocks;
import engine.rse.RseUsers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

    @WebServlet(name = "CompanyServlet", value = "/company-servlet")
    public class CompanyServlet extends HttpServlet {
        public void init(){
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("application/json");

            RseStocks allStocks  = ServletUtils.getStockManager(getServletContext());
            Gson gson = new Gson();
            String res = gson.toJson(allStocks.getRseStock());

            try (PrintWriter out = resp.getWriter()) {
                out.println(res);
                out.flush();
            }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String companyName = request.getParameter("companyName");
            String companySymbol = request.getParameter("companySymbol");
            String amountStr = request.getParameter("companyAmount");
            String worthStr = request.getParameter("companyWorth");
            int amount;
            int worth;
            try {
                worth = Integer.parseInt(worthStr);

            } catch (Exception e) {
                String res = "Worth field must be an integer/cannot be empty";
                response.getOutputStream().println(res);
                return;
            }
            try {
                amount = Integer.parseInt(amountStr);
            } catch (Exception e) {
                String res = "Amount field must be an integer/cannot be empty";
                response.getOutputStream().println(res);
                return;
            }
            int price = worth / amount;
            RseStock newStock = new RseStock(companySymbol, companyName, price);
            String res = LoadFileServlet.addListing(newStock, ServletUtils.getStockManager(getServletContext()));
            if(!res.equalsIgnoreCase("Listing added successfully")){
                response.setStatus(400);
            }else {
                ServletUtils
                        .getUserManager(getServletContext())
                        .getUser(
                                SessionUtils
                                        .getUser(request)
                                        .getUserName()
                        )
                        .getRseHoldings()
                        .getRseItem()
                        .add(new RseItem(companySymbol
                                , amount))
                ;
            }
            response.getOutputStream().println(res);
        }
    }
