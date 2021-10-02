package com.app.bank.controller;

import com.app.bank.dto.CreditDto;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.service.CreditServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/credits")
public class CreditController {

    private static final String REDIRECT_URL_CREDIT = "redirect:/credits/";
    private static final String CREDIT = "credit";
    private static final String CREDIT_FORM_URL = "credit/form";
    private static final String MESSAGE = "message";

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
        model.addAttribute(CREDIT, creditById);
        return CREDIT_FORM_URL;
    }

    @GetMapping("/new")
    public String getPageOfCredit(Model model) {
        logger.info("Getting page for creating new credit");
        model.addAttribute(CREDIT, new CreditDto());
        return CREDIT_FORM_URL;
    }

    @PostMapping("/new")
    public String saveNewCredit(@Valid @ModelAttribute("credit") CreditDto creditDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Saving credit failed");
            model.addAttribute(CREDIT, creditDto);
            return CREDIT_FORM_URL;
        }
        logger.info("Saving new credit");
        UUID idOfSavedCredit = creditService.save(creditDto);
        redirectAttributes.addFlashAttribute(MESSAGE, "Credit successfully saved");
        return REDIRECT_URL_CREDIT + idOfSavedCredit;
    }

    @GetMapping("/remove/{id}")
    public String removeCreditById(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        logger.info("Removing credit with id: {}", id);
        creditService.deleteById(id);
        redirectAttributes.addFlashAttribute(MESSAGE, "Credit successfully removed");
        return REDIRECT_URL_CREDIT;
    }

    @PostMapping("/update")
    public String updateCredit(@Valid @ModelAttribute("credit") CreditDto creditDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) throws NoSuchEntityException {
        if (bindingResult.hasErrors()) {
            logger.info("Updating credit failed");
            model.addAttribute(CREDIT, creditDto);
            return CREDIT_FORM_URL;
        }
        logger.info("Updating credit with id: {}", creditDto.getId());
        creditService.update(creditDto);
        redirectAttributes.addFlashAttribute(MESSAGE, "Credit successfully updated");
        return REDIRECT_URL_CREDIT + creditDto.getId();
    }
}
