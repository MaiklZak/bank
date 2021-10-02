package com.app.bank.controller;

import com.app.bank.dto.ClientDto;
import com.app.bank.dto.CreditDto;
import com.app.bank.dto.OfferDto;
import com.app.bank.dto.SchedulePaymentDto;
import com.app.bank.error.InvalidFieldsException;
import com.app.bank.error.NoSuchEntityException;
import com.app.bank.service.ClientServiceImpl;
import com.app.bank.service.CreditServiceImpl;
import com.app.bank.service.OfferServiceImpl;
import com.app.bank.service.SchedulePaymentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/offers")
public class OfferController {

    private final Logger logger = LoggerFactory.getLogger(OfferController.class);

    private static final String MESSAGE = "message";

    private final OfferServiceImpl offerService;
    private final CreditServiceImpl creditService;
    private final ClientServiceImpl clientService;
    private final SchedulePaymentServiceImpl schedulePaymentService;

    @Autowired
    public OfferController(OfferServiceImpl offerService, CreditServiceImpl creditService, ClientServiceImpl clientService, SchedulePaymentServiceImpl schedulePaymentService) {
        this.offerService = offerService;
        this.creditService = creditService;
        this.clientService = clientService;
        this.schedulePaymentService = schedulePaymentService;
    }

    @GetMapping("/{id}")
    public String getPageEditOfferForClient(@PathVariable UUID id, Model model) throws NoSuchEntityException {
        logger.info("Getting page for editing offer with id: {}", id);
        OfferDto offerById = offerService.getById(id);
        List<CreditDto> allCredits = creditService.getAll();
        model.addAttribute("offer", offerById);
        model.addAttribute("credits", allCredits);
        return "offer/form";
    }

    @GetMapping("/new/{clientId}")
    public String getPageCreatOfferForClient(@PathVariable UUID clientId, Model model) throws NoSuchEntityException {
        logger.info("Getting page for creating offer");
        ClientDto clientDtoById = clientService.getById(clientId);
        OfferDto newOffer = new OfferDto();
        newOffer.setClientDto(clientDtoById);
        List<CreditDto> allCredits = creditService.getAll();
        model.addAttribute("offer", newOffer);
        model.addAttribute("credits", allCredits);
        return "offer/form";
    }

    @PostMapping({"/new", "/update"})
    public String saveUpdateNewOffer(@ModelAttribute OfferDto offerDto,
                                     @RequestParam UUID creditId,
                                     @RequestParam UUID clientId,
                                     @RequestParam Integer countMonth,
                                     @RequestParam Integer datePayment,
                                     RedirectAttributes redirectAttributes) throws NoSuchEntityException, InvalidFieldsException {
        CreditDto creditDto = creditService.getById(creditId);
        ClientDto clientDto = clientService.getById(clientId);
        offerDto.setCreditDto(creditDto);
        offerDto.setClientDto(clientDto);

        BigDecimal paymentMonth = schedulePaymentService
                .computeMonthlyPayment(creditDto.getLimitOn(), creditDto.getInterestRate(), countMonth);

        offerDto.setAmount(paymentMonth.multiply(BigDecimal.valueOf(countMonth)));

        List<SchedulePaymentDto> scheduleDtoList = schedulePaymentService
                .generateSchedule(creditDto.getLimitOn(), paymentMonth, creditDto.getInterestRate(), countMonth, datePayment);

        if (offerDto.getId() == null) {
            logger.info("Saving new offer");
            offerService.save(offerDto, scheduleDtoList);
            redirectAttributes.addFlashAttribute(MESSAGE, "Offer successfully created");
        } else {
            logger.info("Updating new offer");
            offerService.update(offerDto, scheduleDtoList);
            redirectAttributes.addFlashAttribute(MESSAGE, "Offer successfully updated");
        }
        return "redirect:/clients/" + clientId;
    }

    @GetMapping("/remove/{id}")
    public String removeOfferById(@PathVariable UUID id, @RequestParam UUID clientId, RedirectAttributes redirectAttributes) {
        logger.info("Removing offer with id: {}", id);
        offerService.deleteById(id);
        redirectAttributes.addFlashAttribute(MESSAGE, "Offer successfully removed");
        return "redirect:/clients/" + clientId;
    }
}
