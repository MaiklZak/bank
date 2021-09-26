package com.app.bank.service;

import com.app.bank.entity.Offer;
import com.app.bank.entity.SchedulePayment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class SchedulePaymentServiceImpl {

    public Set<SchedulePayment> generateScheduleForOffer(Offer offer) {
        return new HashSet<>();
    }
}
