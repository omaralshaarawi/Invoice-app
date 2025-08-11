package com.appsdeveloperblog.app.ws.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.appsdeveloperblog.app.ws.io.entity.AuthorityEntity;
import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -2108321328590521439L;
	UserEntity userEntity;
	
	public UserPrincipal(UserEntity userEntity)
	{
		this.userEntity=userEntity;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    
		Collection<GrantedAuthority> authorities = new HashSet<>();
		Collection<AuthorityEntity> authorityEntities = new HashSet<>();
	    
	    // Get user Roles
	    Collection<RoleEntity> roles = userEntity.getRoles();
	    
	    if(roles == null) return authorities;
	    
	    roles.forEach((role) -> {
	        authorities.add(new SimpleGrantedAuthority(role.getName()));
	        authorityEntities.addAll(role.getAuthorities());
	    });
	    
	    authorityEntities.forEach((authorityEntity) -> {
	        authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
	    });
	    
	    return authorities;
	}

	@Override
	public String getPassword() {
		return this.userEntity.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return this.userEntity.getEmail();
	}

}
