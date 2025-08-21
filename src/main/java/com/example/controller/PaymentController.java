package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.dto.ApproveRequest;
import com.example.dto.PaymentRequest;
import com.example.dto.PaymentResponse;
import com.example.exception.CustomException;
import com.example.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
		return new ResponseEntity<>(paymentService.createPayment(request), HttpStatus.CREATED);
	}

	@PostMapping("/{id}/approve")
	public ResponseEntity<PaymentResponse> approvePayment(@PathVariable Long id,
			@Valid @RequestBody ApproveRequest request) {
		return ResponseEntity.ok(paymentService.approvePayment(id, request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
		return ResponseEntity.ok(paymentService.getPaymentById(id));
	}

	@GetMapping
	public ResponseEntity<Page<PaymentResponse>> getAllPayments(Pageable pageable) {
		return ResponseEntity.ok(paymentService.getAllPayments(pageable));
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<String> handleCustomException(CustomException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
