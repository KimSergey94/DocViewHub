package kz.itbc.docviewhub.datebase;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionPoolDBCP {

    private static final BasicDataSource INSTANCE = DBUtil.getDataSource();
    public static BasicDataSource getInstance() {
        return INSTANCE;
    }

    public ConnectionPoolDBCP() { }
}
