package com.java.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	private String username;
	private String password;
	private String role;
}