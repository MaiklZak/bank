package com.app.bank.entity;

import com.app.bank.dto.CreditDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "credit")
public class Credit extends AbstractBaseEntity {

    @Column(name = "limit_on")
    private BigDecimal limitOn;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    public Credit() {
    }

    public Credit(CreditDto creditDto) {
        super(creditDto.getId());
        this.limitOn = creditDto.getLimitOn();
        this.interestRate = creditDto.getInterestRate();
    }

    public BigDecimal getLimitOn() {
        return limitOn;
    }

    public void setLimitOn(BigDecimal limit) {
        this.limitOn = limit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
