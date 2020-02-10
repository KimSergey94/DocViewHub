package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.itbc.docviewhub.database.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import kz.itbc.docviewhub.exception.PublicKeySenderUtilException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.util.List;

public class PublicKeySenderUtil extends Thread {
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();
    private Company company;

    public PublicKeySenderUtil(Company company) {
        this.company = company;
    }

    @Override
    public void run() {
        try {
            sendPublicKeyToClient();
        } catch (PublicKeySenderUtilException e) {
            SERVICE_LOGGER.error(e.getMessage());
        }
    }

    private void sendPublicKeyToClient() throws PublicKeySenderUtilException {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        List<Company> companies;
        try {
            companies = new CompanyDAO().getAllAvailableCompanies();
        } catch (CompanyDAOException e) {
            SERVICE_LOGGER.info(e.getMessage(), e);
            throw new PublicKeySenderUtilException("Could not get available companies");
        }
        String jsonRequestData = gson.toJson(this.company);
        HttpsURLConnection connection = null;
        for (Company company : companies) {
            String serverAddress = company.getServerAddress() + "/DocViewHub/update-company";
            try {
                connection = ConnectionUtil.createRequest(serverAddress, jsonRequestData);
                ConnectionUtil.readResponse(connection);
                SERVICE_LOGGER.info("PublicKeySenderUtil: Public key of " + this.company.getNameRU() + " is sent to " + company.getNameRU() + " company");
            } catch (ConnectionUtilException | IOException e) {
                SERVICE_LOGGER.error("No response from the connection", e);
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
    }
}
