package kz.itbc.docviewhub.datebase.DAO;

import kz.itbc.docviewhub.datebase.ConnectionPoolDBCP;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.entity.DocViewHubCompany;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static kz.itbc.docviewhub.constant.DaoConstant.*;

public class CompanyDAO {
    private static final Logger DAO_LOGGER = LogManager.getRootLogger();
    private final DataSource CONNECTION = ConnectionPoolDBCP.getInstance();

    public DocViewHubCompany getCompanyById(int id) throws CompanyDAOException {
        DocViewHubCompany docViewHubCompany = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPANY_BY_ID_SQL_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                docViewHubCompany = initializeCompany(resultSet);
            }
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Cannot get company with ID = " + id);
        }
        if (docViewHubCompany == null){
            throw new CompanyDAOException("CompanyDAO: Company with ID = " + id + " not found");
        }
        return docViewHubCompany;
    }

    private DocViewHubCompany initializeCompany(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID_Company");
        String nameRU = resultSet.getString("NameRU");
        String nameKZ = resultSet.getString("NameKZ");
        String bin = resultSet.getString("BIN");
        String publicKeyB64 = resultSet.getString("PublicKeyB64");
        String serverAddress = resultSet.getString("ServerAddress");
        boolean isDeleted = resultSet.getBoolean("FlagDeleted");
        return new DocViewHubCompany(id, nameRU, nameKZ, bin, publicKeyB64, serverAddress, isDeleted);
    }

    public void updateCompany(DocViewHubCompany docViewHubCompany) throws CompanyDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY_SQL_QUERY)) {
            preparedStatement.setString(1, docViewHubCompany.getPublicKeyBase64());
            preparedStatement.setString(2, docViewHubCompany.getServerAddress());
            preparedStatement.setInt(3, docViewHubCompany.getIdDocViewHub());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Cannot update company with ID = " + docViewHubCompany.getIdDocViewHub());
        }
    }

    public List<Company> getAllCompanies() throws CompanyDAOException{
        List<Company> companies = new ArrayList<>();
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_COMPANIES_SQL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companies.add(initializeCompany(resultSet));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Cannot get companies");
        }
        return companies;
    }
}
