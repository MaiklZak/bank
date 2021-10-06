package com.app.offerCreditApp.controller;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.dto.OfferDto;
import com.app.offerCreditApp.service.ClientService;
import com.app.offerCreditApp.service.CreditService;
import com.app.offerCreditApp.service.OfferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

import static com.app.offerCreditApp.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
class OfferControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    @MockBean
    private OfferService offerService;
    @MockBean
    private ClientService clientService;
    @MockBean
    private CreditService creditService;

    @Autowired
    OfferControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getPageEditOfferForClient() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doReturn(OFFER_DTO_CUSTOM).when(offerService).getById(id);
        Mockito.doReturn(new ArrayList<>()).when(creditService).getAll();
        mockMvc.perform(get("/offers/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("offer/form"))
                .andExpect(model().attribute("offer", OFFER_DTO_CUSTOM))
                .andExpect(xpath("//*[@id=\"bodyOffer\"]/section/h2")
                        .string("Предложение для: " + OFFER_DTO_CUSTOM.getClientDto().getFullName()));

    }

    @Test
    void getPageCreateOfferForClient() throws Exception {
        UUID idOfClient = UUID.randomUUID();
        CLIENT_DTO_NEW.setId(idOfClient);
        Mockito.doReturn(CLIENT_DTO_NEW).when(clientService).getById(idOfClient);
        mockMvc.perform(get("/offers/new/{clientId}", idOfClient))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("offer/form"))
                .andExpect(model().attributeExists("offer", "credits"))
                .andExpect(xpath("//*[@id=\"bodyOffer\"]/section/h2")
                        .string("Предложение для: " + CLIENT_DTO_NEW.getFullName()));

    }

    @Test
    void saveNewOffer() throws Exception {
        UUID creditId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        CLIENT_DTO_NEW.setId(creditId);
        CREDIT_DTO_NEW.setId(clientId);
        Mockito.doReturn(CREDIT_DTO_NEW).when(creditService).getById(creditId);
        Mockito.doReturn(CLIENT_DTO_NEW).when(clientService).getById(clientId);

        mockMvc.perform(post("/offers/new")
                        .flashAttr("offerDto", new OfferDto())
                        .param("creditId", creditId.toString())
                        .param("clientId", clientId.toString())
                        .param("countMonth", "12")
                        .param("datePayment", "5"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/" + clientId))
                .andExpect(flash().attribute("message", "Offer successfully created"));

        Mockito.verify(offerService, Mockito.times(1)).save(Mockito.any(), Mockito.any());


    }

    @Test
    void updateNewOffer() throws Exception {
        UUID creditId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        CLIENT_DTO_NEW.setId(creditId);
        CREDIT_DTO_NEW.setId(clientId);
        OFFER_DTO_CUSTOM.setId(UUID.randomUUID());

        Mockito.doReturn(CREDIT_DTO_NEW).when(creditService).getById(creditId);
        Mockito.doReturn(CLIENT_DTO_NEW).when(clientService).getById(clientId);

        mockMvc.perform(post("/offers/update")
                        .flashAttr("offerDto", OFFER_DTO_CUSTOM)
                        .param("creditId", creditId.toString())
                        .param("clientId", clientId.toString())
                        .param("countMonth", "12")
                        .param("datePayment", "5"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/" + clientId))
                .andExpect(flash().attribute("message", "Offer successfully updated"));

        Mockito.verify(offerService, Mockito.times(1)).update(Mockito.any(), Mockito.any());
    }

    @Test
    void removeOfferById() throws Exception {
        UUID id = UUID.randomUUID();
        OFFER_DTO_CUSTOM.setId(id);
        CLIENT_DTO_NEW.setId(UUID.randomUUID());

        mockMvc.perform(get("/offers/remove/{id}", id)
                        .param("clientId", CLIENT_DTO_NEW.getId().toString()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients/" + CLIENT_DTO_NEW.getId()))
                .andExpect(flash().attribute("message", "Offer successfully removed"));

        Mockito.verify(offerService, Mockito.times(1)).deleteById(id);
    }
}