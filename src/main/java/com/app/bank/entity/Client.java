package com.app.bank.entity;

import com.app.bank.dto.ClientDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client extends AbstractBaseEntity {

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    private String email;

    private String passport;

    public Client() {
    }

    public Client(ClientDto clientDto) {
        super(clientDto.getId());
        this.fullName = clientDto.getFullName();
        this.phone = clientDto.getPhone();
        this.email = clientDto.getEmail();
        this.passport = clientDto.getPassport();
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
}
