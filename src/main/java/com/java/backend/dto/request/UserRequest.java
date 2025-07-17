package com.java.backend.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class UserRequest {
    private String username;
    private String password;
    private String fullName;
    private List<Long> companyIds;
    private List<Long> roleIds;
}