package com.app.offerCreditApp.dto;

import com.app.offerCreditApp.entity.Client;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.UUID;

public class ClientDto {

    private UUID id;

    @Length(min = 4, max = 60, message = "Length name must be between 4 and 60 letters")
    @NotEmpty(message = "Full name must not be empty")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё\\s]*", message = "Full name must contain only letters")
    private String fullName;

    @NotBlank(message = "Phone must not be empty")
    @Pattern(regexp = "\\+7\\s[(][0-9]{3}[)]\\s\\d{3}[-]\\d{2}[-]\\d{2}", message = "Invalid number")
    private String phone;

    @NotBlank(message = "Email must not be empty")
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Invalid email. Sample 'user@mail.org'")
    private String email;

    @NotBlank(message = "Passport must not be empty")
    @Pattern(regexp = "[\\d]{2}\\s[\\d]{2}\\s[\\d]{3}\\s[\\d]{3}", message = "Invalid passport")
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

    public ClientDto(UUID id, String fullName, String phone, String email, String passport) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.passport = passport;
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
