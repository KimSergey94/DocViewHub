package kz.itbc.docviewhub.controller;

import kz.itbc.docviewhub.service.Service;
import kz.itbc.docviewhub.service.ServiceFactory;
import kz.itbc.docviewhub.util.DocViewHubQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static kz.itbc.docviewhub.constant.AppConstant.EMPTY_STRING;

public class Controller extends HttpServlet {
    private static final Logger ROOT_LOGGER = LogManager.getRootLogger();
    private static Timer timer = new Timer();


    @Override
    public void init() throws ServletException {
        super.init();
        DocViewHubQueue docViewHubQueue = DocViewHubQueue.getInstance();
        TimerTask timerClass = docViewHubQueue;
        initDocumentSendingTimer(timerClass);
        ROOT_LOGGER.info("Servlet started");
    }

    public Controller() {
        super();
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String reqURI = req.getRequestURI();
        System.out.println(reqURI);
        ROOT_LOGGER.info("POST " + reqURI);
        reqURI = reqURI.replace(req.getContextPath(), EMPTY_STRING);
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
        System.out.println(reqURI);
        ROOT_LOGGER.info("GET " + reqURI);
        reqURI = reqURI.replace(req.getContextPath(), EMPTY_STRING);
        ServiceFactory factory = ServiceFactory.getInstance();
        Service service = factory.getService(reqURI);
        try {
            service.doGet(req, resp);
        } catch (IOException e ) {
            ROOT_LOGGER.error(e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private void initDocumentSendingTimer(TimerTask timerClass){
        timer.scheduleAtFixedRate(timerClass, 0, 15 * 60 * 1000);
    }

    public void destroy(){
        super.destroy();
        timer.cancel();
        timer.purge();
    }


}
