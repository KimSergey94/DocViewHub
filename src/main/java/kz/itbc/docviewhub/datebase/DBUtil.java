package kz.itbc.docviewhub.datebase;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.ResourceBundle;

public class DBUtil {
    private static BasicDataSource dataSource;
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("DBResources");

    public static BasicDataSource getDataSource() {
        if (dataSource==null){
            BasicDataSource ds=new BasicDataSource();
            ds.setUrl(BUNDLE.getString("url"));
            ds.setUsername(BUNDLE.getString("username"));
            ds.setPassword(BUNDLE.getString("password"));
            ds.setMinIdle(Integer.parseInt(BUNDLE.getString("minIdle")));
            ds.setMaxIdle(Integer.parseInt(BUNDLE.getString("maxIdle")));
            ds.setMaxTotal(Integer.parseInt(BUNDLE.getString("maxTotal")));
            ds.setInitialSize(Integer.parseInt(BUNDLE.getString("initialSize")));
            dataSource = ds;
        }
        return dataSource;
    }
}