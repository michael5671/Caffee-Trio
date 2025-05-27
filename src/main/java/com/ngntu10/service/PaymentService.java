package com.ngntu10.service;


import com.ngntu10.configurations.VNPAYConfig;
import com.ngntu10.dto.response.VNPAYResponse;
import com.ngntu10.util.VNPAYUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService{
    private final VNPAYConfig vnPayConfig;
    
    public VNPAYResponse createVnPayPayment(Map<String, Object> requestData, HttpServletRequest request) {
        long amount = ((Number) requestData.get("amount")).longValue() * 100;
        String orderId = (String) requestData.get("orderId");
        String language = (String) requestData.get("language");
        String bankCode = (String) requestData.getOrDefault("bankCode", "");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_OrderInfo", orderId);
        vnpParamsMap.put("vnp_TxnRef", orderId);
        vnpParamsMap.put("vnp_Locale", language);
        vnpParamsMap.put("vnp_IpAddr", VNPAYUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPAYUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPAYUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPAYUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VNPAYResponse.builder()
                .code("ok")
                .amount(amount)
                .message("success")
                .paymentUrl(paymentUrl).build();
    }
}

