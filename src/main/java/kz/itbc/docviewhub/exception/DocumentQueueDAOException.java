package kz.itbc.docviewhub.exception;

public class DocumentQueueDAOException extends Exception {

    public DocumentQueueDAOException(String message) {
        super(message);
    }

    public DocumentQueueDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
