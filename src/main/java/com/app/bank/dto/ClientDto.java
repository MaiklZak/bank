package com.app.bank.dto;

import com.app.bank.entity.Client;

import java.util.UUID;

public class ClientDto {

    private UUID id;

    private String fullName;

    private String phone;

    private String email;

    private String passport;

    public ClientDto() {
    }

    public ClientDto(Client client) {
        this.id = client.getId();
        this.fullName = client.getFullName();
        this.phone = client.getPhone();
        this.email = client.getEmail();
        this.passport = client.getPassport();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", passport='" + passport + '\'' +
                '}';
    }
}
