package com.app.bank.controller;

import com.app.bank.dto.ClientDto;
import com.app.bank.dto.OfferDto;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.service.ClientServiceImpl;
import com.app.bank.service.OfferServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private static final String REDIRECT_URL_CLIENT = "redirect:/clients/";

    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientServiceImpl clientService;
    private final OfferServiceImpl offerService;


    @Autowired
    public ClientController(ClientServiceImpl clientService, OfferServiceImpl offerService) {
        this.clientService = clientService;
        this.offerService = offerService;
    }

    @GetMapping
    public String getPageOfClientsList(Model model) {
        logger.info("Getting all clients");
        List<ClientDto> clients = clientService.getAll();
        model.addAttribute("clients", clients);
        return "client/list";
    }

    @GetMapping("/{id}")
    public String getPageOfClient(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting client with offers with id: {}", id);
        ClientDto clientDto = clientService.getById(id);
        List<OfferDto> offersByClientId = offerService.getByClientId(id);
        model.addAttribute("client", clientDto);
        model.addAttribute("offers", offersByClientId);
        return "client/index";
    }

    @GetMapping("/update/{id}")
    public String getPageOfClientForEdit(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting client for editing with id: {}", id);
        ClientDto clientDto = clientService.getById(id);
        model.addAttribute("client", clientDto);
        return "client/form";
    }

    @PostMapping("/update")
    public String updateClient(@ModelAttribute ClientDto clientDto) throws NoSuchEntityException {
        logger.info("Updating client with id: {}", clientDto.getId());
        clientService.update(clientDto);
        return REDIRECT_URL_CLIENT + clientDto.getId();
    }

    @GetMapping("/new")
    public String getPageOfClientForCreate(Model model) {
        logger.info("Getting page for creating new client");
        model.addAttribute("client", new ClientDto());
        return "client/form";
    }

    @PostMapping("/new")
    public String saveNewClient(@ModelAttribute ClientDto clientDto) {
        logger.info("Saving new client");
        UUID idOfSavedClient = clientService.save(clientDto);
        return REDIRECT_URL_CLIENT + idOfSavedClient;
    }

    @GetMapping("/remove/{id}")
    public String removeClientById(@PathVariable UUID id) {
        logger.info("Removing client with id: {}", id);
        clientService.deleteById(id);
        return REDIRECT_URL_CLIENT + id;
    }
}
