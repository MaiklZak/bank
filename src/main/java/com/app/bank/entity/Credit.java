package com.app.bank.entity;

import com.app.bank.dto.CreditDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credit")
public class Credit extends AbstractBaseEntity {

    @Column(name = "limit_on")
    private BigDecimal limitOn;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Offer> offers = new HashSet<>();

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

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
