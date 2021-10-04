package com.app.offerCreditApp.dto;

import com.app.offerCreditApp.entity.Offer;

import java.math.BigDecimal;
import java.util.UUID;

public class OfferDto {

    private UUID id;

    private ClientDto clientDto;

    private CreditDto creditDto;

    private BigDecimal amount;

    public OfferDto() {
    }

    public OfferDto(Offer offer) {
        this.id = offer.getId();
        this.clientDto = new ClientDto(offer.getClient());
        this.creditDto = new CreditDto(offer.getCredit());
        this.amount = offer.getAmount();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(ClientDto clientDto) {
        this.clientDto = clientDto;
    }

    public CreditDto getCreditDto() {
        return creditDto;
    }

    public void setCreditDto(CreditDto creditDto) {
        this.creditDto = creditDto;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OfferDto{" +
                "id=" + id +
                ", clientDto=" + clientDto +
                ", creditDto=" + creditDto +
                ", amount=" + amount +
                '}';
    }
}
