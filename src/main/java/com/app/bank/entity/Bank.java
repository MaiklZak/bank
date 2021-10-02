package com.app.bank.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bank")
public class Bank extends AbstractBaseEntity {

    @OneToMany()
    @JoinColumn(name = "client_id")
    private Set<Client> clients = new HashSet<>();

    @OneToMany()
    @JoinColumn(name = "credit_id")
    private Set<Credit> credits = new HashSet<>();

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Credit> getCredits() {
        return credits;
    }

    public void setCredits(Set<Credit> credits) {
        this.credits = credits;
    }
}
