package com.app.bank.dto;

import com.app.bank.entity.Credit;

import java.math.BigDecimal;
import java.util.UUID;

public class CreditDto {

    private UUID id;

    private BigDecimal limitOn;

    private BigDecimal interestRate;

    public CreditDto() {
    }

    public CreditDto(Credit credit) {
        this.id = credit.getId();
        this.limitOn = credit.getLimitOn();
        this.interestRate = credit.getInterestRate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getLimitOn() {
        return limitOn;
    }

    public void setLimitOn(BigDecimal limitOn) {
        this.limitOn = limitOn;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "CreditDto{" +
                "id=" + id +
                ", limitOn=" + limitOn +
                ", interestRate=" + interestRate +
                '}';
    }
}
