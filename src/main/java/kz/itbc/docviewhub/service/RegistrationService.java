package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import static kz.itbc.docviewhub.constant.AppConstant.JSON_CONTENT_TYPE;
import static kz.itbc.docviewhub.constant.AppConstant.UTF_8_CHARSET;

public class RegistrationService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        CompanyDAO companyDAO = new CompanyDAO();
        Map<Integer, String> responseData = new HashMap<>();
        Key publicKey;
        int companyId;
        String serverAddress = getClientIp(req);
        InputStream is = req.getInputStream();
        String jsonRequestData = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        if(serverAddress == null){
            responseData.put(0, "Невозможно определить IP адрес клиента");
            sendResponse(res, responseData);
        } else if(jsonRequestData == null || jsonRequestData.isEmpty()){
            responseData.put(0, "Сервером не получены регистрационные данные");
            sendResponse(res, responseData);
        } else {
            Map<Integer, Key> requestMap = new Gson().fromJson(jsonRequestData, new TypeToken<Map<Integer, String>>() {
            }.getType());
            for (Map.Entry<Integer, Key> entry : requestMap.entrySet()) {
                companyId = entry.getKey();
                publicKey = entry.getValue();
                Company company;
                try {
                    company = companyDAO.getCompanyById(companyId);
                } catch (CompanyDAOException e){
                    SERVICE_LOGGER.error(e.getMessage());
                    responseData.put(0, "Компания не найдена в системе DocViewHub");
                    sendResponse(res, responseData);
                    return;
                }
                String jsonPublicKey = new Gson().toJson(publicKey);
                company.setJsonPublicKey(jsonPublicKey);
                company.setServerAddress(serverAddress);
                try {
                    companyDAO.updateCompany(company);
                } catch (CompanyDAOException e){
                    SERVICE_LOGGER.error(e.getMessage());
                    responseData.put(0, "Возникла ошибка. Обратитесь в поддержу DocViewHub");
                    sendResponse(res, responseData);
                    return;
                }
                SERVICE_LOGGER.info("Компания " + company.getNameRU() + " успешно зарегистрировалась в DocViewHub.");
                responseData.put(1, "Компания " + company.getNameRU() + " успешно зарегистрировалась в DocViewHub.");
                sendResponse(res, responseData);
            }
        }
    }

    private void sendResponse(HttpServletResponse res, Map<Integer, String> responseData){
        res.setContentType(JSON_CONTENT_TYPE);
        res.setCharacterEncoding(UTF_8_CHARSET);
        try (OutputStream os = res.getOutputStream()){
            String jsonResponseData = new Gson().toJson(responseData);
            os.write(jsonResponseData.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e){
            SERVICE_LOGGER.error(e.getMessage(), e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = null;
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
