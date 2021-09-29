package com.app.bank.repository;

import com.app.bank.entity.SchedulePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchedulePaymentRepository extends JpaRepository<SchedulePayment, UUID> {
}
