package kz.itbc.docviewhub.constant;

public class DaoConstant {
    public static final String GET_COMPANY_BY_ID_SQL_QUERY = "SELECT * FROM Companies " +
            "WHERE ID_Company = ? AND FlagDeleted = 0";
    public static final String GET_ALL_COMPANIES_SQL_QUERY = "SELECT * FROM Companies";
    public static final String GET_ALL_AVAILABLE_COMPANIES_SQL_QUERY = "SELECT * FROM Companies WHERE FlagDeleted = 0";
    public static final String UPDATE_COMPANY_SQL_QUERY = "UPDATE Companies SET PublicKeyB64 = ? WHERE ID_Company = ?";
    public static final String DELETE_COMPANY_SQL_QUERY = "UPDATE Companies SET FlagDeleted = ? WHERE ID_Company = ?";
    public static final String ADD_COMPANY_SQL_QUERY = "INSERT INTO Companies (NameRU, NameKZ, BIN, GovOrgNumber, ServerAddress, FlagDeleted) OUTPUT inserted.ID_Company VALUES (?, ?, ?, ?, ?, ?)";
    public static final String EDIT_COMPANY_SQL_QUERY = "UPDATE Companies SET NameRU = ?, NameKZ = ?, BIN = ?, GovOrgNumber = ?," +
            " ServerAddress = ? WHERE ID_Company = ?";
    public static final String GET_COMPANY_ID_SQL_QUERY = "SELECT * FROM Companies WHERE NameRU = ? and NameKZ = ? and " +
            "BIN = ? and GovOrgNumber = ? and ServerAddress = ?";


    public static final String GET_DOCUMENT_STATUS_BY_ID_SQL_QUERY = "SELECT * FROM DocumentStatus WHERE ID_DocumentStatus = ?";


    public static final String GET_DOCUMENT_QUEUE_BY_ID_SQL_QUERY = "SELECT * FROM DocumentQueue WHERE ID_DocumentQueue = ?";
    public static final String GET_ALL_DOCUMENT_QUEUES_BY_ID_SQL_QUERY = "SELECT * FROM DocumentQueue WHERE ID_DocumentStatus = 1 and FlagDeleted = 0";
    public static final String INSERT_DOCUMENT_QUEUE_SQL_QUERY = "INSERT INTO DocumentQueue (ID_SenderCompany, ID_RecipientCompany, " +
            "JsonData, RecieveDate, ID_DocumentStatus, ID_ClientDocumentQueue, FlagDeleted, AES) OUTPUT inserted.ID_DocumentQueue VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_DOCUMENT_QUEUE_SQL_QUERY = "UPDATE DocumentQueue " +
            "SET SendDate = ?, ID_DocumentStatus = ? WHERE ID_DocumentQueue = ?";
    public static final String UPDATE_DOCUMENT_QUEUE_STATUS_SQL_QUERY = "UPDATE DocumentQueue " +
            "SET ID_DocumentStatus = ?, FlagDeleted = ? WHERE ID_DocumentQueue = ?";
    public static final String GET_DOCUMENT_QUEUE_BY_TIMESTAMP_AND_JSON_SQL_QUERY = "SELECT * FROM DocumentQueue WHERE JsonData = ? and ID_SenderCompany = ? and ID_RecipientCompany = ?";


    public static final String GET_USER_BY_ID_SQL_QUERY = "SELECT * FROM [User] WHERE ID_User = ?";
    public static final String GET_USER_BY_LOGIN_SQL_QUERY = "SELECT * FROM [User] WHERE Login = ? ";
    public static final String ADD_USER_SQL_QUERY = "INSERT INTO [User] (Login, Password) VALUES (?, ?)";


}
