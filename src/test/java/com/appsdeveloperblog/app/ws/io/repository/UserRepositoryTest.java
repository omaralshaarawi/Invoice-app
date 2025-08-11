package com.appsdeveloperblog.app.ws.io.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@SpringBootTest
class UserRepositoryTest {

	/*@Autowired
	UserRepository userRepository;
	static boolean recordsCreated=false;
	@BeforeEach
	void setUp() throws Exception {
	    if(!recordsCreated)createRecord();
	   
	}
	@Test
	void testGetVerifiedUsers() {
	    Pageable pageableRequest = PageRequest.of(0, 2);
	    
	    Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
	    
	    assertNotNull(pages);
	    
	    List<UserEntity> userEntities = pages.getContent();
	    
	    assertNotNull(userEntities);
	    assertTrue(userEntities.size() == 1);
	}
	@Test
	void testFindUserByFirstName() {
		String firstName="oara";
		List<UserEntity> users=userRepository.findUserByFirstName(firstName);
		assertNotNull(users);
		assertTrue(users.size()==1);
		assertTrue(users.get(0).getFirstName().equals(firstName));
	}
	
	
	@Test
	void testFindUserByLastName() {
		String lastName="kapkn";
		List<UserEntity> users=userRepository.findUserByLastName(lastName);
		assertNotNull(users);
		assertTrue(users.size()==1);
		assertTrue(users.get(0).getLastName().equals(lastName));
	}
	
	
	

	
	@Test
	final void testFindUserEntityByUserId()
	{
	    String userId = "123c";
	    UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
	    
	    assertNotNull(userEntity);
	    assertTrue(userEntity.getUserId().equals(userId));
	}
	
	
	@Test
	final void testGetUserEntityFullNameById()
	{
	    String userId = "123c";
	    List<Object[]> records = userRepository.getUserEntityFullNameById(userId);
	    
	    assertNotNull(records);
	    assertTrue(records.size() == 1);
	    
	    Object[] userDetails = records.get(0);
	    
	    String firstName = String.valueOf(userDetails[0]);
	    String lastName = String.valueOf(userDetails[1]);
	    
	    assertNotNull(firstName);
	    assertNotNull(lastName);
	}
	
	
	@Test
	final void testUpdateUserEntityEmailVerificationStatus()
	{
	    boolean newEmailVerificationStatus = true;
	    userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "123c");
	    
	    UserEntity storedUserDetails = userRepository.findByUserId("123c");
	    
	    boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
	    
	    assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
	}
	
	void createRecord()
	{
		 // Prepare User Entity
	    UserEntity userEntity = new UserEntity();
	    userEntity.setFirstName("oara");
	    userEntity.setLastName("kapkn");
	    userEntity.setUserId("123c");
	    userEntity.setEncryptedPassword("asaas");
	    userEntity.setEmail("fr@frfr.com");
	    userEntity.setEmailVerificationStatus(false);
	    
	    // Prepare User Addresses
	    AddressEntity addressEntity = new AddressEntity();
	    addressEntity.setType("shipping");
	    addressEntity.setAddressId("ahgyt74hfy");
	    addressEntity.setCity("alex");
	    addressEntity.setCountry("da");
	    addressEntity.setPostalCode("31321");
	    addressEntity.setStreetName("123 street");
	    
	    List<AddressEntity> addresses = new ArrayList<>();
	    addresses.add(addressEntity);
	    
	    userEntity.setAddresses(addresses);
	    
	    userRepository.save(userEntity);
	    recordsCreated=true;
	}*/

}
