package kz.itbc.docviewhub.controller;

import kz.itbc.docviewhub.service.Service;
import kz.itbc.docviewhub.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {
    private static final Logger ROOT_LOGGER = LogManager.getRootLogger();

    @Override
    public void init() {}

    public Controller() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String reqURI = req.getRequestURI();
        reqURI = reqURI.replace(req.getContextPath(), "");
        ServiceFactory factory = ServiceFactory.getInstance();
        Service service = factory.getService(reqURI);
        try {
            service.doPost(req, resp);
        } catch (IOException e ) {
            ROOT_LOGGER.error(e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String reqURI = req.getRequestURI();
        reqURI = reqURI.replace(req.getContextPath(), "");
        ServiceFactory factory = ServiceFactory.getInstance();
        Service service = factory.getService(reqURI);
        try {
            service.doGet(req, resp);
        } catch (IOException e ) {
            ROOT_LOGGER.error(e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
