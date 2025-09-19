package com.stripe.getways.controller;

import com.stripe.getways.dto.output.PaymentRecord;
import com.stripe.getways.service.PaymentRequestService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@Data
public class PaymentRecordController {

    private final PaymentRequestService paymentRequestService;

    public PaymentRecordController(PaymentRequestService paymentRequestService) {
        this.paymentRequestService = paymentRequestService;
    }

    @GetMapping("/list")
    public Page<PaymentRecord> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        return paymentRequestService.getPayments(page, size, username, email);
    }

}
