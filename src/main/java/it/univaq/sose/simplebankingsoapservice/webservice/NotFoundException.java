package it.univaq.sose.simplebankingsoapservice.webservice;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = 1292673996797365627L;

    public NotFoundException(String message) {
        super(message);
    }
}
