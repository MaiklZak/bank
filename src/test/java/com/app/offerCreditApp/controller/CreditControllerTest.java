package com.app.offerCreditApp.controller;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.dto.CreditDto;
import com.app.offerCreditApp.error.NoSuchEntityException;
import com.app.offerCreditApp.service.CreditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.app.offerCreditApp.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
class CreditControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    private final CreditService creditService;

    @Autowired
    CreditControllerTest(MockMvc mockMvc, CreditService creditService) {
        this.mockMvc = mockMvc;
        this.creditService = creditService;
    }

    @Test
    @Sql(value = "/scripts-test/creditTestScripts/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/creditTestScripts/after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPageOfCreditsList() throws Exception {
        mockMvc.perform(get("/credits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("credit/list"))
                .andExpect(model().attribute("credits", hasSize(SIZE_ALL_CREDITS)))
                .andExpect(xpath("//*[@id=\"bodyCreditList\"]/section/h2").string("Кредиты"))
                .andExpect(xpath("//*[@id=\"creditCard\"]/div").nodeCount(SIZE_ALL_CREDITS));
    }

    @Test
    void getPageOfCredit() throws Exception {
        UUID idOfSavedCredit = creditService.save(CREDIT_DTO_NEW);
        mockMvc.perform(get("/credits/{id}", idOfSavedCredit))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("credit/form"))
                .andExpect(model().attributeExists("credit"))
                .andExpect(xpath("//*[@id=\"bodyCreditForm\"]/section/form").exists());
    }

    @Test
    void getPageOfCreditFailed() throws Exception {
        mockMvc.perform(get("/credits/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchEntityException))
                .andExpect(xpath("/html/body/section/h3").string("Credit with specified id does not exist"));
    }

    @Test
    void saveNewCredit() throws Exception {
        mockMvc.perform(post("/credits/new")
                        .flashAttr("credit", CREDIT_DTO_NEW))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Credit successfully saved"));

        Optional<CreditDto> optionalOfCreditDto = creditService.getAll().stream().findFirst();

        assertTrue(optionalOfCreditDto.isPresent());
        assertThat(optionalOfCreditDto.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(CREDIT_DTO_NEW);
    }

    @Test
    void saveNewCreditFailed() throws Exception {
        CreditDto creditDto = new CreditDto();
        mockMvc.perform(post("/credits/new")
                        .flashAttr("credit", creditDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("credit/form"))
                .andExpect(model().attribute("credit", creditDto));
    }

    @Test
    void removeCreditById() throws Exception {
        UUID idOfSavedCredit = creditService.save(CREDIT_DTO_NEW);
        mockMvc.perform(get("/credits/remove/{id}", idOfSavedCredit))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/credits/"))
                .andExpect(flash().attribute("message", "Credit successfully removed"));

        assertThrows(NoSuchEntityException.class, () -> creditService.getById(idOfSavedCredit));
        assertTrue(creditService.getAll().isEmpty());
    }

    @Test
    void updateCredit() throws Exception {
        UUID idOfSavedCredit = creditService.save(CREDIT_DTO_NEW);
        CREDIT_DTO_UPDATE.setId(idOfSavedCredit);
        mockMvc.perform(post("/credits/update")
                        .flashAttr("credit", CREDIT_DTO_UPDATE))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/credits/" + idOfSavedCredit))
                .andExpect(flash().attribute("message", "Credit successfully updated"));

        assertThat(creditService.getById(idOfSavedCredit)).usingRecursiveComparison().isEqualTo(CREDIT_DTO_UPDATE);
    }
}