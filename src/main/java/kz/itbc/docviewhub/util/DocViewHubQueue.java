package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.itbc.docviewhub.datebase.DAO.DocumentQueueDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.entity.DocumentQueue;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import kz.itbc.docviewhub.exception.DocViewHubQueueException;
import kz.itbc.docviewhub.exception.DocumentQueueDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public final class DocViewHubQueue extends TimerTask {
    private static final Logger UTIL_LOGGER = LogManager.getRootLogger();
    private List<DocumentQueue> documentQueue;
    private static DocViewHubQueue single_instance = null;

    private DocViewHubQueue() {
        try {
            getDocumentsFromTable();
        } catch (DocViewHubQueueException e){
            UTIL_LOGGER.error(e.getMessage());
            this.documentQueue = new ArrayList<>();
        }
    }

    public static DocViewHubQueue getInstance() {
        if (single_instance == null) {
            single_instance = new DocViewHubQueue();
        }
        return single_instance;
    }

    @Override
    public void run() {
        if (documentQueue.size() > 0) {
            sendDocumentsToClient();
        } else {
            UTIL_LOGGER.info("There is no documents in the queue.");
        }
    }

    private void sendDocumentsToClient(){
        JSONObject jsonObj = new JSONObject();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        long millisecondsDiff;
        int seconds;
        int hours;
        for (Iterator<DocumentQueue> iterator = documentQueue.iterator(); iterator.hasNext(); ) {
            DocumentQueue documentQueue = iterator.next();
            millisecondsDiff = System.currentTimeMillis() - documentQueue.getReceiveDate().getTime();
            seconds = (int) millisecondsDiff / 1000;
            hours = seconds / 3600;
            if (hours > 71) {
                System.out.println("71+");
                sendFailNotificationToSender(gson, jsonObj, documentQueue);
                iterator.remove();
            } else {
                jsonObj.clear();
                jsonObj.put(ID_DOCUMENT_QUEUE_ATTRIBUTE, documentQueue.getId_DocumentQueue());
                jsonObj.put(JSON_DATA_ATTRIBUTE, documentQueue.getJsonData());
                jsonObj.put(AES_ATTRIBUTE, documentQueue.getAes());
                String jsonRequestData = gson.toJson(jsonObj);
                try {
                    sendDocumentToClient(jsonRequestData, documentQueue);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    documentQueue.setSendDate(ts);
                    documentQueue.getDocumentStatus().setId_DocumentStatus(2);
                    new DocumentQueueDAO().updateDocumentQueueStatusAndSendDate(documentQueue);
                    iterator.remove();
                    sendNotificationToSender(documentQueue);
                    try {
                        new DocumentQueueDAO().updateDocumentQueueStatusAndSendDate(documentQueue);
                    } catch (DocumentQueueDAOException e) {
                        UTIL_LOGGER.error("DocViewHubQueue: Error of updating the document in the table", e);
                    }
                } catch (Exception e) {
                    UTIL_LOGGER.error("DocViewHubQueue: Error occurred during the document sending", e);
                }
            }
        }
        UTIL_LOGGER.info("Document sending has finished at: " + new Date());
    }

    synchronized public void addDocumentToQueue(DocumentQueue documentQueue) {
        this.documentQueue.add(documentQueue);
    }

    private void sendFailNotificationToSender(Gson gson, JSONObject jsonObject, DocumentQueue documentQueue) {
        jsonObject.clear();
        jsonObject.put(ID_CLIENTDOCUMENTQUEUE_ATTRIBUTE, documentQueue.getId_ClientDocumentQueue());
        jsonObject.put(ERROR_ATTRIBUTE, "Не удалось отправить документ в течении трех днех. Документ был удален из списка отправки.");
        String jsonData = gson.toJson(jsonObject);
        String serverAddressToInformClient = documentQueue.getRecipientCompany().getServerAddress() + "/DocViewHub/sending-fail";
        HttpsURLConnection connection = null;
        try {
            connection = ConnectionUtil.createRequest(serverAddressToInformClient, jsonData);
            ConnectionUtil.readResponse(connection);
        } catch (IOException | ConnectionUtilException e) {
            UTIL_LOGGER.error("No response from the connection", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void sendDocumentToClient(String json, DocumentQueue documentQueue) {
        Company senderCompany;
        Company recipientCompany;
        ////////////////////////////////////////////////////////////////////////////
        System.out.println("sendDocumentToClient json: " + json);
        for (DocumentQueue doc : this.documentQueue) {
            System.out.println("Queue listing b4: ");
            System.out.println(doc.getId_DocumentQueue() + ", " + doc.getSenderCompany().getId() + ", "
                    + doc.getJsonData() + ", " + doc.getJsonData() + ", " + doc.getAes());
        }
        ////////////////////////////////////////////////////////////////////////////
        senderCompany = documentQueue.getSenderCompany();
        recipientCompany = documentQueue.getRecipientCompany();
        if (senderCompany != null && recipientCompany != null) {
            String serverAddress = recipientCompany.getServerAddress() + "/DocViewHub/send";
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, json);
                System.out.println("sendDocumentToClient json: " + json);
                ConnectionUtil.readResponse(connection);
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                UTIL_LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void sendNotificationToSender(DocumentQueue documentQueue) {
        Company senderCompany = documentQueue.getSenderCompany();
        if (senderCompany != null) {
            String serverAddress = senderCompany.getServerAddress() + "/DocViewHub/sending-success";
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONObject clientDocumentQueueIDJson = new JSONObject();
            clientDocumentQueueIDJson.put(ID_CLIENTDOCUMENTQUEUE_ATTRIBUTE, documentQueue.getId_ClientDocumentQueue());
            String jsonData = gson.toJson(clientDocumentQueueIDJson);
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, jsonData);
                System.out.println("sendNotificationToSender jsonData: " + jsonData);
                ConnectionUtil.readResponse(connection);
                UTIL_LOGGER.info("DocViewHubQueue: Document data is sent to " + senderCompany.getNameRU() + " company");
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                UTIL_LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void getDocumentsFromTable() throws DocViewHubQueueException {
        List<DocumentQueue> documentQueueList;
        try {
            documentQueueList = new DocumentQueueDAO().getDocumentQueues();
            UTIL_LOGGER.info("DocViewHubQueue: The number of documents in the queue is: " + documentQueueList.size());
            documentQueue = documentQueueList;
        } catch (DocumentQueueDAOException e) {
            UTIL_LOGGER.error("Error: Documents were not successfully received.", e);
            throw new DocViewHubQueueException("Error: Documents were not successfully received.");
        }
    }
}
