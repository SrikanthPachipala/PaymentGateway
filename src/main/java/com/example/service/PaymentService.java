package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dto.ApproveRequest;
import com.example.dto.PaymentRequest;
import com.example.dto.PaymentResponse;
import com.example.entity.Payment;
import com.example.entity.PaymentStatus;
import com.example.exception.CustomException;
import com.example.repository.PaymentRepository;

@Service
public class PaymentService {
	private final PaymentRepository paymentRepository;

    @Value("${api.key}")
    private String apiKey;

    private static final String A_PAY = "A Pay";
    private static final String B_PAY = "B Pay";
    private static final String A_PAY_PIN = "123456";
    private static final String B_PAY_OTP = "999999";

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        if (!A_PAY.equals(request.getMethod()) && !B_PAY.equals(request.getMethod())) {
            throw new CustomException("Unsupported payment method");
        }

        Payment payment = new Payment();
        payment.setMethod(request.getMethod());
        payment.setAmount(request.getAmount());
        payment.setReference(request.getReference());
        payment.setStatus(request.getStatus() != null ? request.getStatus() : PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public PaymentResponse approvePayment(Long id, ApproveRequest request) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (!optionalPayment.isPresent()) {
            throw new CustomException("Payment not found");
        }

        Payment payment = optionalPayment.get();
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new CustomException("Payment already processed");
        }

        String code = request.getCode();
        boolean isValid = false;

        if (A_PAY.equals(payment.getMethod())) {
            isValid = A_PAY_PIN.equals(code);
        } else if (B_PAY.equals(payment.getMethod())) {
        	try {
                Thread.sleep(2000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            isValid = B_PAY_OTP.equals(code);
        }

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            throw new CustomException("Invalid code");
        }

        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    public PaymentResponse getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new CustomException("Payment not found"));
    }

    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setMethod(payment.getMethod());
        response.setAmount(payment.getAmount());
        response.setReference(payment.getReference());
        response.setStatus(payment.getStatus());
        response.setCreatedDate(payment.getCreatedDate());
        return response;
    }

}
