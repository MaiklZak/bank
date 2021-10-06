package com.app.offerCreditApp.service;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.TestData;
import com.app.offerCreditApp.dto.ClientDto;
import com.app.offerCreditApp.entity.Client;
import com.app.offerCreditApp.error.NoSuchEntityException;
import com.app.offerCreditApp.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ClientServiceTest extends AbstractTest {

    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Autowired
    ClientServiceTest(ClientRepository clientRepository, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    @Test
    void save() {
        UUID idOfSavedClient = clientService.save(TestData.CLIENT_DTO_NEW);

        assertNotNull(idOfSavedClient);

        Client savedClient = clientRepository.getById(idOfSavedClient);

        assertNotNull(savedClient);
        assertThat(new ClientDto(savedClient)).usingRecursiveComparison().ignoringFields("id").isEqualTo(TestData.CLIENT_DTO_NEW);
    }

    @Test
    @Sql(value = "/scripts-test/clientTestScripts/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/clientTestScripts/after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll() {
        List<ClientDto> allClients = clientService.getAll();

        assertNotNull(allClients);
        assertFalse(allClients.isEmpty());
        assertEquals(TestData.SIZE_ALL_CLIENTS, allClients.size());
    }

    @Test
    @Sql(value = "/scripts-test/clientTestScripts/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/clientTestScripts/after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPageOfClient() {
        List<ClientDto> pageOfClient = clientService.getPageOfClient(0, 20);

        assertNotNull(pageOfClient);
        assertFalse(pageOfClient.isEmpty());
        assertEquals(20, pageOfClient.size());
    }

    @Test
    void getById() throws NoSuchEntityException {
        Client savedClient = clientRepository.save(new Client(TestData.CLIENT_DTO_NEW));

        ClientDto clientById = clientService.getById(savedClient.getId());

        assertNotNull(clientById);
        assertEquals(savedClient.getId(), clientById.getId());
        assertThat(clientById).usingRecursiveComparison().ignoringFields("id").isEqualTo(TestData.CLIENT_DTO_NEW);
    }

    @Test
    void getByIdFailed() {
        assertThrows(NoSuchEntityException.class,
                () -> clientService.getById(UUID.randomUUID()), "Client with specified id does not exist");
    }

    @Test
    void deleteById() {
        Client savedClient = clientRepository.save(new Client(TestData.CLIENT_DTO_NEW));

        assertNotNull(savedClient);

        clientService.deleteById(savedClient.getId());

        assertTrue(clientRepository.findById(savedClient.getId()).isEmpty());
    }

    @Test
    void update() throws NoSuchEntityException {
        Client savedClient = clientRepository.save(new Client(TestData.CLIENT_DTO_NEW));
        UUID id = savedClient.getId();
        TestData.CLIENT_DTO_UPDATE.setId(id);

        clientService.update(TestData.CLIENT_DTO_UPDATE);

        Client clientById = clientRepository.getById(id);

        assertThat(new ClientDto(clientById)).usingRecursiveComparison().isEqualTo(TestData.CLIENT_DTO_UPDATE);
    }

    @Test
    void updateFailed() {
        TestData.CLIENT_DTO_UPDATE.setId(UUID.randomUUID());
        assertThrows(NoSuchEntityException.class,
                () -> clientService.update(TestData.CLIENT_DTO_UPDATE), "Client with specified id does not exist");
    }
}