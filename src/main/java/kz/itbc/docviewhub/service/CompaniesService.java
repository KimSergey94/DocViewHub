package kz.itbc.docviewhub.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.itbc.docviewhub.datebase.DAO.CompanyDAO;
import kz.itbc.docviewhub.entity.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static kz.itbc.docviewhub.constant.AppConstant.JSON_CONTENT_TYPE;
import static kz.itbc.docviewhub.constant.AppConstant.UTF_8_CHARSET;

public class CompaniesService  implements Service{
    private static final Logger SERVICE_LOGGER = LogManager.getRootLogger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res){
        res.setContentType(JSON_CONTENT_TYPE);
        res.setCharacterEncoding(UTF_8_CHARSET);
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        try (OutputStream os = res.getOutputStream()){
            List<Company> responseData = new CompanyDAO().getAllCompanies();
            SERVICE_LOGGER.info("3");
            SERVICE_LOGGER.info("size" + responseData.size());
            String jsonResponseData = gson.toJson(responseData);
            os.write(jsonResponseData.getBytes(StandardCharsets.UTF_8));
            SERVICE_LOGGER.info("4");
        } catch (Exception e){
            SERVICE_LOGGER.info("5");
            SERVICE_LOGGER.error(e.getMessage(), e);
        }
    }
}
