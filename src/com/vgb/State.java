package com.vgb;

/**
 * Lookup table for US states.
 */
public class State {
    private int    stateId;
    private String stateCode;
    private String stateName;

    public State(int stateId, String stateCode, String stateName) {
        this.stateId   = stateId;
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return stateCode + " - " + stateName;
    }
}
