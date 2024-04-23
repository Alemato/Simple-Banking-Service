package it.univaq.sose.simplebankingsoapservice;

import it.univaq.sose.simplebankingsoapservice.webservice.BankWebServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;

public class SimpleBankingSoapServiceApplication extends CXFNonSpringServlet {
    private static final long serialVersionUID = 6227089265528564333L;

    @Override
    public void loadBus(ServletConfig servletConfig) {
        super.loadBus(servletConfig);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        Endpoint.publish("/bank", new BankWebServiceImpl());
    }

}
