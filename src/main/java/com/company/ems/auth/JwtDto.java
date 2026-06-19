package com.company.ems.auth;

public record JwtDto(
	    String accessToken,
	    String tokenType,
	    boolean isPasswordChanged 
	) {}