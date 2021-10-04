package com.app.offerCreditApp.controller;

import com.app.offerCreditApp.error.NoSuchEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerExceptionController {

    private final Logger logger = LoggerFactory.getLogger(HandlerExceptionController.class);

    @ExceptionHandler({NoSuchEntityException.class, NoSuchEntityException.class})
    public String handleNoSuchEntityException(Exception e, Model model) {
        logger.error(e.getLocalizedMessage());
        model.addAttribute("error", e.getLocalizedMessage());
        return "error/error";
    }
}
