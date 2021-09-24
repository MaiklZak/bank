package com.app.bank.controller;

import com.app.bank.dto.ClientDto;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.service.ClientServiceImpl;
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

    private final ClientServiceImpl clientServiceImpl;


    @Autowired
    public ClientController(ClientServiceImpl clientServiceImpl) {
        this.clientServiceImpl = clientServiceImpl;
    }

    @GetMapping
    public String getPageOfClientsList(Model model) {
        logger.info("Getting all clients");
        List<ClientDto> clients = clientServiceImpl.getAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/{id}")
    public String getPageOfClient(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting client with id: {}", id);
        ClientDto clientDto = clientServiceImpl.getById(id);
        model.addAttribute("client", clientDto);
        return "client";
    }

    @PostMapping
    public String saveNewClient(@ModelAttribute ClientDto clientDto) {
        UUID idOfSavedClient = clientServiceImpl.save(clientDto);
        logger.info("Client with id: {} saved", idOfSavedClient);
        return REDIRECT_URL_CLIENT + idOfSavedClient;
    }

    @GetMapping("/remove/{id}")
    public String removeClientById(@PathVariable UUID id) {
        logger.info("Removing client with id: {}", id);
        clientServiceImpl.deleteById(id);
        return REDIRECT_URL_CLIENT + id;
    }

    @PostMapping("/update")
    public String updateClient(@ModelAttribute ClientDto clientDto) throws NoSuchEntityException {
        logger.info("Updating client with id: {}", clientDto.getId());
        clientServiceImpl.update(clientDto);
        return REDIRECT_URL_CLIENT + clientDto.getId();
    }
}
