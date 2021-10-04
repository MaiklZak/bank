package com.app.offerCreditApp.dto;

import com.app.offerCreditApp.entity.Credit;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;

public class CreditDto {

    private UUID id;

    @DecimalMax(value = "1000000000.00", message = "Maximum allowed limit: 1 000 000 000.00")
    @DecimalMin(value = "50000.00", message = "Minimum allowed limit: 50 000.00")
    @Digits(integer = 10, fraction = 2, message = "Limit must be format: dd.dd; d - digit")
    @NotNull(message = "Must not be empty")
    private BigDecimal limitOn;

    @DecimalMax(value = "99.99", message = "Maximum allowed rate: 99.99 %")
    @DecimalMin(value = "0.25", message = "Minimum allowed rate: 0.25 %")
    @Digits(integer = 2, fraction = 2, message = "Rate must be format: dd.dd; d - digit")
    @NotNull(message = "Must not be empty")
    private BigDecimal interestRate;

    public CreditDto() {
    }

    public CreditDto(Credit credit) {
        this.id = credit.getId();
        this.limitOn = credit.getLimitOn();
        this.interestRate = credit.getInterestRate();
    }

    public CreditDto(UUID id, BigDecimal limitOn, BigDecimal interestRate) {
        this.id = id;
        this.limitOn = limitOn;
        this.interestRate = interestRate;
    }

    @JsonProperty("formatLimit")
    public String formatLimit() {
        return new DecimalFormat( "#,###,###,###.00" ).format(limitOn);
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
