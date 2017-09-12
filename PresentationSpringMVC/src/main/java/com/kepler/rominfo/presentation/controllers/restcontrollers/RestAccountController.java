package com.kepler.rominfo.presentation.controllers.restcontrollers;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.services.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RestAccountController {

    private static final Log LOGGER = LogFactory.getLog(RestAccountController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/addNewAccount", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String addNewAccount(@RequestBody Map<String, Object> params) {

        String result;

        String firstName = (String) params.get("firstName");
        String lastName = (String) params.get("lastName");
        String ssn = (String) params.get("ssn");
        String email = (String) params.get("email");
        String password = (String) params.get("password");
        String role = (String) params.get("role");

        User user = userService.findUser(email);
        if (user == null) {
            userService.addUser(firstName, lastName, Long.parseLong(ssn), email, password, role);
            LOGGER.info("register successful for " + email);
            result = "Account creation successful!";
        } else {
            LOGGER.info("register failed for " + email);
            result = "That email is already used!";
        }

        return result;
    }
}
