package com.java.backend.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CurrentRequestContext {

    private final HttpServletRequest request;

    public CurrentRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    public String getUsername() {
        return (String) request.getAttribute("username");
    }

    public Long getCompanyId() {
        return (Long) request.getAttribute("companyId");
    }
}
