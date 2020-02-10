package kz.itbc.docviewhub.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPoolDBCP {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private static DataSource INSTANCE;

    static {
        try {
            InitialContext ic = new InitialContext();
            INSTANCE = (DataSource) ic.lookup("java:comp/env/jdbc/DocViewHub");
        } catch (NamingException e){
            LOGGER.error(e.getMessage(), e);
        }
    }
    public static DataSource getInstance() {
        return INSTANCE;
    }

    public ConnectionPoolDBCP() { }
}
