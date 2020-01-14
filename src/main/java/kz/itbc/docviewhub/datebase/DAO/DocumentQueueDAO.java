package kz.itbc.docviewhub.datebase.DAO;

import kz.itbc.docviewhub.datebase.ConnectionPoolDBCP;
import kz.itbc.docviewhub.entity.Company;
import kz.itbc.docviewhub.entity.DocumentQueue;
import kz.itbc.docviewhub.entity.DocumentStatus;
import kz.itbc.docviewhub.exception.CompanyDAOException;
import kz.itbc.docviewhub.exception.DocumentQueueDAOException;
import kz.itbc.docviewhub.exception.DocumentStatusDAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static kz.itbc.docviewhub.constant.DaoConstant.*;

public class DocumentQueueDAO {
    private static final Logger DAO_LOGGER = LogManager.getRootLogger();
    private final DataSource CONNECTION = ConnectionPoolDBCP.getInstance();

    public DocumentQueue getDocumentQueueById(int id) throws DocumentQueueDAOException {
        DocumentQueue documentQueue = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCUMENT_QUEUE_BY_ID_SQL_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documentQueue = initializeDocumentQueue(resultSet);
            }
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new DocumentQueueDAOException("DocumentQueueDAO: Cannot get the documentQueue with ID = " + id);
        }
        if (documentQueue == null){
            throw new DocumentQueueDAOException("DocumentQueueDAO: Document with ID = " + id + " not found");
        }
        return documentQueue;
    }

    public DocumentQueue getDocumentQueueIdByTimestampAndCompaniesIDs(int senderCompanyID, int recipientCompanyID, String jsonData) throws DocumentQueueDAOException {
        DocumentQueue documentQueue = null;
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCUMENT_QUEUE_BY_TIMESTAMP_AND_JSON_SQL_QUERY)) {
            preparedStatement.setString(1, jsonData);
            preparedStatement.setInt(2, senderCompanyID);
            preparedStatement.setInt(3, recipientCompanyID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documentQueue = initializeDocumentQueue(resultSet);
            }
        } catch (SQLException e){
            DAO_LOGGER.error(e.getMessage(), e);
            throw new DocumentQueueDAOException("DocumentQueueDAO: Cannot get the documentQueue with received date and data");
        }
        if (documentQueue == null){
            throw new DocumentQueueDAOException("DocumentQueueDAO: Document with received date and data not found");
        }
        return documentQueue;
    }


    public void addDocumentQueue(DocumentQueue documentQueue) throws DocumentQueueDAOException{
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT_QUEUE_SQL_QUERY)) {
            preparedStatement.setInt(1, documentQueue.getSenderCompany().getId());
            preparedStatement.setInt(2, documentQueue.getRecipientCompany().getId());
            preparedStatement.setString(3, documentQueue.getJsonData());
            preparedStatement.setTimestamp(4, documentQueue.getReceiveDate());
            preparedStatement.setInt(5, documentQueue.getDocumentStatus().getId_DocumentStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DAO_LOGGER.error("DocumentQueueDAO: Error while inserting a document to the table.", e);
            throw new DocumentQueueDAOException("DocumentQueueDAO: Error while inserting a document to the table.");
        }
    }

    public void updateDocumentQueueSendDate(DocumentQueue documentQueue) throws DocumentQueueDAOException{
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DOCUMENT_QUEUE_SQL_QUERY)) {
            preparedStatement.setTimestamp(1, documentQueue.getSendDate());
            preparedStatement.setInt(2, documentQueue.getDocumentStatus().getId_DocumentStatus());
            preparedStatement.setInt(3, documentQueue.getId_DocumentQueue());
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            DAO_LOGGER.error("DocumentQueueDAO: Error while updating a document in the document queue table.", e);
            throw new DocumentQueueDAOException("DocumentQueueDAO: Error while updating a document in the document queue table.");
        }
    }

    private DocumentQueue initializeDocumentQueue(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID_DocumentQueue");
        String jsonData = resultSet.getString("JsonData");
        Timestamp receiveDate = resultSet.getTimestamp("RecieveDate");
        Timestamp sendDate = resultSet.getTimestamp("SendDate");
        int senderCompanyID = resultSet.getInt("ID_SenderCompany");
        int addresseeCompanyID = resultSet.getInt("ID_RecipientCompany");
        int documentStatusID = resultSet.getInt("ID_DocumentStatus");
        Company senderCompany = null;
        Company addresseeCompany = null;
        DocumentStatus documentStatus = null;
        try {
            senderCompany = new CompanyDAO().getCompanyById(senderCompanyID);
            documentStatus = new DocumentStatusDAO().getDocumentStatusById(documentStatusID);
        } catch (CompanyDAOException e) {
            DAO_LOGGER.error("Error occurred while getting the company with id = "+senderCompanyID, e);
        } catch (DocumentStatusDAOException e) {
            DAO_LOGGER.error("Document status error", e);
        }
        try {
            addresseeCompany = new CompanyDAO().getCompanyById(addresseeCompanyID);
        } catch (CompanyDAOException e) {
            DAO_LOGGER.error("Error occurred while getting the company with id = "+addresseeCompanyID, e);
        }
        return new DocumentQueue(id, senderCompany, addresseeCompany, jsonData, receiveDate, sendDate, documentStatus);
    }


    public List<DocumentQueue> getDocumentQueues() throws DocumentQueueDAOException{
        List<DocumentQueue> documentQueues = new ArrayList<>();
        try (Connection connection = CONNECTION.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DOCUMENT_QUEUES_BY_ID_SQL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documentQueues.add(initializeDocumentQueue(resultSet));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            DAO_LOGGER.error(e.getMessage(), e);
            throw new DocumentQueueDAOException("DocumentQueueDAO: Could not get the documents in the queue");
        }
        return documentQueues;
    }
}
