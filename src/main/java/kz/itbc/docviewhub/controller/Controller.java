package kz.itbc.docviewhub.controller;

import kz.itbc.docviewhub.datebase.DAO.DocumentQueueDAO;
import kz.itbc.docviewhub.entity.DocumentQueue;
import kz.itbc.docviewhub.exception.DocViewHubQueueException;
import kz.itbc.docviewhub.exception.DocumentQueueDAOException;
import kz.itbc.docviewhub.service.Service;
import kz.itbc.docviewhub.service.ServiceFactory;
import kz.itbc.docviewhub.util.DocViewHubQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static kz.itbc.docviewhub.constant.AppConstant.EMPTY_STRING;

@MultipartConfig(fileSizeThreshold=1024*1024*10, maxFileSize=1024*1024*100, maxRequestSize=1024*1024*150)
public class Controller extends HttpServlet {
    private static final Logger ROOT_LOGGER = LogManager.getRootLogger();

    @Override
    public void init() throws ServletException {
        super.init();
        /*try {
            DocViewHubQueue docViewHubQueue = DocViewHubQueue.getInstance();
            TimerTask timerClass = docViewHubQueue.;
            //initDocViewHubSendingTimer(timerClass);
        } *//*catch (IOException e){
            ROOT_LOGGER.error(e.getMessage(), e);
            this.destroy();
        }*//* catch (Exception e) {
            e.printStackTrace();
        }*/

        /*catch (DocViewHubException e) {
            this.destroy();
        }*/
        System.out.println("Servlet started");

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


   /* private DocViewHubQueue documentSending(DocViewHubQueue docViewHubQueue) throws DocViewHubQueueException {
        try {
            for(Map.Entry<Integer, String> entry : docViewHubQueue.documentQueue.entrySet()){

            }
        }
    }*/



    /*private DocViewHubQueue initDocViewHubQueue(DocViewHubQueue docViewHubQueue) throws DocViewHubQueueException {
        try {
            List<DocumentQueue> documentQueueList = new DocumentQueueDAO().getDocumentQueues();
            for(DocumentQueue document : documentQueueList){
                docViewHubQueue.getInstance().put(document.getId_DocumentQueue(), document.getJsonData());
                System.out.println("TEST: "+document.getId_DocumentQueue()+", "+document.getJsonData());
            }
        } catch (DocumentQueueDAOException e) {
            DAO_LOGGER.error("Error: Documents were not successfully received.", e);
        }

        Company company;
        try {
            company = new CompanyDAO().getCompany();
        } catch (SQLException e){
            ROOT_LOGGER.error(e.getMessage(), e);
            throw new DocViewHubException("Company is null");
        }
        if(company == null){
            throw new DocViewHubException("Company is null");
        }
        docViewHubUtil.registerInDocViewHub();
        return docViewHubUtil;
    }*/

    private void initDocumentSendingTimer(TimerTask timerClass){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerClass, 0, 1 * 30 * 1000);
    }

}
