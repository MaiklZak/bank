package com.app.offerCreditApp.service;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.dto.ClientDto;
import com.app.offerCreditApp.dto.CreditDto;
import com.app.offerCreditApp.dto.OfferDto;
import com.app.offerCreditApp.entity.Client;
import com.app.offerCreditApp.entity.Credit;
import com.app.offerCreditApp.entity.Offer;
import com.app.offerCreditApp.error.InvalidFieldsException;
import com.app.offerCreditApp.error.NoSuchEntityException;
import com.app.offerCreditApp.repository.ClientRepository;
import com.app.offerCreditApp.repository.CreditRepository;
import com.app.offerCreditApp.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static com.app.offerCreditApp.TestData.*;

@Transactional
class OfferServiceTest extends AbstractTest {

    private final OfferService offerService;

    private final OfferRepository offerRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;

    @Autowired
    OfferServiceTest(OfferService offerService, OfferRepository offerRepository, ClientRepository clientRepository, CreditRepository creditRepository) {
        this.offerService = offerService;
        this.offerRepository = offerRepository;
        this.clientRepository = clientRepository;
        this.creditRepository = creditRepository;
    }

    @Test
    void save() throws InvalidFieldsException {
        Client client = clientRepository.save(new Client(CLIENT_DTO_NEW));
        Credit credit = creditRepository.save(new Credit(CREDIT_DTO_NEW));
        OFFER_DTO.setClientDto(new ClientDto(client));
        OFFER_DTO.setCreditDto(new CreditDto(credit));
        OFFER_DTO.setAmount(BigDecimal.valueOf(100000));

        UUID idOfSavedOffer = offerService.save(OFFER_DTO, SCHEDULE_PAYMENT_DTO_LIST);

        assertNotNull(idOfSavedOffer);

        Offer offerById = offerRepository.getById(idOfSavedOffer);

        assertThat(new OfferDto(offerById)).usingRecursiveComparison().ignoringFields("id").isEqualTo(OFFER_DTO);
    }

    @Test
    void saveFailed() {
        CLIENT_DTO_NEW.setId(UUID.randomUUID());
        CREDIT_DTO_NEW.setId(UUID.randomUUID());
        OFFER_DTO.setCreditDto(CREDIT_DTO_NEW);
        OFFER_DTO.setClientDto(CLIENT_DTO_NEW);

        assertThrows(InvalidFieldsException.class,
                () -> offerService.save(OFFER_DTO, new ArrayList<>()), "Fields of offer must not be null");
    }

    @Test
    void getById() throws NoSuchEntityException {
        Offer savedOffer = offerRepository.save(new Offer(new Client(CLIENT_DTO_NEW), new Credit(CREDIT_DTO_NEW), BigDecimal.valueOf(200000)));

        OfferDto offerById = offerService.getById(savedOffer.getId());

        assertNotNull(offerById);
        assertEquals(savedOffer.getId(), offerById.getId());
        assertThat(offerById).usingRecursiveComparison().isEqualTo(new OfferDto(savedOffer));
    }

    @Test
    void getByIdFailed() {
        assertThrows(NoSuchEntityException.class,
                () -> offerService.getById(UUID.randomUUID()), "Offer with specified id does not exist");
    }

    @Test
    void deleteById() {
        Offer savedOffer = offerRepository.save(new Offer(new Client(CLIENT_DTO_NEW), new Credit(CREDIT_DTO_NEW), BigDecimal.valueOf(200000)));

        assertNotNull(savedOffer);

        offerService.deleteById(savedOffer.getId());

        assertTrue(offerRepository.findById(savedOffer.getId()).isEmpty());
    }
}