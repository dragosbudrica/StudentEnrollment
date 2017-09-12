package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.services.UserService;
import org.springframework.stereotype.Component;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@SessionScoped
public class LoginView implements Serializable {
    private static final String ALL_COURSES_REDIRECT = "/secured/allCourses.xhtml?faces-redirect=true";
    private static final String PROFESSOR_COURSES_REDIRECT = "/secured/professorCourses.xhtml?faces-redirect=true";
    private static final String REGISTER_REDIRECT = "/secured/addNewAccount.xhtml?faces-redirect=true";
    private static final String LOGIN_REDIRECT = "/login.xhtml?faces-redirect=true";

    @ManagedProperty("#{userService}")
    private UserService userService;

    private String email;
    private String password;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
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

    public String login() {
        User user = userService.findUser(email);
        if (user != null) {
            if (userService.checkCredentials(user, email, password)) {
                Utils.putUserOnSession(user);
                switch(user.getRole().getRoleName()) {
                    case "Admin":
                        return REGISTER_REDIRECT;
                    case "Professor":
                        return PROFESSOR_COURSES_REDIRECT;
                    case "Student":
                        return ALL_COURSES_REDIRECT;
                }
            }
            else {
                Utils.addMessage("Wrong password!", "Wrong password!", FacesMessage.SEVERITY_WARN);
            }
        } else {
            Utils.addMessage("That user does not exists!", "That user does not exists!", FacesMessage.SEVERITY_WARN);
        }

        return null;
    }

    public boolean isLoggedIn() {
        User user = Utils.getUserFromSession();
        return user != null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return LOGIN_REDIRECT;
    }

    public String getUserFirstName() {
        User user = Utils.getUserFromSession();
        return user.getFirstName();
    }
}
