package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class AddAccountView implements Serializable {

    @ManagedProperty("#{userService}")
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private String firstName;
    private String lastName;
    private String ssn;
    private String email;
    private String password;
    private String role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void addAccount() {
        try {
            User user = userService.findUser(email);
            if (user == null) {
                userService.addUser(firstName, lastName, Long.parseLong(ssn), email, password, role);
                Utils.addMessage("Account creation successful!", "Account creation successful!", FacesMessage.SEVERITY_INFO);
            } else {
                Utils.addMessage("That email is already used!", "That email is already used!", FacesMessage.SEVERITY_WARN);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
