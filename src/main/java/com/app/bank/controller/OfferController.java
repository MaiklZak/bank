package com.app.bank.controller;

import com.app.bank.dto.OfferDto;
import com.app.bank.service.ClientServiceImpl;
import com.app.bank.service.OfferServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/offers")
public class OfferController {

    private final Logger logger = LoggerFactory.getLogger(OfferController.class);

    private final ClientServiceImpl clientService;
    private final OfferServiceImpl offerService;

    @Autowired
    public OfferController(ClientServiceImpl clientService, OfferServiceImpl offerService) {
        this.clientService = clientService;
        this.offerService = offerService;
    }

    @GetMapping
    public String getPageOfOffersList(Model model) {
        logger.info("Getting list of offers");
        model.addAttribute("offers", offerService.getAll());
        return "offersList";
    }

    @GetMapping("/client")
    public String getPageOffersForClient(@RequestParam UUID clientId, Model model) {
        logger.info("Getting offers for client with id: {}", clientId);
        List<OfferDto> offerByClientId = offerService.getByClientId(clientId);
        model.addAttribute("offers", offerByClientId);
        return "offer";
    }
}
