package it.univaq.sose.simplebankingsoapservice;

import it.univaq.sose.simplebankingsoapservice.security.BankJAASLoginInterceptor;
import it.univaq.sose.simplebankingsoapservice.security.BankSecureAnnotationsInterceptor;
import it.univaq.sose.simplebankingsoapservice.security.BankWSS4JInInterceptor;
import it.univaq.sose.simplebankingsoapservice.webservice.BankWebServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;
import java.util.HashMap;

public class SimpleBankingSoapServiceApplication extends CXFNonSpringServlet {
    private static final long serialVersionUID = 6227089265528564333L;

    private static final org.slf4j.Logger LOG =
            org.slf4j.LoggerFactory.getLogger(SimpleBankingSoapServiceApplication.class);

    @Override
    public void loadBus(ServletConfig servletConfig) {
        super.loadBus(servletConfig);

        BankWebServiceImpl serviceInstance = new BankWebServiceImpl();

        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        // WSS4JInInterceptor
        bus.getInInterceptors().add(new BankWSS4JInInterceptor());
        // JAASLoginInterceptor
        bus.getInInterceptors().add(new BankJAASLoginInterceptor());
        // SecureAnnotationsInterceptor
        bus.getInInterceptors().add(new BankSecureAnnotationsInterceptor(serviceInstance));

        Endpoint e = Endpoint.publish("/bank", serviceInstance);
        HashMap<String, Object> endpointProperties = new HashMap<>();
        endpointProperties.put("ws-security.validate.token", false);
        e.setProperties(endpointProperties);
    }
}
