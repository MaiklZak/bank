package com.app.offerCreditApp;

import com.app.offerCreditApp.dto.ClientDto;
import com.app.offerCreditApp.dto.CreditDto;
import com.app.offerCreditApp.dto.OfferDto;
import com.app.offerCreditApp.dto.SchedulePaymentDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestData {

    public static final Integer SIZE_ALL_CLIENTS = 60;
    public static final ClientDto CLIENT_DTO_NEW = new ClientDto(null, "Test Client", "+7 (111) 111-11-11", "test@mail.org", "12 12 121 123");
    public static final ClientDto CLIENT_DTO_UPDATE = new ClientDto(null, "Test Client Update", "+7 (222) 111-11-11", "update@mail.org", "99 12 121 999");

    public static final Integer SIZE_ALL_CREDITS = 20;
    public static final CreditDto CREDIT_DTO_NEW = new CreditDto(null, new BigDecimal("100000.00"), new BigDecimal("20.00"));
    public static final CreditDto CREDIT_DTO_UPDATE = new CreditDto(null, new BigDecimal("505000.00"), new BigDecimal("15.00"));

    public static final OfferDto OFFER_DTO = new OfferDto();

    public static final List<SchedulePaymentDto> SCHEDULE_PAYMENT_DTO_LIST = new ArrayList<>();

    static {
        for (int i = 0; i < 20; i++) {
            SCHEDULE_PAYMENT_DTO_LIST.add(new SchedulePaymentDto(new Date(), BigDecimal.valueOf(1000 + i), BigDecimal.valueOf(100 + i), BigDecimal.valueOf(150 + i)));
        }
    }
}
