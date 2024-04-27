package it.univaq.sose.simplebankingsoapservice.security;

import org.apache.cxf.interceptor.security.JAASLoginInterceptor;
import org.apache.cxf.interceptor.security.callback.CallbackHandlerProvider;
import org.apache.cxf.interceptor.security.callback.CallbackHandlerProviderAuthPol;
import org.apache.cxf.interceptor.security.callback.CallbackHandlerProviderUsernameToken;

import java.util.ArrayList;
import java.util.List;

public class BankJAASLoginInterceptor extends JAASLoginInterceptor {
    public BankJAASLoginInterceptor() {
        super();
        this.setContextName("BankLogin");
        this.setAllowAnonymous(false);
        this.setReportFault(true);
        this.setRoleClassifier("it.univaq.sose.simplebankingsoapservice.security.RolePrincipal");
        this.setRoleClassifierType("classname");

        List<CallbackHandlerProvider> callbackHandlerProviders = new ArrayList<>();
        callbackHandlerProviders.add(new CallbackHandlerProviderAuthPol());
        callbackHandlerProviders.add(new CallbackHandlerProviderUsernameToken());

        this.setCallbackHandlerProviders(callbackHandlerProviders);
    }
}
