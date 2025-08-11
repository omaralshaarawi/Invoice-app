package com.appsdeveloperblog.app.ws.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userId= utils.generateUserId(30);
		String userId1= utils.generateUserId(30);
		assertNotNull(userId);
		assertNotNull(userId1);
		assertTrue(userId.length()==30);
		assertTrue(userId!=userId1);
	}

}
