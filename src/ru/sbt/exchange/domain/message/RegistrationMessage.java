package ru.sbt.exchange.domain.message;

import java.io.Serializable;

public class RegistrationMessage implements Serializable {
    private final String clientId;
    private final String password;

    private RegistrationMessage(String clientId, String password) {
        this.clientId = clientId;
        this.password = password;
    }

    public static RegistrationMessage registrationRequest(String clientId, String password) {
        return new RegistrationMessage(clientId, password);
    }

    public String getClientId() {
        return clientId;
    }

    public String getPassword() {
        return password;
    }
}
