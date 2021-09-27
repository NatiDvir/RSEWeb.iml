package com.example.demo.servlets;

import com.example.demo.utils.ServletUtils;
import com.example.demo.utils.SessionUtils;
import com.google.gson.Gson;
import engine.rse.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SingleCompanyServlet", value = "/singleCompany-servlet")
public class SingleCompanyServlet  extends HttpServlet {
    public void init() { }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String name = req.getParameter("companyName");
        RseStock stock  = ServletUtils.getStockManager(getServletContext()).findStock(name);
        Gson gson = new Gson();
        List<String> res = new ArrayList<>();
        if(stock != null) {
            RseUser user = ServletUtils
                    .getUserManager(getServletContext())
                    .getUser(SessionUtils
                            .getUser(req)
                            .getUserName());

            if(!user.isAdmin()){
                RseOffers offers = stock.getDealsMade();
                RseStock newStock = new RseStock(stock.getRseSymbol(),
                        stock.getRseCompanyName(),
                        stock.getRsePrice(),
                        stock.getRseCycle());
                newStock.setDealsMade(offers);
                res.add(gson.toJson(newStock));
                RseItem item =user
                        .getRseHoldings()
                        .findItem(stock.getRseSymbol());
                int amountUserHolds = item != null ? user
                        .getRseHoldings()
                        .findItem(stock.getRseSymbol())
                        .getQuantity() : 0;
                res.add(gson.toJson(amountUserHolds));
            }else
                res.add(gson.toJson(stock));
        }
        else
            res = null;

        try (PrintWriter out = resp.getWriter()) {
            out.println(res);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyName = request.getParameter("companyName");
        String companySymbol = request.getParameter("companySymbol");
        String stocksOwnStr = request.getParameter("stocksOwn");
        String amountStr = request.getParameter("amount");
        String priceStr = request.getParameter("price");
        String buySell = request.getParameter("buySell");
        String dealType = request.getParameter("dealType");

        int amount, price = 0, oType = 0, stocksOwn = 0;
        if(companyName == null
                || companySymbol == null
                || stocksOwnStr == null
                || buySell == null
                || dealType == null){
            response.setStatus(404);
            response.getOutputStream().println("Not all fields are filled");
            return;
        }if(companyName.isEmpty()
                || companySymbol.isEmpty()
                || stocksOwnStr.isEmpty()
                || buySell.isEmpty()
                || dealType.isEmpty()){
            response.setStatus(404);
            response.getOutputStream().println("Not all fields are filled");
            return;
        }
        if (dealType.equalsIgnoreCase("lmt"))
            oType = RseOffer.LMT;
        if (dealType.equalsIgnoreCase("mkt")) {
            oType = RseOffer.MKT_CODE;
            price = -1;
        }
        if (dealType.equalsIgnoreCase("FOK"))
            oType = RseOffer.FOK;
        if (dealType.equalsIgnoreCase("IOC"))
            oType = RseOffer.IOC;

        try {
            amount = Integer.parseInt(amountStr);
            price = price == -1 ? -1 : Integer.parseInt(priceStr);
        } catch (Exception e) {
            response.setStatus(404);
            response.getOutputStream().println("Amount and Price must be integers and not empty");
            return;
        }
        try{
            if (request.getParameter("buySell").equalsIgnoreCase("sell"))
                stocksOwn = Integer.parseInt(stocksOwnStr);
        }
        catch (Exception e) {
                response.setStatus(404);
                response.getOutputStream().println("Not holding item for sale");
                return;
        }
        RizpaStockExchangeDescriptor descriptor = new RizpaStockExchangeDescriptor(
                ServletUtils.getStockManager(getServletContext())
                , ServletUtils.getUserManager(getServletContext()));
        RseUser u = descriptor
                .getRseUsers()
                .getUser(SessionUtils.getUser(request).getUserName());

        if (request.getParameter("buySell").equalsIgnoreCase("sell")) {
            RseItem item = u
                    .getRseHoldings()
                    .findItem(companyName);
            if (item != null) {
                if (item.getQuantity() - item.getAwaitingSale() - amount <= 0) {
                    response.setStatus(404);
                    response.getOutputStream().println("Not holding enough for sale");
                    return;
                } else {
                    item.addToAwaitingSale(amount);
                }
            } else {
                response.setStatus(404);
                response.getOutputStream().println("Not holding item for sale");
                return;
            }
        }
        RseOffer o = new RseOffer(
               u
                , amount
                , price
                , oType
                , buySell.equalsIgnoreCase("sell")
        );

        List<String> res = descriptor
                .getRseStocks()
                .findStock(companyName)
                .addOffer(
                        (buySell.equalsIgnoreCase("sell") ? RseOffer.SELL : RseOffer.BUY)
                        , o
                );
        Gson gson = new Gson();
        response.getOutputStream().println(gson.toJson(res));
    }
}
