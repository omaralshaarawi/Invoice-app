package com.appsdeveloperblog.app.ws.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {
	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,UserRepository userRepository) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository=userRepository;
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    AuthenticationManagerBuilder authenticationManagerBuilder =
	        http.getSharedObject(AuthenticationManagerBuilder.class);

	    authenticationManagerBuilder
	        .userDetailsService(userDetailsService)
	        .passwordEncoder(bCryptPasswordEncoder);

	    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

	    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
	    authenticationFilter.setFilterProcessesUrl("/users/login");

	    http.csrf(csrf -> csrf
	    	    .disable())
	    	    .headers(headers -> headers
	    	        .frameOptions(frameOptions -> frameOptions.disable()) 
	    	    )
	    	    .authorizeHttpRequests(authz -> authz
	    	        .requestMatchers(SecurityConstants.H2_CONSOLE).permitAll() 
	    	        .requestMatchers(HttpMethod.POST, "/users").permitAll()	    	        
	    	        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
	    	        .requestMatchers(HttpMethod.GET, "/users/*").permitAll()
	    	        .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")
	    	        .requestMatchers(HttpMethod.PUT, "/users/*").hasRole("ADMIN")
	    	        .requestMatchers(HttpMethod.POST, "/providers").hasAnyRole("ADMIN","CHEF_ACCOUNTANT")
	    	        .requestMatchers(HttpMethod.GET, "/providers/*").hasAnyRole("ADMIN","CHEF_ACCOUNTANT")
	    	        .requestMatchers(HttpMethod.POST, "/invoiceLine/**").permitAll()
	    	        .requestMatchers(HttpMethod.GET, "/invoiceLine/**").hasAnyRole("ADMIN","CHEF_ACCOUNTANT")
	    	        .requestMatchers(HttpMethod.PUT, "/invoiceLine/**").hasRole("ADMIN")
	    	        .requestMatchers(HttpMethod.DELETE, "/invoiceLine/**").hasRole("ADMIN")
	    	        .requestMatchers(HttpMethod.DELETE, "/invoice/**").hasRole("ADMIN")
	    	        .requestMatchers(HttpMethod.GET, "/invoice/**").hasAnyRole("ADMIN","CHEF_ACCOUNTANT")
	    	        .requestMatchers(HttpMethod.POST, "/invoice").permitAll()
	    	        .requestMatchers(HttpMethod.PUT, "/invoice/**").hasRole("ADMIN")
	    	        .anyRequest().authenticated()
	    	    )
	    	    .authenticationManager(authenticationManager)
	    	    .addFilter(authenticationFilter)
	    	    .addFilter(new AuthorizationFilter(authenticationManager,userRepository))
	    	    .sessionManagement(session -> session
	    	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    	    );

	    return http.build();
	}



}