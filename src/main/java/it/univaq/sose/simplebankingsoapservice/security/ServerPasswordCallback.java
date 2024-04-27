package it.univaq.sose.simplebankingsoapservice.security;

import it.univaq.sose.simplebankingsoapservice.repository.AccountRepository;
import it.univaq.sose.simplebankingsoapservice.webservice.NotFoundException;
import org.apache.cxf.interceptor.Fault;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class ServerPasswordCallback implements CallbackHandler {

    public ServerPasswordCallback() {
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            WSPasswordCallback pc = (WSPasswordCallback) callback;
            AccountRepository repo = AccountRepository.getInstance();
            try {
                String password = repo.findByUsername(pc.getIdentifier()).getPassword();
                if (password != null) {
                    pc.setPassword(password);
                }
            } catch (NotFoundException e) {
                throw new Fault(e);
            }
        }
    }
}