package kz.itbc.docviewhub.datebase.DAO;

import kz.itbc.docviewhub.datebase.ConnectionPoolDBCP;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.entity.User;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.UserDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static kz.itbc.docviewhub.constant.DaoConstant.*;

public class UserDAO {
    private static final Logger DAO_LOGGER = LogManager.getRootLogger();
    private final DataSource CONNECTION = ConnectionPoolDBCP.getInstance();

    public User getUserById(int id) throws UserDAOException {
        User user = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID_SQL_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = initializeUser(resultSet);
            }
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new UserDAOException("UserDAO: Cannot get the user with ID = " + id);
        }
        if (user == null){
            throw new UserDAOException("UserDAO: The user with ID = " + id + " not found");
        }
        return user;
    }

    public User getUserByLogin(String login) throws UserDAOException {
        User user = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN_SQL_QUERY)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = initializeUser(resultSet);
            }
            connection.commit();
        } catch (SQLException e){
            DAO_LOGGER.error("Ошибка получения пользователя с логином " + login, e);
            throw new UserDAOException("Ошибка получения пользователя с логином " + login, e);
        }

        if(user == null){
            DAO_LOGGER.info("Пользователь с логином " + login + " не найден");
            throw new UserDAOException("Пользователь с логином " + login + " не найден");
        }
        return user;
    }

    private User initializeUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID_User");
        String login = resultSet.getString("Login");
        String password = resultSet.getString("Password");
        return new User(id, login, password);
    }

    public void addUser(User user) throws UserDAOException {
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_SQL_QUERY)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            DAO_LOGGER.error("Ошибка добавления пользователя с логином " + user.getLogin(), e);
            throw new UserDAOException("Ошибка добавления пользователя с логином " + user.getLogin(), e);
        }
    }
}
