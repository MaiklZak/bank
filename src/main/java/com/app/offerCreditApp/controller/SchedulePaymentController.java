package com.app.offerCreditApp.controller;

import com.app.offerCreditApp.dto.SchedulePaymentDto;
import com.app.offerCreditApp.error.InvalidFieldsException;
import com.app.offerCreditApp.service.SchedulePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SchedulePaymentController {

    private final SchedulePaymentService schedulePaymentService;

    @Autowired
    public SchedulePaymentController(SchedulePaymentService schedulePaymentService) {
        this.schedulePaymentService = schedulePaymentService;
    }

    @GetMapping("/schedule")
    @ResponseBody
    public List<SchedulePaymentDto> getScheduleDtoList(@RequestParam BigDecimal amountCredit,
                                                       @RequestParam BigDecimal interestRate,
                                                       @RequestParam Integer countMonth,
                                                       @RequestParam Integer datePayment) throws InvalidFieldsException {
        if (amountCredit == null || interestRate == null || countMonth == null || datePayment == null) {
            throw new InvalidFieldsException("Fields for calculating schedule are not filled in");
        }
        BigDecimal paymentMonth = schedulePaymentService.computeMonthlyPayment(amountCredit, interestRate, countMonth);
        return schedulePaymentService.generateSchedule(amountCredit, paymentMonth, interestRate, countMonth, datePayment);
    }
}
