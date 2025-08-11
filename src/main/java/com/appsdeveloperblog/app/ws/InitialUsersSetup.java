package com.appsdeveloperblog.app.ws;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.appsdeveloperblog.app.ws.io.entity.AuthorityEntity;
import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.AuthorityRepository;
import com.appsdeveloperblog.app.ws.io.repository.RoleRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.Roles;
import com.appsdeveloperblog.app.ws.shared.Utils;


@Component
public class InitialUsersSetup {
    
	
	
    @Autowired
    AuthorityRepository authorityRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    Utils utils;
    
    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready event...");
        
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
        
         createRole(Roles.ROLE_CHEF_ACCOUNTANT.name(),Arrays.asList(readAuthority,writeAuthority));
         createRole(Roles.ROLE_ACCOUNTANT.name(),Arrays.asList(writeAuthority));

        RoleEntity  roleAdmin=createRole(Roles.ROLE_ADMIN.name(),Arrays.asList(readAuthority,writeAuthority,deleteAuthority));
        
        if(roleAdmin==null)return;     
        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("RASN");
        adminUser.setLastName("CONSULT");
        adminUser.setEmail("omar@RASN.com");
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("1234"));
        adminUser.setRoles(Arrays.asList(roleAdmin));
     
        userRepository.save(adminUser);
    }
    
    @Transactional
    private AuthorityEntity createAuthority(String name) {
        
        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
       
        return authority;
    }
    
    @Transactional
    private RoleEntity createRole(
            String name, Collection<AuthorityEntity> authorities) {
        
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}