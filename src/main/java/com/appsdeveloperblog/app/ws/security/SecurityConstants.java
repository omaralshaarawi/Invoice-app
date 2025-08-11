package com.appsdeveloperblog.app.ws.security;

import org.springframework.core.env.Environment;

import com.appsdeveloperblog.app.ws.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME=86400000;
	public static final String TOKEN_PREFIX="Bearer ";
	public static final String HEADER_STRING="Authorization";
	public static final String SIGN_UP_URL="/users";
	public static final String TOKEN_SECRET="74d92e16ac32b3234ae2fd111048b965cad1e5037c20376161058d924e2892d0f7bbd51a04dea89c38a2a9a8a046a0e4ef78018b15dfb222cc2d1db4d7287196";
	public static final String H2_CONSOLE="/h2-console/**";
	public static String getTokenSecret()
	{
		Environment environment = (Environment) SpringApplicationContext.getBean("environment");
		return environment.getProperty("tokenSecret");
	}
}
