package com.vgb;

import java.util.UUID;

/**
 * Represents an email address belonging to a Person.
 */
public class Email {
    private UUID   emailUuid;
    private String emailAddress;

    public Email(UUID emailUuid, String emailAddress) {
        this.emailUuid    = emailUuid;
        this.emailAddress = emailAddress;
    }

    public UUID getEmailUuid() {
        return emailUuid;
    }

    public void setEmailUuid(UUID emailUuid) {
        this.emailUuid = emailUuid;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return emailAddress;
    }
}
