package kz.itbc.docviewhub.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Service {

    void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException;

    void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException;
}
