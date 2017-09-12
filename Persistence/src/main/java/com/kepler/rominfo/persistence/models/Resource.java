package com.kepler.rominfo.persistence.models;

import java.io.Serializable;

public class Resource implements Serializable {
    private long resourceId;
    private String resourceName;

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
