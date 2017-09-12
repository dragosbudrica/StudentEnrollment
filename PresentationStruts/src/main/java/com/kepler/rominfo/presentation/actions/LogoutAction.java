package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Dragos on 10.07.2017.
 */

@Component
public class LogoutAction extends ActionSupport {
    private static final Log LOGGER = LogFactory.getLog(LogoutAction.class);

    public String logout() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        LOGGER.debug("invalidating session for " + ((User) session.get("user")).getEmail());
        session.remove("user");
        return SUCCESS;
    }
}
