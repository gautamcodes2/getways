package com.stripe.getways.controller;

import com.stripe.getways.dto.output.PaymentRecord;
import com.stripe.getways.service.PaymentRequestService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Data
public class PaymentRecordController {

    private final PaymentRequestService paymentRequestService;
    private final PagedResourcesAssembler<PaymentRecord> pagedResourcesAssembler;

    public PaymentRecordController(PaymentRequestService paymentRequestService, PagedResourcesAssembler<PaymentRecord> pagedResourcesAssembler) {
        this.paymentRequestService = paymentRequestService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/list")
    public PagedModel<EntityModel<PaymentRecord>> getPayments(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            Pageable pageable,
            PagedResourcesAssembler<PaymentRecord> assembler
    ) {
        // Fetch Page<PaymentRecord> from service
        Page<PaymentRecord> paymentPage = paymentRequestService.getPayments(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                username,
                email
        );

        return assembler.toModel(paymentPage);
    }
}
