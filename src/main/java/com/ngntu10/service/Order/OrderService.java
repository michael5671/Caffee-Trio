package com.ngntu10.service.Order;

import com.ngntu10.dto.request.ChangeOrderStatus;
import com.ngntu10.dto.request.CreateOrderDTO;
import com.ngntu10.dto.request.ListOrderRequest;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.entity.Order;
import com.ngntu10.entity.OrderItem;
import com.ngntu10.entity.Product;
import com.ngntu10.entity.User;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.OrderRepository;
import com.ngntu10.repository.ProductRepository;
import com.ngntu10.repository.UserRepository;
import com.ngntu10.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService{
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Transactional
    public APIResponse<Order> createOrder(CreateOrderDTO createOrderDTO) {
        Order order = modelMapper.map(createOrderDTO, Order.class);
        User user = userRepository.findById(UUID.fromString(createOrderDTO.getUserId()))
                .orElseThrow(() -> new NotFoundException("User not exist"));
        order.setUser(user);
        order.setPaymentMethod(createOrderDTO.getPaymentType());
        order.setOrderStatus(0);
        order.setUser(user);
        order = orderRepository.save(order);
        Order finalOrder = order;
        List<OrderItem> orderItems = createOrderDTO.getOrderItems().stream()
                .map(item -> {
                    OrderItem orderItem = modelMapper.map(item, OrderItem.class);
                    orderItem.setOrder(finalOrder);
//                    orderItem.setId(productId.getId());
                    return orderItem;
                }).collect(Collectors.toList());
        order.setOrderItemList(orderItems);
        orderRepository.save(order);
        List<Order> orders = user.getOrderList();
        orders.add(order);
        user.setOrderList(orders);
        userRepository.save(user);
        return new APIResponse<>(true, 200, order, "Order created successfully");
    }

    @Transactional
    public void cancelOrder(String id) {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setOrderStatus(3);
        orderRepository.save(order);
    }

    public APIResponse<Order> getOneOrderById(String id) {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return new APIResponse<>(true, 200, order, "");
    }

    @Transactional
    public void handlePaymentOrderById(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.getOrderItemList().forEach(orderItem -> {
            Product product = new Product();
            int amount = orderItem.getAmount();
            product.setSold(product.getSold() + amount);
            productRepository.save(product);
        });
        order.setOrderStatus(2);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(String id) {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Transactional
    public Order changeStatusOrder(String orderId, ChangeOrderStatus changeOrderStatus) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setOrderStatus(changeOrderStatus.getStatus());
        orderRepository.save(order);

        User user = order.getUser();
        userRepository.save(user);
        return order;
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllOrderByMe(ListOrderRequest listOrderRequest, Pageable pageable) {
        User user = userService.getUser();

        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        Page<Order> result = new  PageImpl<Order>(orderList, pageable, orderList.size());

        return result;
    }
    
}

