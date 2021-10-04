package com.app.offerCreditApp.service;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.TestData;
import com.app.offerCreditApp.dto.CreditDto;
import com.app.offerCreditApp.entity.Credit;
import com.app.offerCreditApp.error.NoSuchEntityException;
import com.app.offerCreditApp.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CreditServiceTest extends AbstractTest {

    private final CreditRepository creditRepository;
    private final CreditService creditService;

    @Autowired
    CreditServiceTest(CreditRepository creditRepository, CreditService creditService) {
        this.creditRepository = creditRepository;
        this.creditService = creditService;
    }

    @Test
    void save() {
        UUID idOfCredit = creditService.save(TestData.CREDIT_DTO_NEW);

        assertNotNull(idOfCredit);

        Credit savedCredit = creditRepository.getById(idOfCredit);

        assertNotNull(savedCredit);
        assertThat(new CreditDto(savedCredit)).usingRecursiveComparison().ignoringFields("id").isEqualTo(TestData.CREDIT_DTO_NEW);

    }

    @Test
    @Sql(value = "/scripts-test/creditServiceTestScripts/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/creditServiceTestScripts/after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll() {
        List<CreditDto> allCredits = creditService.getAll();

        assertNotNull(allCredits);
        assertFalse(allCredits.isEmpty());
        assertEquals(TestData.SIZE_ALL_CREDITS, allCredits.size());
    }

    @Test
    void getById() throws NoSuchEntityException {
        Credit savedCredit = creditRepository.save(new Credit(TestData.CREDIT_DTO_NEW));

        CreditDto creditById = creditService.getById(savedCredit.getId());

        assertNotNull(creditById);
        assertEquals(savedCredit.getId(), creditById.getId());
        assertThat(creditById).usingRecursiveComparison().ignoringFields("id").isEqualTo(TestData.CREDIT_DTO_NEW);
    }

    @Test
    void getByIdFailed() {
        assertThrows(NoSuchEntityException.class,
                () -> creditService.getById(UUID.randomUUID()), "Credit with specified id does not exist");
    }

    @Test
    void deleteById() {
        Credit savedCredit = creditRepository.save(new Credit(TestData.CREDIT_DTO_NEW));

        assertNotNull(savedCredit);

        creditService.deleteById(savedCredit.getId());

        assertTrue(creditRepository.findById(savedCredit.getId()).isEmpty());
    }

    @Test
    void update() throws NoSuchEntityException {
        Credit savedCredit = creditRepository.save(new Credit(TestData.CREDIT_DTO_NEW));
        UUID id = savedCredit.getId();
        TestData.CREDIT_DTO_UPDATE.setId(id);

        creditService.update(TestData.CREDIT_DTO_UPDATE);

        Credit creditById = creditRepository.getById(id);

        assertThat(new CreditDto(creditById)).usingRecursiveComparison().isEqualTo(TestData.CREDIT_DTO_UPDATE);
    }

    @Test
    void updateFailed() {
        TestData.CREDIT_DTO_UPDATE.setId(UUID.randomUUID());
        assertThrows(NoSuchEntityException.class,
                () -> creditService.update(TestData.CREDIT_DTO_UPDATE), "Credit with specified id does not exist");
    }
}