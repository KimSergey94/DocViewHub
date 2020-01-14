package kz.itbc.docviewhub.exception;

public class DocumentStatusDAOException extends Exception {

    public DocumentStatusDAOException(String message) {
        super(message);
    }

    public DocumentStatusDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
