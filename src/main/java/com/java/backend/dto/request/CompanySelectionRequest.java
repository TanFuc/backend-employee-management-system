package com.java.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanySelectionRequest {
    private String username;
    private Long companyId;
}
