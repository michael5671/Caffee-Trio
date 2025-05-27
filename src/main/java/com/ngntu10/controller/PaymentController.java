package com.ngntu10.controller;

import com.ngntu10.annotation.SecuredSwaggerOperation;
import com.ngntu10.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Everything about payment by VNPAY")
public class PaymentController {
    private final PaymentService paymentService;

    @SecuredSwaggerOperation(summary = "Create VNPAY payment url")
    @PostMapping("/vnpay")
    public ResponseEntity<?> pay(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.createVnPayPayment(requestData, request));
    }

    @SecuredSwaggerOperation(summary = "Get status payment vnpay")
    @GetMapping("/vnpay-callback")
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_OrderInfo");
        long amount = Long.parseLong(request.getParameter("vnp_Amount"))/100;
        if (status.equals("00")) {
//            orderService.handlePaymentOrderById(orderId);
//            APIResponse<VNPAYResponse> response = new APIResponse<>(new VNPAYResponse(status, localizationUtils.getLocalizedMessage(MessageKeys.PAY_SUCCESS), "", amount), "Success");
            return ResponseEntity.ok("Success");
        } else {
//            APIResponse<VNPAYResponse> response = new APIResponse<>(null, localizationUtils.getLocalizedMessage(MessageKeys.PAY_FAILED));
            return ResponseEntity.badRequest().body("Failed");
        }
    }
}
