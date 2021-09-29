package com.app.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SchedulePaymentDto {

    private Date datePayment;

    private BigDecimal amountPayment;

    private BigDecimal bodyRepayment;

    private BigDecimal interestRepayment;

    public SchedulePaymentDto() {
    }

    public SchedulePaymentDto(Date datePayment, BigDecimal amountPayment, BigDecimal bodyRepayment, BigDecimal interestRepayment) {
        this.datePayment = datePayment;
        this.amountPayment = amountPayment;
        this.bodyRepayment = bodyRepayment;
        this.interestRepayment = interestRepayment;
    }

    @JsonProperty("dateFormat")
    public String dateFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return formatter.format(datePayment);
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

    @Override
    public String toString() {
        return "SchedulePaymentDto{" +
                "datePayment=" + datePayment +
                ", amountPayment=" + amountPayment +
                ", bodyRepayment=" + bodyRepayment +
                ", interestRepayment=" + interestRepayment +
                '}';
    }
}
