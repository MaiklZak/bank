package com.app.offerCreditApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankApplicationTests extends AbstractTest {

	private final BankApplication application;

	@Autowired
	BankApplicationTests(BankApplication application) {
		this.application = application;
	}

	@Test
	void contextLoads() {
		assertNotNull(application);
	}
}
