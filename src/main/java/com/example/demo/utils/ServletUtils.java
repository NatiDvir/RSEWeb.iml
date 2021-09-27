package com.example.demo.utils;

import engine.rse.RseStocks;
import engine.rse.RseUsers;

import javax.servlet.ServletContext;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String STOCK_MANAGER_ATTRIBUTE_NAME = "stockManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object stockManagerLock = new Object();

    public static RseUsers getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new RseUsers());
            }
        }
        return (RseUsers) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static RseStocks getStockManager(ServletContext servletContext) {
        synchronized (stockManagerLock) {
            if (servletContext.getAttribute(STOCK_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(STOCK_MANAGER_ATTRIBUTE_NAME, new RseStocks());
            }
        }
        return (RseStocks) servletContext.getAttribute(STOCK_MANAGER_ATTRIBUTE_NAME);
    }
}