package com.example.demo.servlets;

import com.example.demo.utils.ServletUtils;
import com.example.demo.utils.SessionUtils;
import engine.rse.RizpaStockExchangeDescriptor;
import engine.rse.RseStock;
import engine.rse.RseStocks;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

@WebServlet(name = "loadFileServlet", value = "/loadFile")
public class LoadFileServlet extends HttpServlet {
    private static final Object stockLock = new Object();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String file = request.getParameter("file");
        StringReader xmlStr = new StringReader(file);
        RizpaStockExchangeDescriptor descriptor = new RizpaStockExchangeDescriptor();
        try {
            descriptor.deserializeFrom(xmlStr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        String res = descriptor.checkRSED();
        if(res == null) {

            RizpaStockExchangeDescriptor oldDescriptor = new RizpaStockExchangeDescriptor(
                    ServletUtils.getStockManager(getServletContext()),
                    ServletUtils.getUserManager(getServletContext()));
            synchronized (stockLock) {
                res = oldDescriptor.checkRSED(descriptor);
                if (res == null) {
                    oldDescriptor.addToRSEDescriptor(descriptor, SessionUtils.getUser(request).getUserName());
                    res = "Success!";
                }
            }
        }
        else
            response.setStatus(400);
        try (PrintWriter out = response.getWriter()) {
            out.println(res);
            out.flush();
        }
    }

    public static String addListing(RseStock s, RseStocks stocks) {
        String res;
        synchronized (stockLock) {
            res = stocks.checkStockExist(s);
            if (res == null) {
                stocks
                        .getRseStock()
                        .add(new RseStock(s.getRseSymbol()
                                , s.getRseCompanyName()
                                , s.getRsePrice()
                        ));
                res = "Listing added successfully";
            }
        }
        return res;
    }
}
