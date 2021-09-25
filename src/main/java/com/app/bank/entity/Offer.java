package com.app.bank.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    private BigDecimal amount;

    @OneToMany(mappedBy = "offer")
    private Set<SchedulePayment> schedulePayments = new HashSet<>();

    public Offer() {
    }

    public Offer(Client client, Credit credit, BigDecimal amount) {
        this.client = client;
        this.credit = credit;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
