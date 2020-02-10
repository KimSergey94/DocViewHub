package kz.itbc.docviewhub.service;

import java.util.HashMap;
import java.util.Map;

import static kz.itbc.docviewhub.constant.AppConstant.*;

public class ServiceFactory {
    private static final Map<String, Service> SERVICE_MAP = new HashMap<>();
    private static final ServiceFactory SERVICE_FACTORY = new ServiceFactory();

    private ServiceFactory() {
        init();
    }

    private void init() {
        SERVICE_MAP.put(REGISTRATION_URI, new RegistrationService());
        SERVICE_MAP.put(COMPANIES_URI, new CompaniesService());
        SERVICE_MAP.put(ADD_COMPANY_PAGE_URI, new AddCompany());
        SERVICE_MAP.put(RECEIVE_DOCUMENT_PAGE_URI, new ReceiveDocumentService());
        SERVICE_MAP.put(GET_COMPANY_INFO_AJAX_PAGE_URI, new AJAXGetCompanyInfoService());
        SERVICE_MAP.put(DELETE_COMPANY_PAGE_URI, new DeleteCompanyService());
        SERVICE_MAP.put(EDIT_COMPANY_PAGE_URI, new EditCompanyService());
        SERVICE_MAP.put(MAIN_PAGE_URI, new MainService());
        SERVICE_MAP.put(LOGIN_PAGE_URI, new LoginService());
    }

    public static ServiceFactory getInstance() {
        return SERVICE_FACTORY;
    }

    public Service getService(String request) {
        return SERVICE_MAP.get(request);
    }
}
