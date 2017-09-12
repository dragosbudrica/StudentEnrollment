package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.services.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dragos on 04.07.2017.
 */
@Component
public class NewAccountAction extends ActionSupport {
    private static final Log LOGGER = LogFactory.getLog(NewAccountAction.class);

    private UserService userService;

    private String newAccountResult;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private String firstName;
    private String lastName;
    private String ssn;
    private String email;
    private String password;
    private String role;

    public void setNewAccountResult(String newAccountResult) {
        this.newAccountResult = newAccountResult;
    }

    public String getNewAccountResult() {
        return newAccountResult;
    }

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

    public String newAccount() {
        User user = userService.findUser(email);
        if(user == null) {
            userService.addUser(firstName, lastName, Long.parseLong(ssn), email, password, role);
            LOGGER.info("register successful for " + email);
            newAccountResult = "Account creation successful!";
            return SUCCESS;
        }
        else {
            LOGGER.info("register failed for " + email);
            newAccountResult = "That email is already used!";
            return ERROR;
        }
    }
}
