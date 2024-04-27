package it.univaq.sose.simplebankingsoapservice.util;

import it.univaq.sose.simplebankingsoapservice.domain.Role;
import it.univaq.sose.simplebankingsoapservice.security.AccountDetails;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.apache.cxf.interceptor.security.RolePrefixSecurityContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.WebServiceContext;

public final class AuthenticationUtils {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationUtils.class);

    private AuthenticationUtils() {
    }

    public static AccountDetails getAuthenticationDetails(WebServiceContext wsContext) {
        LOG.info("wsContext {}", wsContext);
        LOG.info("wsContext.getUserPrincipal() {}", wsContext.getUserPrincipal());
        LOG.info("wsContext.getMessageContext() {}", wsContext.getMessageContext());
        LOG.info("wsContext.getMessageContext().get() {}", wsContext.getMessageContext().get("org.apache.cxf.security.SecurityContext"));
        LOG.info("wsContext.getMessageContext().get().getUserRoles {}", ((RolePrefixSecurityContextImpl) wsContext.getMessageContext().get("org.apache.cxf.security.SecurityContext")).getUserRoles());
        RolePrefixSecurityContextImpl securityContext = (RolePrefixSecurityContextImpl) wsContext.getMessageContext().get("org.apache.cxf.security.SecurityContext");
        if (securityContext != null && securityContext.getUserPrincipal() != null && !securityContext.getUserRoles().isEmpty()) {
            return new AccountDetails(securityContext.getUserPrincipal().getName(), Role.valueOf(securityContext.getUserRoles().stream().findFirst().get().getName()));
        }
        throw new AuthenticationException("You are not authorised to make this request");
    }
}
