package com.app.bank.entity;

import com.app.bank.dto.SchedulePaymentDto;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "schedule_payment")
public class SchedulePayment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(name = "date_payment")
    private Date datePayment;

    @Column(name = "amount_payment")
    private BigDecimal amountPayment;

    @Column(name = "body_repayment")
    private BigDecimal bodyRepayment;

    @Column(name = "interest_repayment")
    private BigDecimal interestRepayment;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    public SchedulePayment() {
    }

    public SchedulePayment(SchedulePaymentDto schedulePaymentDto) {
        this.datePayment = schedulePaymentDto.getDatePayment();
        this.amountPayment = schedulePaymentDto.getAmountPayment();
        this.bodyRepayment = schedulePaymentDto.getBodyRepayment();
        this.interestRepayment = schedulePaymentDto.getInterestRepayment();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public BigDecimal getAmountPayment() {
        return amountPayment;
    }

    public void setAmountPayment(BigDecimal amountPayment) {
        this.amountPayment = amountPayment;
    }

    public BigDecimal getBodyRepayment() {
        return bodyRepayment;
    }

    public void setBodyRepayment(BigDecimal bodyRepayment) {
        this.bodyRepayment = bodyRepayment;
    }

    public BigDecimal getInterestRepayment() {
        return interestRepayment;
    }

    public void setInterestRepayment(BigDecimal interestRepayment) {
        this.interestRepayment = interestRepayment;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
