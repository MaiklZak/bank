package com.app.bank.controller;

import com.app.bank.dto.ClientDto;
import com.app.bank.dto.OfferDto;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.service.ClientServiceImpl;
import com.app.bank.service.OfferServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private static final String REDIRECT_URL_CLIENT = "redirect:/clients/";
    private static final String CLIENT = "client";
    private static final String CLIENT_FORM_URL = "client/form";
    private static final String MESSAGE = "message";

    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientServiceImpl clientService;
    private final OfferServiceImpl offerService;


    @Autowired
    public ClientController(ClientServiceImpl clientService, OfferServiceImpl offerService) {
        this.clientService = clientService;
        this.offerService = offerService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String getPageOfClientsList(Model model) {
        logger.info("Getting page all clients");
        List<ClientDto> clients = clientService.getPageOfClient(0, 20);
        model.addAttribute("clients", clients);
        return "client/list";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientDto> getPageOfClientsList(@RequestParam Integer offset, @RequestParam Integer limit) {
        logger.info("Getting clients offset: {}, limit: {}", offset, limit);
        return clientService.getPageOfClient(offset, limit);
    }

    @GetMapping("/{id}")
    public String getPageOfClient(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting client with offers with id: {}", id);
        ClientDto clientDto = clientService.getById(id);
        List<OfferDto> offersByClientId = offerService.getByClientId(id);
        model.addAttribute(CLIENT, clientDto);
        model.addAttribute("offers", offersByClientId);
        return "client/index";
    }

    @GetMapping("/update/{id}")
    public String getPageOfClientForEdit(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting client for editing with id: {}", id);
        ClientDto clientDto = clientService.getById(id);
        model.addAttribute(CLIENT, clientDto);
        return CLIENT_FORM_URL;
    }

    @PostMapping("/update")
    public String updateClient(@Valid @ModelAttribute("client") ClientDto clientDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) throws NoSuchEntityException {
        if (bindingResult.hasErrors()) {
            logger.info("Updating client failed");
            model.addAttribute(CLIENT, clientDto);
            return CLIENT_FORM_URL;
        }
        logger.info("Updating client with id: {}", clientDto.getId());
        clientService.update(clientDto);
        redirectAttributes.addFlashAttribute(MESSAGE, "Client successfully updated");
        return REDIRECT_URL_CLIENT + clientDto.getId();
    }

    @GetMapping("/new")
    public String getPageOfClientForCreate(Model model) {
        logger.info("Getting page for creating new client");
        model.addAttribute(CLIENT, new ClientDto());
        return CLIENT_FORM_URL;
    }

    @PostMapping("/new")
    public String saveNewClient(@Valid @ModelAttribute("client") ClientDto client,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Saving client failed");
            model.addAttribute(CLIENT, client);
            return CLIENT_FORM_URL;
        }
        logger.info("Saving new client");
        UUID idOfSavedClient = clientService.save(client);
        redirectAttributes.addFlashAttribute(MESSAGE, "Client successfully saved");
        return REDIRECT_URL_CLIENT + idOfSavedClient;
    }

    @GetMapping("/remove/{id}")
    public String removeClientById(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        logger.info("Removing client with id: {}", id);
        clientService.deleteById(id);
        redirectAttributes.addFlashAttribute(MESSAGE, "Client successfully removed");
        return REDIRECT_URL_CLIENT;
    }
}
