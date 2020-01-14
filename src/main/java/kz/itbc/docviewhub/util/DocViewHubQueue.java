package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kz.itbc.docviewhub.datebase.DAO.DocumentQueueDAO;
import kz.itbc.docviewhub.entity.DocumentQueue;
import kz.itbc.docviewhub.exception.DocViewHubQueueException;
import kz.itbc.docviewhub.exception.DocumentQueueDAOException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class DocViewHubQueue extends TimerTask {
    private static final Logger DAO_LOGGER = LogManager.getRootLogger();
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
        System.out.println("Timer task started at:"+new Date());
        JSONObject json = new JSONObject();

        try {
            URL url = new URL(null, "http://localhost:8080/DocViewHub_war/senddocument", new sun.net.www.protocol.https.Handler());
            HttpsURLConnection connection = null;
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            OutputStream out = connection.getOutputStream();

            for(Map.Entry<Integer, String> entry : documentQueue.entrySet()){
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
                System.out.println("**********");
                json.clear();
                json.put(entry.getKey(), entry.getValue());
                out.write(json.toString().getBytes(StandardCharsets.UTF_8));
                System.out.println("json.toString(): "+json.toString());
            }
            readResponse(connection);
        } catch (DocViewHubQueueException e) {
            DAO_LOGGER.error("IO Exception occurred.", e);
        } catch (ProtocolException e) {
            DAO_LOGGER.error("Protocol Exception occurred.", e);
        } catch (IOException e) {
            DAO_LOGGER.error("Error reading response.", e);
        }


        System.out.println("Timer task finished at:"+new Date());

    }


    private HttpsURLConnection createRequest(String stringUrl, String jsonData) throws IOException {
        URL url = new URL(null, stringUrl, new sun.net.www.protocol.https.Handler());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        OutputStream out = connection.getOutputStream();
        out.write(jsonData.getBytes(StandardCharsets.UTF_8));
        return connection;
    }
    private void readResponse(HttpsURLConnection connection) throws DocViewHubQueueException, IOException {
        int response = -1;
        String responseMessage = "";
        InputStream in = connection.getInputStream();
        String jsonResponse = IOUtils.toString(in, StandardCharsets.UTF_8.name());
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            Map<Integer, String> responseMap = new Gson().fromJson(jsonResponse, new TypeToken<Map<Integer, String>>() {
            }.getType());
            for (Map.Entry<Integer, String> entry : responseMap.entrySet()) {
                response = entry.getKey();
                responseMessage = entry.getValue();
            }
        }
        if (response == 0) {
            throw new DocViewHubQueueException(responseMessage);
        } else if (response == -1) {
            throw new DocViewHubQueueException("No response");
        }
    }




    synchronized public void addDocumentToQueue(DocumentQueue documentQueue){
        this.documentQueue.put(documentQueue.getId_DocumentQueue(), documentQueue.getJsonData());

        System.out.println("8888:"+documentQueue.getId_DocumentQueue());
        for(Map.Entry<Integer, String> entry : this.documentQueue.entrySet()){
            System.out.println("key-"+entry.getKey());
            System.out.println("val-"+entry.getValue());
            System.out.println("********123**");
        }
    }

    private void getDocumentsFromTable() {
        List<DocumentQueue> documentQueueList = new ArrayList<>();
        try {
            documentQueueList = new DocumentQueueDAO().getDocumentQueues();
            for(DocumentQueue document : documentQueueList){
                documentQueue.put(document.getId_DocumentQueue(), document.getJsonData());
                System.out.println("TEST: "+document.getId_DocumentQueue()+", "+document.getJsonData());
            }
        } catch (DocumentQueueDAOException e) {
            DAO_LOGGER.error("Error: Documents were not successfully received.", e);
        }
    }




}
