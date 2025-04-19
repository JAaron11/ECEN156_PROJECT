package com.vgb;

/**
 * Lookup table for postal (ZIP) codes.
 */
public class ZipCode {
    private int    zipId;
    private String zipCode;

    public ZipCode(int zipId, String zipCode) {
        this.zipId   = zipId;
        this.zipCode = zipCode;
    }

    public int getZipId() {
        return zipId;
    }

    public void setZipId(int zipId) {
        this.zipId = zipId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return zipCode;
    }
}
