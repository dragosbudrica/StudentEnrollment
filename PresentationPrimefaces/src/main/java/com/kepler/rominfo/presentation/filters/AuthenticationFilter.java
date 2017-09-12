package com.kepler.rominfo.presentation.filters;

import com.kepler.rominfo.persistence.mappers.AuthorizationMapper;
import com.kepler.rominfo.persistence.mappers.ResourceMapper;
import com.kepler.rominfo.persistence.models.Resource;
import com.kepler.rominfo.persistence.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    private static final String LOGIN_PAGE = "/login.xhtml";
    private static final String UNAUTHORIZED = "/secured/accessDenied.xhtml";

    private AuthorizationMapper authorizationMapper;
    private ResourceMapper resourceMapper;

    @Autowired
    public void setAuthorizationMapper(AuthorizationMapper authorizationMapper) {
        this.authorizationMapper = authorizationMapper;
    }

    @Autowired
    public void setResourceMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        User user = (User) httpServletRequest
                .getSession().getAttribute("user");

        String resourceRequested = null;


        if (user != null) {
            // user is logged in, continue request
            resourceRequested = ((HttpServletRequest) servletRequest).getRequestURL().toString();
            String[] parts = resourceRequested.split("[/.]");

            Resource res = resourceMapper.getResourceByName(parts[parts.length - 2]);

            if(res != null) {
                if (!authorizationMapper.isAuthorized(user.getRole().getRoleId(), res.getResourceId())) {
                    httpServletResponse.sendRedirect(
                            httpServletRequest.getContextPath()
                                    + UNAUTHORIZED);
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            } else {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } else {
            // user is not logged in, redirect to login page
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath()
                            + LOGIN_PAGE);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void destroy() {
        // close resources
    }
}
