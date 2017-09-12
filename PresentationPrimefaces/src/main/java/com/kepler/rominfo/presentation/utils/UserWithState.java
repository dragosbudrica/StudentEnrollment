package com.kepler.rominfo.presentation.utils;

import com.kepler.rominfo.persistence.models.User;

import java.io.Serializable;

public class UserWithState implements Serializable {
    private User user;
    private boolean state;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
