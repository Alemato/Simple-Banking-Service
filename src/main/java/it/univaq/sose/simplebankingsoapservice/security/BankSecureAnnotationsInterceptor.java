package it.univaq.sose.simplebankingsoapservice.security;

import org.apache.cxf.interceptor.security.SecureAnnotationsInterceptor;

public class BankSecureAnnotationsInterceptor extends SecureAnnotationsInterceptor {
    public BankSecureAnnotationsInterceptor(Object object) {
        super();
        this.setSecuredObject(object);
    }
}
