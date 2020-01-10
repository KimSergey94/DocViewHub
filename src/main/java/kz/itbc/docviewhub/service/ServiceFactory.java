package kz.itbc.docviewhub.service;

import java.util.HashMap;
import java.util.Map;

import static kz.itbc.docviewhub.constant.AppConstant.COMPANIES_URI;
import static kz.itbc.docviewhub.constant.AppConstant.REGISTRATION_URI;

public class ServiceFactory {
    private static final Map<String, Service> SERVICE_MAP = new HashMap<>();
    private static final ServiceFactory SERVICE_FACTORY = new ServiceFactory();

    private ServiceFactory() {
        init();
    }

    private void init() {
        SERVICE_MAP.put(REGISTRATION_URI, new RegistrationService());
        SERVICE_MAP.put(COMPANIES_URI, new CompaniesService());
    }

    public static ServiceFactory getInstance() {
        return SERVICE_FACTORY;
    }

    public Service getService(String request) {
        return SERVICE_MAP.get(request);
    }
}
