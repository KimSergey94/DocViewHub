package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class AddCompany implements Service {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(ADD_COMPANY_PAGE_JSP);
        requestDispatcher.forward(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Company> companies;
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        CompanyDAO companyDAO = new CompanyDAO();
        String name_ru = req.getParameter(NAME_RU_PARAMETER);
        String name_kz = req.getParameter(NAME_KZ_PARAMETER);
        String bin = req.getParameter(BIN_PARAMETER);
        String gov_org_num = req.getParameter(GOV_ORG_NUM_PARAMETER);
        String server_address = req.getParameter(SERVER_ADDRESS_PARAMETER);
        Company company = new Company(name_ru, name_kz, bin, gov_org_num, server_address);
        try {
            companyDAO.addCompany(company);
            String jsonRequestData = gson.toJson(company);
            companies = companyDAO.getAllAvailableCompanies();
            for (Company client : companies) {
                String serverAddress = client.getServerAddress() + "/DocViewHub/add-company";
                HttpsURLConnection connection = null;
                try {
                    connection = ConnectionUtil.createRequest(serverAddress, jsonRequestData);
                    ConnectionUtil.readResponse(connection);
                    SERVICE_LOGGER.info("AddCompanyService: Id of the deleted company has been sent to " + client.getNameRU() + " company successfully.");
                } catch (ConnectionUtilException | IOException e) {
                    SERVICE_LOGGER.error("No response from the connection on the " + client.getNameRU() + " company server.", e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                try {
                    Thread.sleep( 1000);
                } catch (InterruptedException e) {
                    SERVICE_LOGGER.error(e.getMessage(), e);
                }
            }
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(MAIN_PAGE_JSP);
            requestDispatcher.forward(req, res);
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.error("Error occurred while adding a company", e);
        }
    }
}
