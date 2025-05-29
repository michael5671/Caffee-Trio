package com.ngntu10.dto.request.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CreateOrderDTO {

    private List<OrderItemDTO> items;
}
