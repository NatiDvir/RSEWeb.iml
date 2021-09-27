package com.example.demo.utils;

import engine.rse.RseUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static RseUser getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("myUser") : null;
        return (RseUser) sessionAttribute;
    }
    public static void setUser(HttpServletRequest request, RseUser user) {
        HttpSession session = request.getSession(false);
        if(session != null)
            session.setAttribute("myUser",user);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
