package kz.itbc.docviewhub.datebase.DAO;

import kz.itbc.docviewhub.datebase.ConnectionPoolDBCP;
import kz.itbc.docviewhub.entity.DocumentStatus;
import kz.itbc.docviewhub.exception.DocumentStatusDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static kz.itbc.docviewhub.constant.DaoConstant.*;

public class DocumentStatusDAO {
    private static final Logger DAO_LOGGER = LogManager.getRootLogger();
    private final DataSource CONNECTION = ConnectionPoolDBCP.getInstance();

    public DocumentStatus getDocumentStatusById(int id) throws DocumentStatusDAOException {
        DocumentStatus documentStatus = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCUMENT_STATUS_BY_ID_SQL_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documentStatus = initializeDocumentStatus(resultSet);
            }
        } catch (SQLException e) {
            DAO_LOGGER.error(e.getMessage(), e);
            throw new DocumentStatusDAOException("DocumentStatusDAO: Cannot get the document status with ID = " + id);
        }
        if (documentStatus == null) {
            throw new DocumentStatusDAOException("DocumentStatusDAO: Document status with ID = " + id + " not found");
        }
        return documentStatus;
    }

    private DocumentStatus initializeDocumentStatus(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID_DocumentStatus");
        String name = resultSet.getString("Name");
        return new DocumentStatus(id, name);
    }

}