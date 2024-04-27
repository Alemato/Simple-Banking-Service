package it.univaq.sose.simplebankingsoapservice.security;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.repository.AccountRepository;
import it.univaq.sose.simplebankingsoapservice.util.JAASUtils;
import it.univaq.sose.simplebankingsoapservice.webservice.NotFoundException;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class BankLoginModule implements LoginModule {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(BankLoginModule.class);

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    private boolean debug;

    private boolean success = false;
    private Account account;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        this.debug = Boolean.parseBoolean(JAASUtils.getString(options, "debug"));
        if (debug) {
            LOG.debug("Initialize BankLoginModule: subject={}, callbackHandler={}, sharedState={}, options={}", subject, callbackHandler.getClass().getName(), sharedState, options);
        }
    }

    @Override
    public boolean login() throws LoginException {
        if (debug) {
            LOG.debug("Run login()");
            LOG.debug("CallbackHandler available: {}", callbackHandler != null);
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("username");
        callbacks[1] = new PasswordCallback("password", false);

        if (callbackHandler != null) {
            try {
                callbackHandler.handle(callbacks);
            } catch (IOException e) {
                throw new LoginException(e.getMessage());
            } catch (UnsupportedCallbackException e) {
                throw new LoginException(e.getMessage() + " not available to obtain information from user");
            }
        } else
            throw new LoginException("Error: no CallbackHandler available to garner authentication information from the user");

        if (((NameCallback) callbacks[0]).getName() == null) {
            throw new LoginException("Username can not be null");
        }
        String username = ((NameCallback) callbacks[0]).getName();

        if (((PasswordCallback) callbacks[1]).getPassword() == null) {
            throw new LoginException("Password can not be null");
        }
        char[] password = ((PasswordCallback) callbacks[1]).getPassword();

        AccountRepository repo = AccountRepository.getInstance();
        try {
            if (debug) {
                LOG.debug("Execute the query on the Database by username: {}", username);
            }
            account = repo.findByUsername(username);
            if (debug) {
                LOG.debug("result is: {}", account);
            }
            if (account != null && account.getPassword().equals(new String(password))) {
                success = true;
                return true;
            } else {
                throw new FailedLoginException("Authentication failed for username: " + username);
            }
        } catch (NotFoundException e) {
            throw new FailedLoginException("User  " + username + " does not exist");
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (debug) {
            LOG.debug("Run commit()");
        }
        if (success) {
            if (debug) {
                LOG.debug("SetUp AccountPrincipal and RolePrincipal with username:{} Role:{}", account.getUsername(), account.getRole().name());
            }
            subject.getPrincipals().add(new AccountPrincipal(account.getUsername()));
            subject.getPrincipals().add(new RolePrincipal(account.getRole().name()));
            return true;
        } else {
            if (debug) {
                LOG.debug("Unsuccessful: Clear All");
            }
            subject.getPrincipals().clear();
            account = null;
            return false;
        }
    }

    @Override
    public boolean abort() throws LoginException {
        if (debug) {
            LOG.debug("Run abort()");
        }
        return logout();
    }

    @Override
    public boolean logout() throws LoginException {
        if (debug) {
            LOG.debug("Run logout(): Clear All");
        }
        subject.getPrincipals().clear();
        success = false;
        account = null;
        return true;
    }
}
