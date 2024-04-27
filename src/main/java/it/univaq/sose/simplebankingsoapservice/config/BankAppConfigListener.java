package it.univaq.sose.simplebankingsoapservice.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BankAppConfigListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String jaasConfigPath = sce.getServletContext().getInitParameter("java.security.auth.login.config");
        System.setProperty("java.security.auth.login.config", sce.getServletContext().getRealPath(jaasConfigPath));
    }
}
