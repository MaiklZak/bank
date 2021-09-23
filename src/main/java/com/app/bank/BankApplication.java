package com.app.bank;

import com.app.bank.entity.Client;
import com.app.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class BankApplication implements ApplicationRunner {

	private final ClientRepository clientRepository;

	@Autowired
	public BankApplication(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		Client client = new Client();
//		client.setEmail("email");
//		client.setFullName("name");
//		client.setPassport("123 1234");
//		client.setPhone("34134234");
//
//		Client saveClient = clientRepository.save(client);

//		System.out.println(saveClient.getId());

//		System.out.println(clientRepository.getById(saveClient.getId()).getId());
//
		Client byId = clientRepository.getById(UUID.fromString("4375CF8E-DEF5-43F6-92F3-074D34A4CE35"));
//		System.out.println(byId);
		System.out.println(byId.getId());
	}
}
