package kz.itbc.docviewhub.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kz.itbc.docviewhub.exception.ConnectionUtilException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static kz.itbc.docviewhub.constant.AppConstant.JSON_CONTENT_TYPE;
import static kz.itbc.docviewhub.constant.AppConstant.UTF_8_CHARSET;

public class ConnectionUtil {
    private static final Logger UTIL_LOGGER = LogManager.getRootLogger();

    public static HttpsURLConnection createRequest(String stringUrl, String jsonData) throws IOException {
        URL url = new URL(null, stringUrl, new sun.net.www.protocol.https.Handler());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setHostnameVerifier((hostname, sslSession) -> true);
        OutputStream out = connection.getOutputStream();
        out.write(jsonData.getBytes(StandardCharsets.UTF_8));
        return connection;
    }

    public static void readResponse(HttpsURLConnection connection) throws ConnectionUtilException, IOException {
        int response = -1;
        String responseMessage = "";
        InputStream in = connection.getInputStream();
        String jsonResponse = IOUtils.toString(in, StandardCharsets.UTF_8.name());
        UTIL_LOGGER.info("ConnectionUtil: Request json " + jsonResponse);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            Map<Integer, String> responseMap = new Gson().fromJson(jsonResponse, new TypeToken<Map<Integer, String>>() {
            }.getType());
            for (Map.Entry<Integer, String> entry : responseMap.entrySet()) {
                response = entry.getKey();
                responseMessage = entry.getValue();
            }
        }
        if (response == 0) {
            UTIL_LOGGER.error("ConnectionUtil: The response code and message are as follows: " + response + ": " + responseMessage);
            throw new ConnectionUtilException(responseMessage);
        } else if (response == -1) {
            UTIL_LOGGER.error("ConnectionUtil: The response code and message are as follows: " + response + ": " + responseMessage);
            throw new ConnectionUtilException("No response");
        } else if (response == 1) {
            UTIL_LOGGER.info("ConnectionUtil: The response code and message are as follows: " + response + ": " + responseMessage);
        }
    }

    public static void sendResponse(HttpServletResponse res, int responseType, String responseMessage){
        Map<Integer, String> responseData = new HashMap<>();
        responseData.put(responseType, responseMessage);
        res.setContentType(JSON_CONTENT_TYPE);
        res.setCharacterEncoding(UTF_8_CHARSET);
        try (OutputStream os = res.getOutputStream()){
            String jsonResponseData = new Gson().toJson(responseData);
            os.write(jsonResponseData.getBytes(StandardCharsets.UTF_8));
            os.close();
            UTIL_LOGGER.info(responseType + " " + responseMessage);
        } catch (IOException e){
            UTIL_LOGGER.error(e.getMessage(), e);
        }
    }
}
