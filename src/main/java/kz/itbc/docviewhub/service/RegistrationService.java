package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.util.PublicKeySenderUtil;
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
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String responseMessage;
        int responseType;
        CompanyDAO companyDAO = new CompanyDAO();
        InputStream is = req.getInputStream();
        String jsonRequestData = IOUtils.toString(is, StandardCharsets.UTF_8);
        if(jsonRequestData == null || jsonRequestData.isEmpty()){
            responseType = FAILURE_RESPONSE;
            responseMessage = "Сервером не получены регистрационные данные";
            sendResponse(res, responseType, responseMessage);
        } else {
            Company companyPKey = new Gson().fromJson(jsonRequestData, Company.class);
            Company company;
            try {
                company = companyDAO.getCompanyById(companyPKey.getId());
            } catch (CompanyDAOException e){
                SERVICE_LOGGER.error(e.getMessage());
                responseType = FAILURE_RESPONSE;
                responseMessage = "Компания не найдена в системе DocViewHub";
                sendResponse(res, responseType, responseMessage);
                return;
            }
            company.setPublicKeyBase64(companyPKey.getPublicKeyBase64());
            try {
                companyDAO.updateCompany(company);
                Thread thread = new PublicKeySenderUtil(company);
                thread.start();

            } catch (CompanyDAOException e){
                SERVICE_LOGGER.error(e.getMessage());
                responseType = FAILURE_RESPONSE;
                responseMessage = "Возникла ошибка регистрации компании. Обратитесь в поддержу DocViewHub";
                sendResponse(res, responseType, responseMessage);
                return;
            }
            responseType = SUCCESS_RESPONSE;
            responseMessage = "Компания " + company.getNameRU() + " успешно зарегистрировалась в DocViewHub.";
            SERVICE_LOGGER.info(responseMessage);
            sendResponse(res, responseType, responseMessage);
        }
    }

    private void sendResponse(HttpServletResponse res, int responseType, String responseMessage){
        Map<Integer, String> responseData = new HashMap<>();
        responseData.put(responseType, responseMessage);
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
}
