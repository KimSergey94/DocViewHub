package kz.itbc.docviewhub.service;

import com.google.gson.*;
import kz.itbc.docviewhub.database.DAO.CompanyDAO;
import kz.itbc.docviewhub.database.DAO.DocumentQueueDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.entity.DocumentQueue;
import kz.itbc.docviewhub.entity.DocumentStatus;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.DocumentQueueDAOException;
import kz.itbc.docviewhub.util.ConnectionUtil;
import kz.itbc.docviewhub.util.DocViewHubQueue;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class ReceiveDocumentService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        doPost(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String responseMessage;
        int responseType;
        CompanyDAO companyDAO = new CompanyDAO();
        DocumentQueueDAO documentQueueDAO = new DocumentQueueDAO();
        InputStream is = req.getInputStream();
        String stringJsonDoc = IOUtils.toString(is, StandardCharsets.UTF_8);
        SERVICE_LOGGER.info("ReceiveDocumentService: Request json " + stringJsonDoc);
        if (stringJsonDoc == null || stringJsonDoc.isEmpty()) {
            SERVICE_LOGGER.error("The server has not received the document data.");
            responseType = FAILURE_RESPONSE;
            responseMessage = "Сервером не получены данные документа";
            ConnectionUtil.sendResponse(res, responseType, responseMessage);
        } else {
            DocumentQueue documentQueue = new DocumentQueue();
            int ID_SenderCompany = 0;
            int ID_RecipientCompany = 0;
            int ID_ClientDocumentQueue = 0;
            String jsonData = "";
            String aes = "";
            Company senderCompany;
            Company recipientCompany;
            JsonElement element = new JsonParser().parse(stringJsonDoc);
            JsonObject obj = element.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries) {
                switch (entry.getKey()) {
                    case ID_SENDER_COMPANY_ATTRIBUTE:
                        ID_SenderCompany = entry.getValue().getAsInt();
                        break;
                    case ID_RECIPIENT_COMPANY_ATTRIBUTE:
                        ID_RecipientCompany = entry.getValue().getAsInt();
                        break;
                    case JSON_DATA_ATTRIBUTE:
                        jsonData = entry.getValue().getAsString();
                        break;
                    case ID_CLIENTDOCUMENTQUEUE_ATTRIBUTE:
                        ID_ClientDocumentQueue = entry.getValue().getAsInt();
                        break;
                    case AES_ATTRIBUTE:
                        aes = entry.getValue().getAsString();
                        break;
                }
            }
            try {
                if (companyDAO.getCompanyById(ID_SenderCompany) != null || companyDAO.getCompanyById(ID_RecipientCompany) != null) {
                    SERVICE_LOGGER.error("Either the recipient or sender company has not been found or is deleted.");
                    responseType = FAILURE_RESPONSE;
                    responseMessage = "Компания получателя или отправителя не была найдена в базе данных либо была удалена";
                    ConnectionUtil.sendResponse(res, responseType, responseMessage);
                } else {
                    try {
                        senderCompany = companyDAO.getCompanyById(ID_SenderCompany);
                        recipientCompany = companyDAO.getCompanyById(ID_RecipientCompany);
                        documentQueue.setSenderCompany(senderCompany);
                        documentQueue.setRecipientCompany(recipientCompany);
                        documentQueue.setJsonData(jsonData);
                        documentQueue.setDocumentStatus(new DocumentStatus(1, "В очереди"));
                        documentQueue.setId_ClientDocumentQueue(ID_ClientDocumentQueue);
                        Timestamp ts = new Timestamp(System.currentTimeMillis());
                        documentQueue.setReceiveDate(ts);
                        documentQueue.setAes(aes);
                        documentQueueDAO.addDocumentQueue(documentQueue);
                        DocViewHubQueue docViewHubQueue = DocViewHubQueue.getInstance();
                        docViewHubQueue.addDocumentToQueue(documentQueue);
                        SERVICE_LOGGER.info("Document is successfully added to the database and document queue.");
                        responseType = SUCCESS_RESPONSE;
                        responseMessage = "Документ успешно добавлен в базу данных и очередь документов";
                        ConnectionUtil.sendResponse(res, responseType, responseMessage);
                    } catch (DocumentQueueDAOException e) {
                        SERVICE_LOGGER.error("Error occurred while inserting the document in the document queue table.", e);
                        responseType = FAILURE_RESPONSE;
                        responseMessage = "Возникла ошибка добавления документа в базу данных. Обратитесь в поддержу DocViewHub";
                        ConnectionUtil.sendResponse(res, responseType, responseMessage);
                    }
                }
            } catch (CompanyDAOException e) {
                SERVICE_LOGGER.error(e.getMessage());
                responseType = FAILURE_RESPONSE;
                responseMessage = "Ошибка получения компаний получателя и отправителя";
                ConnectionUtil.sendResponse(res, responseType, responseMessage);
            }
        }
    }
}
