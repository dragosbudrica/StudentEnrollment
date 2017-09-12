package com.kepler.rominfo.persistence.models;

import java.io.Serializable;

public class Grade implements Serializable {

    private int mark;
    private boolean validated;

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getMark() {
        return mark;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;    }
}
