package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import kz.itbc.docviewhub.database.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class AJAXGetCompanyInfoService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)  {}

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String> message = new HashMap<>();
        Company company;
        int companyID = Integer.parseInt(req.getParameter(COMPANY_ID_PARAMETER));
        try{
            company = new CompanyDAO().getCompanyById(companyID);
            message.put(COMPANY_ID_PARAMETER, String.valueOf(company.getId()));
            message.put(NAME_RU_PARAMETER, company.getNameRU());
            message.put(NAME_KZ_PARAMETER, company.getNameKZ());
            message.put(BIN_PARAMETER, company.getBin());
            message.put(GOV_ORG_NUM_PARAMETER, company.getGovOrgNumber());
            message.put(SERVER_ADDRESS_PARAMETER, company.getServerAddress());
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error getting the company with ID = " + companyID, e);
        }
        res.setContentType(JSON_CONTENT_TYPE);
        res.setCharacterEncoding(UTF_8_CHARSET);
        PrintWriter writer = res.getWriter();
        String json = new Gson().toJson(message);
        writer.write(json);
    }
}
