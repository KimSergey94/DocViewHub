package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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

import static kz.itbc.docviewhub.constant.AppConstant.*;

public final class DocViewHubQueue extends TimerTask {
    private static final Logger UTIL_LOGGER = LogManager.getRootLogger();
    public List<DocumentQueue> documentQueue = new ArrayList();
    private static DocViewHubQueue single_instance = null;

    private DocViewHubQueue() {
        this.documentQueue = new ArrayList();
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
        for(DocumentQueue documentQueue : documentQueue){
            json.clear();
            json.put(ID_DOCUMENT_QUEUE_ATTRIBUTE, documentQueue.getId_DocumentQueue());
            json.put(ID_SENDER_COMPANY_ATTRIBUTE, documentQueue.getSenderCompany().getId());
            json.put(JSON_DATA_ATTRIBUTE, documentQueue.getJsonData());
            String jsonRequestData = gson.toJson(json);
            /*Map<Integer, String> mapFromJson = gson.fromJson(jsonRequestData, new TypeToken<Map<Integer, String>>() {}.getType());
            String jsonData = gson.toJson(mapFromJson);*/
            sendDocumentToClient(jsonRequestData, documentQueue);
        }
        UTIL_LOGGER.error("Document sending has finished at: " + new Date());
    }

    synchronized public void addDocumentToQueue(DocumentQueue documentQueue){
        this.documentQueue.add(documentQueue);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JSONObject json = new JSONObject();
        json.put(ID_DOCUMENT_QUEUE_ATTRIBUTE, documentQueue.getId_DocumentQueue());
        json.put(ID_SENDER_COMPANY_ATTRIBUTE, documentQueue.getSenderCompany().getId());
        json.put(JSON_DATA_ATTRIBUTE, documentQueue.getJsonData());
        String jsonRequestData = gson.toJson(json);
        sendDocumentToClient(jsonRequestData, documentQueue);
        for(DocumentQueue doc : this.documentQueue){
            System.out.println("Queue listing after sending: ");
            System.out.println(doc.getId_DocumentQueue() + ", " + doc.getSenderCompany().getId() + ", "
                    + doc.getJsonData() + ", " + doc.getJsonData());
        }
    }

    private void sendDocumentToClient(String json, DocumentQueue documentQueue) {
        Company senderCompany = null;
        Company recipientCompany = null;
        System.out.println("sendDocumentToClient json: "+ json);
        for(DocumentQueue doc : this.documentQueue){
            System.out.println("Queue listing b4: ");
            System.out.println(doc.getId_DocumentQueue() + ", " + doc.getSenderCompany().getId() + ", "
                    + doc.getJsonData() + ", " + doc.getJsonData());
        }
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
                connection = ConnectionUtil.createRequest(serverAddress, json);
                System.out.println("Queue: request is created and the doc is sent");
                System.out.println(json);
                ConnectionUtil.readResponse(connection);
                this.documentQueue.remove(documentQueue);
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
                documentQueue.add(document);
            }
        } catch (DocumentQueueDAOException e) {
            UTIL_LOGGER.error("Error: Documents were not successfully received.", e);
        }
    }
}
