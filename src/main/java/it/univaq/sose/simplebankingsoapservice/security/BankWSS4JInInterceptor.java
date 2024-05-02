package it.univaq.sose.simplebankingsoapservice.security;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import java.util.HashMap;

public class BankWSS4JInInterceptor extends WSS4JInInterceptor {

    public BankWSS4JInInterceptor() {
        super(getProps());
    }

    private static HashMap<String, Object> getProps() {
        HashMap<String, Object> props = new HashMap<>();
        //props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN + " " + WSHandlerConstants.SIGNATURE + " " + WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.ENCRYPTION);
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN + " " + WSHandlerConstants.SIGNATURE + " " + WSHandlerConstants.TIMESTAMP);
        props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        props.put(WSHandlerConstants.SIG_VER_PROP_FILE, "client-sign.properties");
        //props.put(WSHandlerConstants.DEC_PROP_FILE, "client-encr.properties");
        return props;
    }
}