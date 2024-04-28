package it.univaq.sose.simplebankingsoapservice.security;

import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import java.util.HashMap;

public class BankWSS4JOutInterceptor extends WSS4JOutInterceptor {
    public BankWSS4JOutInterceptor() {
        super(getProps());
    }

    private static HashMap<String, Object> getProps() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE + " " + WSHandlerConstants.ENCRYPTION);
        props.put(WSHandlerConstants.USER, "webservice.simplebankingsoapservice.sose.univaq.it");
        props.put(WSHandlerConstants.SIG_PROP_FILE, "server-sign.properties");
        props.put(WSHandlerConstants.ENC_PROP_FILE, "server-encr.properties");
        props.put(WSHandlerConstants.PW_CALLBACK_CLASS, ServerPasswordCallback.class.getName());
        return props;
    }
}
