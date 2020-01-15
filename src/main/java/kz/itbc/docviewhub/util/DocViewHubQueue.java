package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.datebase.DAO.DocumentQueueDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.entity.DocumentQueue;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import kz.itbc.docviewhub.exception.DocViewHubQueueException;
import kz.itbc.docviewhub.exception.DocumentQueueDAOException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class DocViewHubQueue extends TimerTask {
    private static final Logger UTIL_LOGGER = LogManager.getRootLogger();
    public Map<Integer, String> documentQueue = new HashMap<>();

    private static DocViewHubQueue single_instance = null;

    private DocViewHubQueue() {
        this.documentQueue = new HashMap<>();
        getDocumentsFromTable();
    }

    public static DocViewHubQueue getInstance() {
        if (single_instance == null) {
            single_instance = new DocViewHubQueue();
        }
        return single_instance;
    }

    @Override
    public void run() {
        UTIL_LOGGER.error("Document sending has started at: " + new Date());
        JSONObject json = new JSONObject();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        for(Map.Entry<Integer, String> entry : documentQueue.entrySet()){
            json.clear();
            json.put(entry.getKey(), entry.getValue());
            String jsonRequestData = gson.toJson(json);
            Map<Integer, String> mapFromJson = gson.fromJson(jsonRequestData, new TypeToken<Map<Integer, String>>() {}.getType());
            String jsonData = gson.toJson(mapFromJson);
            sendDocumentToClients(jsonData);
        }
        UTIL_LOGGER.error("Document sending has finished at: " + new Date());
    }

    private synchronized void sendDocumentToClients(String json) {
        List<Company> companies = null;
        try {
            companies = new CompanyDAO().getAllAvailableCompanies();
        } catch (CompanyDAOException e) {
            UTIL_LOGGER.info("Could not get available companies", e);
        }

        for (Company company : companies) {
            String serverAddress = company.getServerAddress() + "/DocViewHub/send";
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, json);
                ConnectionUtil.readResponse(connection);
                UTIL_LOGGER.info("DocViewHubQueue: Document data is sent to " + company.getNameRU() + " company");
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e){
                UTIL_LOGGER.error(e.getMessage(), e);
            }
        }
    }

    synchronized public void addDocumentToQueue(DocumentQueue documentQueue){
        this.documentQueue.put(documentQueue.getId_DocumentQueue(), documentQueue.getJsonData());
        sendDocumentToClient(documentQueue);
        for(Map.Entry<Integer, String> entry : this.documentQueue.entrySet()){
            System.out.println("key-"+entry.getKey());
            System.out.println("val-"+entry.getValue());
            System.out.println("********123**");
        }
    }
    private synchronized void sendDocumentToClient(DocumentQueue documentQueue) {
        Company senderCompany = null;
        Company recipientCompany = null;
        try {
            senderCompany = new CompanyDAO().getCompanyById(documentQueue.getSenderCompany().getId());
        } catch (CompanyDAOException e) {
            UTIL_LOGGER.info("Could not find the sender company", e);
        }
        try {
            recipientCompany = new CompanyDAO().getCompanyById(documentQueue.getRecipientCompany().getId());
        } catch (CompanyDAOException e) {
            UTIL_LOGGER.info("Could not find the recipient company", e);
        }

        if(senderCompany != null && recipientCompany != null){
            String serverAddress = recipientCompany.getServerAddress() + "/DocViewHub/send";
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, documentQueue.getJsonData());
                ConnectionUtil.readResponse(connection);
                UTIL_LOGGER.info("DocViewHubQueue: Document data is sent to " + recipientCompany.getNameRU() + " senderCompany");
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e){
                UTIL_LOGGER.error(e.getMessage(), e);
            }
        }

    }

    private void getDocumentsFromTable() {
        List<DocumentQueue> documentQueueList = new ArrayList<>();
        try {
            documentQueueList = new DocumentQueueDAO().getDocumentQueues();
            for(DocumentQueue document : documentQueueList){
                documentQueue.put(document.getId_DocumentQueue(), document.getJsonData());
            }
        } catch (DocumentQueueDAOException e) {
            UTIL_LOGGER.error("Error: Documents were not successfully received.", e);
        }
    }




}
