package com.vgb;

import java.util.UUID;

/**
 * Represents a Company entity loaded from the database or CSV,
 * with both its integer PK and its public UUID.
 */
public class Companies {
    private int     companyId;    // new: integer primary key
    private UUID    companyUuid;
    private UUID    contactUuid;
    private String  name;
    private Address address;

    /**
     * Full constructor.
     * @param companyId    the DB auto‐increment ID
     * @param companyUuid  the public UUID
     * @param contactUuid  the primary contact’s UUID
     * @param name         the company’s name
     * @param address      the company’s address
     */
    public Companies(int companyId,
                     UUID companyUuid,
                     UUID contactUuid,
                     String name,
                     Address address) {
        this.companyId   = companyId;
        this.companyUuid = companyUuid;
        this.contactUuid = contactUuid;
        this.name        = name;
        this.address     = address;
    }

    /** Returns the integer DB ID. */
    public int getCompanyId() {
        return companyId;
    }

    /** Sets the integer DB ID. */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public UUID getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(UUID companyUuid) {
        this.companyUuid = companyUuid;
    }

    public UUID getContactUuid() {
        return contactUuid;
    }

    public void setContactUuid(UUID contactUuid) {
        this.contactUuid = contactUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Companies{" +
               "companyId="   + companyId +
               ", companyUuid=" + companyUuid +
               ", name='"       + name + '\'' +
               ", address="     + address +
               '}';
    }
}