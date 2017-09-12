package com.kepler.rominfo.persistence.models;

import java.io.Serializable;

public class Category implements Serializable {
    private long categoryId;
    private String categoryName;

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
