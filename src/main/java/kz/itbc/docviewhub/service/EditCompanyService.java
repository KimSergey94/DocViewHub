package kz.itbc.docviewhub.service;

import kz.itbc.docviewhub.database.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class EditCompanyService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            List<Company> companies = new CompanyDAO().getAllAvailableCompanies();
            req.setAttribute(COMPANIES_ATTRIBUTE, companies);
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error occurred while retrieving available companies.", e);
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(EDIT_COMPANY_PAGE_JSP);
        requestDispatcher.forward(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int companyID = Integer.parseInt(req.getParameter(COMPANY_ID_PARAMETER));
        Company company;
        CompanyDAO companyDAO = new CompanyDAO();
        String name_ru = req.getParameter(NAME_RU_PARAMETER);
        String name_kz = req.getParameter(NAME_KZ_PARAMETER);
        String bin = req.getParameter(BIN_PARAMETER);
        String gov_org_num = req.getParameter(GOV_ORG_NUM_PARAMETER);
        String server_address = req.getParameter(SERVER_ADDRESS_PARAMETER);
        try {
            company = companyDAO.getCompanyById(companyID);
            company.setNameRU(name_ru);
            company.setNameKZ(name_kz);
            company.setBin(bin);
            company.setGovOrgNumber(gov_org_num);
            company.setServerAddress(server_address);
            companyDAO.editCompany(company);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(EDIT_COMPANY_PAGE_JSP);
            requestDispatcher.forward(req, res);
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error occurred while deleting the company with ID = " + companyID, e);
        }
    }
}
