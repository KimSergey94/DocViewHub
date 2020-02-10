package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import kz.itbc.docviewhub.database.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import kz.itbc.docviewhub.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import static kz.itbc.docviewhub.constant.AppConstant.*;

public class DeleteCompanyService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            List<Company> companies = new CompanyDAO().getAllAvailableCompanies();
            req.setAttribute(COMPANIES_ATTRIBUTE, companies);
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error occurred while retrieving available companies.", e);
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(DELETE_COMPANY_PAGE_JSP);
        requestDispatcher.forward(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Company> companies;
        int companyID = Integer.parseInt(req.getParameter(COMPANY_ID_PARAMETER));
        Company company;
        CompanyDAO companyDAO = new CompanyDAO();
        try {
            company = companyDAO.getCompanyById(companyID);
            company.setDeleted(true);
            companyDAO.deleteCompany(company);
            String jsonRequestData = new Gson().toJson(companyID);
            companies = new CompanyDAO().getAllAvailableCompanies();
            for (Company client : companies) {
                String serverAddress = client.getServerAddress() + "/DocViewHub/remove-company";
                HttpsURLConnection connection = null;
                try {
                    connection = ConnectionUtil.createRequest(serverAddress, jsonRequestData);
                    ConnectionUtil.readResponse(connection);
                    SERVICE_LOGGER.info("DeleteCompanyService: Id of the deleted company has been sent to " + client.getNameRU() + " company successfully.");
                } catch (ConnectionUtilException | IOException e) {
                    SERVICE_LOGGER.error("No response from the connection on the " + client.getNameRU() + " company server.", e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                try {
                    Thread.sleep( 1000);
                } catch (InterruptedException e){
                    SERVICE_LOGGER.error(e.getMessage(), e);
                }
            }
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(DELETE_COMPANY_PAGE_JSP);
            requestDispatcher.forward(req, res);
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error(e.getMessage());
        }
    }
}
