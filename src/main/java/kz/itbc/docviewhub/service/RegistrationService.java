package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.DocViewHubCompany;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class RegistrationService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String responseMessage;
        int responseType;
        CompanyDAO companyDAO = new CompanyDAO();
        int companyId;
        String keyBase64;
        String serverAddress = getClientIp(req);
        InputStream is = req.getInputStream();
        String jsonRequestData = IOUtils.toString(is, StandardCharsets.UTF_8);
        if(serverAddress == null){
            responseType = FAILURE_RESPONSE;
            responseMessage = "Невозможно определить IP адрес клиента";
            sendResponse(res, responseType, responseMessage);
        } else if(jsonRequestData == null || jsonRequestData.isEmpty()){
            responseType = FAILURE_RESPONSE;
            responseMessage = "Сервером не получены регистрационные данные";
            sendResponse(res, responseType, responseMessage);
        } else {
            Map<Integer, String> requestMap = new Gson().fromJson(jsonRequestData, new TypeToken<Map<Integer, String>>() {
            }.getType());
            for (Map.Entry<Integer, String> entry : requestMap.entrySet()) {
                companyId = entry.getKey();
                keyBase64 = entry.getValue();
                DocViewHubCompany docViewHubCompany;
                try {
                    docViewHubCompany = companyDAO.getCompanyById(companyId);
                } catch (CompanyDAOException e){
                    SERVICE_LOGGER.error(e.getMessage());
                    responseType = FAILURE_RESPONSE;
                    responseMessage = "Компания не найдена в системе DocViewHub";
                    sendResponse(res, responseType, responseMessage);
                    return;
                }
                docViewHubCompany.setPublicKeyBase64(keyBase64);
                docViewHubCompany.setServerAddress(serverAddress);
                try {
                    companyDAO.updateCompany(docViewHubCompany);
                } catch (CompanyDAOException e){
                    SERVICE_LOGGER.error(e.getMessage());
                    responseType = FAILURE_RESPONSE;
                    responseMessage = "Возникла ошибка. Обратитесь в поддержу DocViewHub";
                    sendResponse(res, responseType, responseMessage);
                    return;
                }
                responseType = SUCCESS_RESPONSE;
                responseMessage = "Компания " + docViewHubCompany.getNameRU() + " успешно зарегистрировалась в DocViewHub.";
                SERVICE_LOGGER.info(responseMessage);
                sendResponse(res, responseType, responseMessage);
            }
        }
    }

    private void sendResponse(HttpServletResponse res, int responseType, String responseMessage){
        Map<Integer, String> responseData = new HashMap<>();
        responseData.put(responseType, responseMessage);
        System.out.println(responseType);
        System.out.println(responseMessage);
        res.setContentType(JSON_CONTENT_TYPE);
        res.setCharacterEncoding(UTF_8_CHARSET);
        try (OutputStream os = res.getOutputStream()){
            String jsonResponseData = new Gson().toJson(responseData);
            os.write(jsonResponseData.getBytes(StandardCharsets.UTF_8));
            os.close();
            SERVICE_LOGGER.info(responseType + " " + responseMessage);
        } catch (IOException e){
            SERVICE_LOGGER.error(e.getMessage(), e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = null;
        if (request != null) {
            remoteAddr = request.getHeader(X_FORWARDED_FOR_HEADER);
            if (remoteAddr == null || remoteAddr.equals(EMPTY_STRING)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
