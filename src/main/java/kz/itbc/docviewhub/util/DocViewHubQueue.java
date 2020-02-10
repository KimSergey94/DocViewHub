package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.itbc.docviewhub.database.ConnectionPoolDBCP;
import kz.itbc.docviewhub.database.DAO.DocumentQueueDAO;
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
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static kz.itbc.docviewhub.constant.AppConstant.*;

public final class DocViewHubQueue extends TimerTask {
    private static final Logger UTIL_LOGGER = LogManager.getRootLogger();
    private List<DocumentQueue> documentQueue;
    private static DocViewHubQueue single_instance = null;
    private List<DocumentQueue> addedDocumentQueue;
    private static AtomicBoolean flagInIteration;

    private DocViewHubQueue() {
        try {
            getDocumentsFromTable();
            addedDocumentQueue = new ArrayList<>();
            flagInIteration = new AtomicBoolean(false);
        } catch (DocViewHubQueueException e) {
            UTIL_LOGGER.error(e.getMessage());
            documentQueue = new ArrayList<>();
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
        documentQueue.addAll(addedDocumentQueue);
        addedDocumentQueue.clear();
        if (flagInIteration.get()) {
            UTIL_LOGGER.info("The iteration is in progress. The process will be started over with the next method call");
        } else {
            if (documentQueue.size() > 0) {
                sendDocumentsToClient();
            } else {
                UTIL_LOGGER.info("There are no documents in the queue.");
            }
        }
    }

    private void sendDocumentsToClient() {
        flagInIteration.set(true);
        JSONObject jsonObj = new JSONObject();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        long millisecondsDiff;
        int hoursDiff;
        for (Iterator<DocumentQueue> iterator = documentQueue.iterator(); iterator.hasNext(); ) {
            DocumentQueue documentQueue = iterator.next();
            millisecondsDiff = System.currentTimeMillis() - documentQueue.getReceiveDate().getTime();
            hoursDiff = ((int) millisecondsDiff / 1000) / 3600;
            if (hoursDiff > 23) {
                try (Connection connection = ConnectionPoolDBCP.getInstance().getConnection()){
                    connection.setAutoCommit(false);
                    documentQueue.setFlagDeleted(true);
                    documentQueue.getDocumentStatus().setId_DocumentStatus(3);
                    new DocumentQueueDAO().updateDocumentQueueStatus(connection, documentQueue);
                    sendFailNotificationToSender(gson, jsonObj, documentQueue);
                    connection.commit();
                    iterator.remove();
                    UTIL_LOGGER.info("DocViewHubQueue: Document had not been sent in 24 hours and was deleted from the queue.");
                } catch (DocumentQueueDAOException e) {
                    UTIL_LOGGER.error("DocViewHubQueue: Error of updating the document in the table", e);
                } catch (Exception e) {
                    UTIL_LOGGER.error("DocViewHubQueue: Error occurred during the document update", e);
                }
            } else {
                jsonObj.clear();
                jsonObj.put(ID_DOCUMENT_QUEUE_ATTRIBUTE, documentQueue.getId_DocumentQueue());
                jsonObj.put(JSON_DATA_ATTRIBUTE, documentQueue.getJsonData());
                jsonObj.put(AES_ATTRIBUTE, documentQueue.getAes());
                String jsonRequestData = gson.toJson(jsonObj);
                try (Connection connection = ConnectionPoolDBCP.getInstance().getConnection()){
                    connection.setAutoCommit(false);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    documentQueue.setSendDate(ts);
                    documentQueue.getDocumentStatus().setId_DocumentStatus(2);
                    new DocumentQueueDAO().updateDocumentQueueStatusAndSendDate(connection, documentQueue);
                    sendDocumentToClient(jsonRequestData, documentQueue);
                    connection.commit();
                    iterator.remove();
                    sendNotificationToSender(documentQueue);
                } catch (DocumentQueueDAOException e) {
                    UTIL_LOGGER.error("DocViewHubQueue: Error of updating the document in the table", e);
                } catch (Exception e) {
                    UTIL_LOGGER.error("DocViewHubQueue: Error occurred during the document sending", e);
                }
            }
        }
        flagInIteration.set(false);
        UTIL_LOGGER.info("Document sending has finished at: " + new Date());
    }

    synchronized public void addDocumentToQueue(DocumentQueue documentQueue) {
        if (flagInIteration.get()) {
            this.addedDocumentQueue.add(documentQueue);
        } else {
            this.documentQueue.add(documentQueue);
        }
    }

    private void sendFailNotificationToSender(Gson gson, JSONObject jsonObject, DocumentQueue documentQueue) {
        jsonObject.clear();
        jsonObject.put(ID_CLIENTDOCUMENTQUEUE_ATTRIBUTE, documentQueue.getId_ClientDocumentQueue());
        jsonObject.put(CODE_RESULT_ATTRIBUTE, 0);
        jsonObject.put(MESSAGE_ATTRIBUTE, "Не удалось отправить документ в течение 24 часов. Документ был удален из списка отправки.");
        String jsonData = gson.toJson(jsonObject);
        String serverAddressToInformClient = documentQueue.getSenderCompany().getServerAddress() + "/DocViewHub/sending-result";
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

    private void sendDocumentToClient(String json, DocumentQueue documentQueue) throws DocViewHubQueueException {
        Company senderCompany;
        Company recipientCompany;
        senderCompany = documentQueue.getSenderCompany();
        recipientCompany = documentQueue.getRecipientCompany();
        if (senderCompany != null && recipientCompany != null) {
            String serverAddress = recipientCompany.getServerAddress() + "/DocViewHub/send";
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, json);
                ConnectionUtil.readResponse(connection);
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
                throw new DocViewHubQueueException("DocViewHubQueue: Error occurred while sending a document to the recipient company.");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep( 1000);
            } catch (InterruptedException e) {
                UTIL_LOGGER.error(e.getMessage(), e);
                throw new DocViewHubQueueException("DocViewHubQueue: Thread operation error.");
            }
        }
    }

    private void sendNotificationToSender(DocumentQueue documentQueue) throws DocViewHubQueueException {
        Company senderCompany = documentQueue.getSenderCompany();
        if (senderCompany != null) {
            String serverAddress = senderCompany.getServerAddress() + "/DocViewHub/sending-result";
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONObject clientDocumentQueueIDJson = new JSONObject();
            clientDocumentQueueIDJson.put(ID_CLIENTDOCUMENTQUEUE_ATTRIBUTE, documentQueue.getId_ClientDocumentQueue());
            clientDocumentQueueIDJson.put(CODE_RESULT_ATTRIBUTE, 1);
            clientDocumentQueueIDJson.put(MESSAGE_ATTRIBUTE, "Документ успешно отправлен.");
            String jsonData = gson.toJson(clientDocumentQueueIDJson);
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, jsonData);
                ConnectionUtil.readResponse(connection);
                UTIL_LOGGER.info("DocViewHubQueue: Document data is sent to " + senderCompany.getNameRU() + " company");
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
                throw new DocViewHubQueueException("DocViewHubQueue: Error occurred while establishing a connection.");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep( 1000);
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
            UTIL_LOGGER.error(e.getMessage());
            throw new DocViewHubQueueException("Error: Documents were not successfully received.");
        }
    }
}
