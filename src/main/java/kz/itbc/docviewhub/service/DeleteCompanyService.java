package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import kz.itbc.docviewhub.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class DeleteCompanyService implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            List<Company> companies = new CompanyDAO().getAllAvailableCompanies();
            Set<Company> companyHashSet = new HashSet<>(companies);

            req.setAttribute(COMPANIES_ATTRIBUTE, companyHashSet);
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error occurred while retrieving available companies.", e);
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(DELETE_COMPANY_PAGE_JSP);
        requestDispatcher.forward(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Company> companies = null;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JSONObject json = new JSONObject();
        int companyID = Integer.parseInt(req.getParameter(COMPANY_ID_PARAMETER));
        System.out.println("companyID: "+companyID);
        Company company = new Company();
        CompanyDAO companyDAO = new CompanyDAO();
        try {
            company = companyDAO.getCompanyById(companyID);
            company.setDeleted(true);
            companyDAO.deleteCompany(company);
            json.put(ID_COMPANY_ATTRIBUTE, companyID);
            try {
                companies = new CompanyDAO().getAllAvailableCompanies();
            } catch (CompanyDAOException e) {
                SERVICE_LOGGER.info("Could not get available companies", e);
            }
            for (Company client : companies) {
                String jsonRequestData = gson.toJson(json);
                System.out.println("jsonRequestData to remove-company: " + jsonRequestData);
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
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e){
                    SERVICE_LOGGER.error(e.getMessage(), e);
                }
            }
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error occurred while deleting the company with ID = " + companyID, e);
        }
    }
}
