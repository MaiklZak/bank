package com.app.offerCreditApp.repository;

import com.app.offerCreditApp.entity.SchedulePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchedulePaymentRepository extends JpaRepository<SchedulePayment, UUID> {
}
