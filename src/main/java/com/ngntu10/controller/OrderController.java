package com.ngntu10.controller;

import com.ngntu10.constants.Endpoint;
import com.ngntu10.dto.request.ChangeOrderStatus;
import com.ngntu10.dto.request.CreateOrderDTO;
import com.ngntu10.dto.request.ListOrderRequest;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.entity.Order;
import com.ngntu10.service.Order.OrderService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order")
public class OrderController {
    public final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        try{
            return ResponseEntity.ok(orderService.createOrder(createOrderDTO));
        } catch(Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
//    @GetMapping
//    public ResponseEntity<?> getAllOrders(@RequestParam Map<Object, String> filters){
//        return ResponseEntity.ok(orderService.getAllOrder(filters));
//    }
    
    @GetMapping("/me")
    public ResponseEntity<APIResponse<Page<Order>>> getAllOrderUser(
            @ParameterObject @ModelAttribute ListOrderRequest listOrderRequest,
            @ParameterObject Pageable pageable){
        var result = orderService.getAllOrderByMe(listOrderRequest, pageable);
        return ResponseEntity.ok(new APIResponse<>(true, 200, result, ""));
    }
    
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<APIResponse<?>> cancelOrder(@PathVariable String orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok().build();
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new APIResponse<>(false, 400, null, ex.getMessage()));
        }
    }
    
    @GetMapping("/me/{orderId}")
    public ResponseEntity<APIResponse<Order>> getOrderMeById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOneOrderById(orderId));
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<APIResponse<Order>> getOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOneOrderById(orderId));
    }
    
    @PostMapping("/{orderId}/status")
    public ResponseEntity<?> changeOrderStatusById(@PathVariable String orderId,@RequestBody ChangeOrderStatus changeOrderStatus){
        try {
            orderService.changeStatusOrder(orderId, changeOrderStatus);
            return ResponseEntity.ok().build();
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
