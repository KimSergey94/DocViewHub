package kz.itbc.docviewhub.exception;

public class CompanyDAOException extends Exception {

    public CompanyDAOException(String message) {
        super(message);
    }

    public CompanyDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
