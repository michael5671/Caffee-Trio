package com.ngntu10.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ngntu10.entity.OrderItem;
import com.ngntu10.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private String address;
    private String fullName;
    private Long itemsPrice;
    private List<OrderItem> orderItems;
    private PaymentType paymentType;
    private String phone;
    private Long totalPrice;
    private String userId;
}
