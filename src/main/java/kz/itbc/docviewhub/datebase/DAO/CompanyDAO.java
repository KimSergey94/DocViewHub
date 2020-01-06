package kz.itbc.docviewhub.datebase.DAO;

import kz.itbc.docviewhub.datebase.ConnectionPoolDBCP;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static kz.itbc.docviewhub.constant.DaoConstant.GET_COMPANY_BY_ID_SQL_QUERY;
import static kz.itbc.docviewhub.constant.DaoConstant.UPDATE_COMPANY_SQL_QUERY;

public class CompanyDAO {
    private final BasicDataSource CONNECTION = ConnectionPoolDBCP.getInstance();
    private static final Logger DAO_LOGGER = LogManager.getRootLogger();

    public Company getCompanyById(int id) throws CompanyDAOException {
        Company company = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPANY_BY_ID_SQL_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                company = initializeCompany(resultSet);
            }
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Cannot get company with ID = " + id);
        }
        if (company == null){
            throw new CompanyDAOException("CompanyDAO: Company with ID = " + id + " not found");
        }
        return company;
    }

    private Company initializeCompany(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID_Company");
        String nameRU = resultSet.getString("NameLocaleRU");
        String nameKZ = resultSet.getString("NameLocaleKZ");
        String bin = resultSet.getString("BIN");
        String jsonPublicKey = resultSet.getString("JsonPublicKey");
        String serverAddress = resultSet.getString("ServerAddress");
        boolean isDeleted = resultSet.getBoolean("FlagDeleted");
        return new Company(id, nameRU, nameKZ, bin, jsonPublicKey, serverAddress, isDeleted);
    }

    public void updateCompany(Company company) throws CompanyDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY_SQL_QUERY)) {
            preparedStatement.setString(1, company.getJsonPublicKey());
            preparedStatement.setString(2, company.getServerAddress());
            preparedStatement.setInt(3, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Cannot update company with ID = " + company.getId());
        }
    }

}
