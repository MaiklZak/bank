package com.app.bank.service;

import com.app.bank.dto.OfferDto;
import com.app.bank.dto.SchedulePaymentDto;
import com.app.bank.entity.Client;
import com.app.bank.entity.Credit;
import com.app.bank.entity.Offer;
import com.app.bank.entity.SchedulePayment;
import com.app.bank.error.InvalidFieldsException;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.repository.ClientRepository;
import com.app.bank.repository.CreditRepository;
import com.app.bank.repository.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OfferServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);

    private final OfferRepository offerRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final SchedulePaymentServiceImpl schedulePaymentService;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ClientRepository clientRepository, CreditRepository creditRepository, SchedulePaymentServiceImpl schedulePaymentService) {
        this.offerRepository = offerRepository;
        this.clientRepository = clientRepository;

        this.creditRepository = creditRepository;
        this.schedulePaymentService = schedulePaymentService;
    }

    public UUID save(OfferDto offerDto, List<SchedulePaymentDto> scheduleDtoList) throws InvalidFieldsException {
        logger.info("Saving offer");
        Optional<Client> clientById = clientRepository.findById(offerDto.getClientDto().getId());
        Optional<Credit> creditById = creditRepository.findById(offerDto.getCreditDto().getId());
        if (clientById.isEmpty() || creditById.isEmpty() || offerDto.getAmount() == null) {
            throw new InvalidFieldsException("Fields of offer must not be null");
        }
        Offer offer = new Offer(clientById.get(), creditById.get(), offerDto.getAmount());
        offer = offerRepository.save(offer);

        Set<SchedulePayment> schedulePayments = schedulePaymentService.generateScheduleForOffer(offer, scheduleDtoList);
        offer.setSchedulePayments(schedulePayments);

        return offerRepository.save(offer).getId();
    }

    public List<OfferDto> getAll() {
        return offerRepository.findAll().stream()
                .map(OfferDto::new)
                .collect(Collectors.toList());
    }

    public OfferDto getById(UUID id) throws NoSuchEntityException {
        Optional<Offer> offerById = offerRepository.findById(id);
        if (offerById.isEmpty()) {
            throw new NoSuchEntityException("Offer with specified id does not exist");
        }
        return new OfferDto(offerById.get());
    }

    public void deleteById(UUID id) {
        offerRepository.deleteById(id);
    }

    public void update(OfferDto offerDto, List<SchedulePaymentDto> scheduleDtoList) throws NoSuchEntityException {
        Optional<Offer> offerById = offerRepository.findById(offerDto.getId());
        if (offerById.isEmpty()) {
            throw new NoSuchEntityException("Offer with specified id does not exist");
        }
        Offer offer = offerById.get();
        offer.setAmount(offerDto.getAmount());
        offer.setCredit(creditRepository.getById(offerDto.getCreditDto().getId()));

        Set<SchedulePayment> schedulePayments = schedulePaymentService.generateScheduleForOffer(offer, scheduleDtoList);
        offer.setSchedulePayments(schedulePayments);

        offerRepository.save(offer);
    }

    public List<OfferDto> getByClientId(UUID clientId) {
        logger.info("Getting offers by client with id: {}", clientId);
        return offerRepository.findByClientId(clientId).stream()
                .map(OfferDto::new)
                .collect(Collectors.toList());
    }
}
