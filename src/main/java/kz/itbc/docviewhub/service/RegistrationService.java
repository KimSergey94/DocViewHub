package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import kz.itbc.docviewhub.database.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.util.ConnectionUtil;
import kz.itbc.docviewhub.util.PublicKeySenderUtil;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class RegistrationService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {}

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
            ConnectionUtil.sendResponse(res, responseType, responseMessage);
        } else {
            Company companyPKey = new Gson().fromJson(jsonRequestData, Company.class);
            Company company;
            try {
                company = companyDAO.getCompanyById(companyPKey.getId());
            } catch (CompanyDAOException e){
                SERVICE_LOGGER.error(e.getMessage());
                responseType = FAILURE_RESPONSE;
                responseMessage = "Компания не найдена в системе DocViewHub";
                ConnectionUtil.sendResponse(res, responseType, responseMessage);
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
                ConnectionUtil.sendResponse(res, responseType, responseMessage);
                return;
            }
            responseType = SUCCESS_RESPONSE;
            responseMessage = "Компания " + company.getNameRU() + " успешно зарегистрировалась в DocViewHub.";
            SERVICE_LOGGER.info(responseMessage);
            ConnectionUtil.sendResponse(res, responseType, responseMessage);
        }
    }
}
