package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;

import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;

	public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository) {
		super(authManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);

		if (authorizationHeader == null) {
			return null;
		}

		String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

		byte[] secretKeyBytes = Base64.getDecoder().decode(SecurityConstants.getTokenSecret());
		SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);

		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

		Claims claims = parser.parseClaimsJws(token).getBody();

		String subject = claims.getSubject();

		if (subject == null) {
			return null;
		}
		UserEntity userEntity = userRepository.findByEmail(subject);
		UserPrincipal userPrincipal = new UserPrincipal(userEntity);

		return new UsernamePasswordAuthenticationToken(subject, null, userPrincipal.getAuthorities());

	}

}
