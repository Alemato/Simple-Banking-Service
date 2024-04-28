package it.univaq.sose.simplebankingsoapservice.security;

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
//        for (Callback callback : callbacks) {
//            WSPasswordCallback pc = (WSPasswordCallback) callback;
//            AccountRepository repo = AccountRepository.getInstance();
//            try {
//                String password = repo.findByUsername(pc.getIdentifier()).getPassword();
//                if (password != null) {
//                    pc.setPassword(password);
//                }
//            } catch (NotFoundException e) {
//                throw new Fault(e);
//            }
//        }
        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callback;
                // Qui puoi impostare la logica per selezionare la password corretta in base all'identificatore
                if ("webservice.simplebankingsoapservice.sose.univaq.it".equals(pc.getIdentifier())) {
                    // Imposta la password della chiave privata
                    pc.setPassword("123456");
                }
            }
        }
    }
}