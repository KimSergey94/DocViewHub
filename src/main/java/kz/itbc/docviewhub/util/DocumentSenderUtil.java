package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.util.List;

public class DocumentSenderUtil extends Thread {
    private static final Logger UTIL_LOGGER = LogManager.getRootLogger();
    private Company company;

    public DocumentSenderUtil(Company company) {
        this.company = company;
    }

    @Override
    public void run() {
        sendDocumentToClients();
    }

    private void sendDocumentToClients() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        List<Company> companies = null;
        try {
            companies = new CompanyDAO().getAllAvailableCompanies();
        } catch (CompanyDAOException e) {
            UTIL_LOGGER.info("Could not get available companies", e);
        }
        for (Company company : companies) {
            String jsonRequestData = gson.toJson(this.company);
            System.out.println(jsonRequestData);
            String serverAddress = company.getServerAddress() + "/DocViewHub/update-company";
            HttpsURLConnection connection = null;
            try {
                connection = ConnectionUtil.createRequest(serverAddress, jsonRequestData);
                ConnectionUtil.readResponse(connection);
                UTIL_LOGGER.info("PublicKeySenderUtil: Public key is sent to " + company.getNameRU() + " company");
            } catch (ConnectionUtilException | IOException e) {
                UTIL_LOGGER.error("No response from the connection", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e){
                UTIL_LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
