package kz.itbc.docviewhub.constant;

public class DaoConstant {
    public static final String GET_COMPANY_BY_ID_SQL_QUERY = "SELECT * FROM Companies " +
            "WHERE ID_Company = ? AND FlagDeleted = 0";
    public static final String GET_ALL_COMPANIES_SQL_QUERY = "SELECT * FROM Companies " +
            "WHERE FlagDeleted = 0";
    public static final String UPDATE_COMPANY_SQL_QUERY = "UPDATE Companies " +
            "SET PublicKeyB64 = ?,  ServerAddress = ? WHERE ID_Company = ?";


}
