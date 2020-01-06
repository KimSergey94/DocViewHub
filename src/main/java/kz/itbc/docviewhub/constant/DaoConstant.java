package kz.itbc.docviewhub.constant;

public class DaoConstant {
    public static final String GET_COMPANY_BY_ID_SQL_QUERY = "SELECT * FROM Companies " +
            "WHERE ID_Company = ? AND FlagDeleted = 0";
    public static final String UPDATE_COMPANY_SQL_QUERY = "UPDATE Companies " +
            "SET JsonPublicKey = ?,  ServerAddress = ? WHERE ID_Company = ?";


}
