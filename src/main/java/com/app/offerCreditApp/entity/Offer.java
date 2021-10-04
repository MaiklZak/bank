package com.app.offerCreditApp.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "offer")
public class Offer extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    private BigDecimal amount;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SchedulePayment> schedulePayments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Offer> offers = new HashSet<>();

    public Offer() {
    }

    public Offer(Client client, Credit credit, BigDecimal amount) {
        this.client = client;
        this.credit = credit;
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Set<SchedulePayment> getSchedulePayments() {
        return schedulePayments;
    }

    public void setSchedulePayments(Set<SchedulePayment> schedulePayments) {
        this.schedulePayments = schedulePayments;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
