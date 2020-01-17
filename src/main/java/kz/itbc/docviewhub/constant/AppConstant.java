package kz.itbc.docviewhub.constant;

public class AppConstant {
    public static final String REGISTRATION_URI = "/register";
    public static final String COMPANIES_URI = "/companies";
    public static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
    public static final String UTF_8_CHARSET = "UTF-8";
    public static final String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";
    public static final String EMPTY_STRING = "";
    public static final int SUCCESS_RESPONSE = 1;
    public static final int FAILURE_RESPONSE = 0;

    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String ERROR_ATTRIBUTE = "error";
    public static final String COMPANIES_ATTRIBUTE = "companies";
    public static final String ID_DOCUMENT_QUEUE_ATTRIBUTE = "ID_DocumentQueue";
    public static final String ID_SENDER_COMPANY_ATTRIBUTE = "ID_SenderCompany";
    public static final String JSON_DATA_ATTRIBUTE = "JsonData";
    public static final String ID_COMPANY_ATTRIBUTE = "ID_Company";

    public static final String NAME_RU_PARAMETER = "name_ru";
    public static final String NAME_KZ_PARAMETER = "name_kz";
    public static final String BIN_PARAMETER = "bin";
    public static final String GOV_ORG_NUM_PARAMETER = "gov_org_num";
    public static final String SERVER_ADDRESS_PARAMETER = "server_address";
    public static final String COMPANY_ID_PARAMETER = "companyID";

    public static final String ADD_COMPANY_PAGE_URI = "/addcompany";
    public static final String DELETE_COMPANY_PAGE_URI = "/deletecompany";
    public static final String EDIT_COMPANY_PAGE_URI = "/editcompany";
    public static final String RECEIVE_DOCUMENT_PAGE_URI = "/senddocument";
    public static final String GET_COMPANY_INFO_AJAX_PAGE_URI = "/getcompanyinfo";


    public static final String ADD_COMPANY_PAGE_JSP = "/WEB-INF/jsp/addcompany.jsp";
    public static final String DELETE_COMPANY_PAGE_JSP = "/WEB-INF/jsp/deletecompany.jsp";
    public static final String EDIT_COMPANY_PAGE_JSP = "/WEB-INF/jsp/editcompany.jsp";


}
