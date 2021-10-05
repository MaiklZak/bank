package com.app.offerCreditApp.controller;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.dto.ClientDto;
import com.app.offerCreditApp.error.NoSuchEntityException;
import com.app.offerCreditApp.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.app.offerCreditApp.TestData.CLIENT_DTO_NEW;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
class ClientControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    private final ClientService clientService;

    @Autowired
    ClientControllerTest(MockMvc mockMvc, ClientService clientService) {
        this.mockMvc = mockMvc;
        this.clientService = clientService;
    }

    @Test
    void getPageOfClientsList() throws Exception {
        mockMvc.perform(get("/clients")
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/list"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(xpath("//*[@id=\"bodyClient\"]/section/h2").string("Клиенты"));
    }

    @Test
    @Sql(value = "/scripts-test/clientServiceTestScripts/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/clientServiceTestScripts/after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testGetPageOfClientsList() throws Exception {
        mockMvc.perform(get("/clients")
                        .param("offset", "0")
                        .param("limit", "20")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(20)));
    }

    @Test
    void getPageOfClient() throws Exception {
        UUID idOfSavedClient = clientService.save(CLIENT_DTO_NEW);
        mockMvc.perform(get("/clients/{id}", idOfSavedClient))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/index"))
                .andExpect(model().attributeExists("client", "offers"))
                .andExpect(xpath("//*[@id=\"bodyClientIndex\"]/section[1]/h2").string(CLIENT_DTO_NEW.getFullName()));
    }

    @Test
    void getPageOfClientFailed() throws Exception {
        mockMvc.perform(get("/clients/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchEntityException))
                .andExpect(view().name("error/error"))
                .andExpect(xpath("/html/body/section/h3").string("Client with specified id does not exist"));
    }

    @Test
    void getPageOfClientForEdit() throws Exception {
        UUID idOfSavedClient = clientService.save(CLIENT_DTO_NEW);
        mockMvc.perform(get("/clients/update/{id}", idOfSavedClient))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/form"))
                .andExpect(model().attributeExists("client"))
                .andExpect(xpath("//*[@id=\"bodyClientForm\"]/section/h2").string("Клиент"));
    }

    @Test
    void updateClient() throws Exception {
        UUID idOfSavedClient = clientService.save(CLIENT_DTO_NEW);
        CLIENT_DTO_NEW.setId(idOfSavedClient);
        mockMvc.perform(post("/clients/update")
                        .flashAttr("client", CLIENT_DTO_NEW))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/" + CLIENT_DTO_NEW.getId()))
                .andExpect(flash().attribute("message", "Client successfully updated"));
    }

    @Test
    void updateClientFailed() throws Exception {
        ClientDto clientDto = new ClientDto();
        mockMvc.perform(post("/clients/update")
                        .flashAttr("client", clientDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/form"))
                .andExpect(model().attribute("client", clientDto));
    }

    @Test
    void getPageOfClientForCreate() throws Exception {
        mockMvc.perform(get("/clients/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/form"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void saveNewClient() throws Exception {
        mockMvc.perform(post("/clients/new")
                        .flashAttr("client", CLIENT_DTO_NEW))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Client successfully saved"));
    }

    @Test
    void removeClientById() throws Exception {
        UUID idOfClient = clientService.save(CLIENT_DTO_NEW);
        mockMvc.perform(get("/clients/remove/{id}", idOfClient))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/"))
                .andExpect(flash().attribute("message", "Client successfully removed"));
    }
}