package kz.itbc.docviewhub.database.DAO;

import kz.itbc.docviewhub.database.ConnectionPoolDBCP;
import kz.itbc.docviewhub.entity.Company;
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
        String nameRU = resultSet.getString("NameRU");
        String nameKZ = resultSet.getString("NameKZ");
        String bin = resultSet.getString("BIN");
        String publicKeyB64 = resultSet.getString("PublicKeyB64");
        String serverAddress = resultSet.getString("ServerAddress");
        String govOrgNumber = resultSet.getString("GovOrgNumber");
        boolean isDeleted = resultSet.getBoolean("FlagDeleted");
        return new Company(id, nameRU, nameKZ, bin, govOrgNumber, publicKeyB64, serverAddress, isDeleted);
    }

    public void updateCompany(Company company) throws CompanyDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY_SQL_QUERY)) {
            preparedStatement.setString(1, company.getPublicKeyBase64());
            preparedStatement.setInt(2, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Could not update the company with ID = " + company.getId());
        }
    }

    public void addCompany(Company company) throws CompanyDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_COMPANY_SQL_QUERY)) {
            preparedStatement.setString(1, company.getNameRU());
            preparedStatement.setString(2, company.getNameKZ());
            preparedStatement.setString(3, company.getBin());
            preparedStatement.setString(4, company.getGovOrgNumber());
            preparedStatement.setString(5, company.getServerAddress());
            preparedStatement.setBoolean(6, company.isDeleted());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                company.setId(resultSet.getInt(1));
            }
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Could not insert the company to database.");
        }
    }

    public void deleteCompany(Company company) throws CompanyDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPANY_SQL_QUERY)) {
            preparedStatement.setBoolean(1, company.isDeleted());
            preparedStatement.setInt(2, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Could not delete the company with ID = " + company.getId());
        }
    }

    public void editCompany(Company company) throws CompanyDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_COMPANY_SQL_QUERY)) {
            preparedStatement.setString(1, company.getNameRU());
            preparedStatement.setString(2, company.getNameKZ());
            preparedStatement.setString(3, company.getBin());
            preparedStatement.setString(4, company.getGovOrgNumber());
            preparedStatement.setString(5, company.getServerAddress());
            preparedStatement.setInt(6, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Could not edit the company with ID = " + company.getId());
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
            e.printStackTrace();
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Cannot get companies");
        }
        return companies;
    }

    public List<Company> getAllAvailableCompanies() throws CompanyDAOException{
        List<Company> companies = new ArrayList<>();
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_AVAILABLE_COMPANIES_SQL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companies.add(initializeCompany(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
            DAO_LOGGER.error(e.getMessage(), e);
            throw new CompanyDAOException("CompanyDAO: Could not get available companies");
        }
        return companies;
    }
}
