package com.app.bank.controller;

import com.app.bank.dto.CreditDto;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.service.CreditServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/credits")
public class CreditController {

    private static final String REDIRECT_URL_CREDIT = "redirect:/credits/";

    private final Logger logger = LoggerFactory.getLogger(CreditController.class);

    private final CreditServiceImpl creditService;

    @Autowired
    public CreditController(CreditServiceImpl creditService) {
        this.creditService = creditService;
    }

    @GetMapping
    public String getPageOfCreditsList(Model model) {
        logger.info("Getting all credits");
        model.addAttribute("credits", creditService.getAll());
        return "credit/list";
    }

    @GetMapping("/{id}")
    public String getPageOfCredit(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting credit with id: {}", id);
        CreditDto creditById = creditService.getById(id);
        model.addAttribute("credit", creditById);
        return "credit/form";
    }

    @GetMapping("/new")
    public String getPageOfCredit(Model model) {
        logger.info("Getting page for creating new credit");
        model.addAttribute("credit", new CreditDto());
        return "credit/form";
    }

    @PostMapping("/new")
    public String saveNewCredit(@ModelAttribute CreditDto creditDto) {
        logger.info("Saving new credit");
        UUID idOfSavedCredit = creditService.save(creditDto);
        return REDIRECT_URL_CREDIT + idOfSavedCredit;
    }

    @GetMapping("/remove/{id}")
    public String removeCreditById(@PathVariable UUID id) {
        logger.info("Removing credit with id: {}", id);
        creditService.deleteById(id);
        return REDIRECT_URL_CREDIT + id;
    }

    @PostMapping("/update")
    public String updateCredit(@ModelAttribute CreditDto creditDto) throws NoSuchEntityException {
        logger.info("Updating credit with id: {}", creditDto.getId());
        creditService.update(creditDto);
        return REDIRECT_URL_CREDIT + creditDto.getId();
    }
}
