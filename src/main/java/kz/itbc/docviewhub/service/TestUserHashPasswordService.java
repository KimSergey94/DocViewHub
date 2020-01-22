package kz.itbc.docviewhub.service;

import kz.itbc.docviewhub.datebase.DAO.UserDAO;
import kz.itbc.docviewhub.entity.User;
import kz.itbc.docviewhub.exception.UserDAOException;
import kz.itbc.docviewhub.security.Security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class TestUserHashPasswordService implements Service {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        /*UserDAO userDAO = new UserDAO();
        String login = "admin";//req.getParameter(LOGIN_PARAMETER);
        String password = "admin";// req.getParameter(PASSWORD_PARAMETER);

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(Security.crypt(password));
        try {
            userDAO.addUser(newUser);
        } catch (UserDAOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

    }
}