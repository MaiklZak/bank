package com.app.bank.controller;

import com.app.bank.dto.SchedulePaymentDto;
import com.app.bank.service.SchedulePaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SchedulePaymentController {

    private final SchedulePaymentServiceImpl schedulePaymentService;

    @Autowired
    public SchedulePaymentController(SchedulePaymentServiceImpl schedulePaymentService) {
        this.schedulePaymentService = schedulePaymentService;
    }

    @GetMapping("/schedule")
    @ResponseBody
    public List<SchedulePaymentDto> getScheduleDtoList(@RequestParam BigDecimal amountCredit,
                                                       @RequestParam BigDecimal interestRate,
                                                       @RequestParam Integer countMonth,
                                                       @RequestParam Integer datePayment) {
        BigDecimal paymentMonth = schedulePaymentService.computeMonthlyPayment(amountCredit, interestRate, countMonth);
        return schedulePaymentService.generateSchedule(amountCredit, paymentMonth, interestRate, countMonth, datePayment);
    }
}
