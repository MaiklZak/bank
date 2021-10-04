package com.app.offerCreditApp.service;

import com.app.offerCreditApp.AbstractTest;
import com.app.offerCreditApp.TestData;
import com.app.offerCreditApp.dto.SchedulePaymentDto;
import com.app.offerCreditApp.entity.Offer;
import com.app.offerCreditApp.entity.SchedulePayment;
import com.app.offerCreditApp.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SchedulePaymentServiceTest extends AbstractTest {

    private final SchedulePaymentService schedulePaymentService;

    private final OfferRepository offerRepository;

    @Autowired
    SchedulePaymentServiceTest(SchedulePaymentService schedulePaymentService, OfferRepository offerRepository) {
        this.schedulePaymentService = schedulePaymentService;
        this.offerRepository = offerRepository;
    }

    @Test
    void generateScheduleForOffer() {
        Offer offer = offerRepository.save(new Offer());
        Set<SchedulePayment> schedulePayments = schedulePaymentService.generateScheduleForOffer(offer, TestData.SCHEDULE_PAYMENT_DTO_LIST);

        assertNotNull(schedulePayments);
        assertFalse(schedulePayments.isEmpty());
        assertEquals(TestData.SCHEDULE_PAYMENT_DTO_LIST.size(), schedulePayments.size());
    }

    @Test
    void generateSchedule() {
        Integer count = 20;
        List<SchedulePaymentDto> schedulePaymentDto = schedulePaymentService
                .generateSchedule(BigDecimal.valueOf(200000), BigDecimal.valueOf(1000), BigDecimal.valueOf(15), count, 5);

        assertNotNull(schedulePaymentDto);
        assertFalse(schedulePaymentDto.isEmpty());
        assertEquals(count, schedulePaymentDto.size());
    }
}