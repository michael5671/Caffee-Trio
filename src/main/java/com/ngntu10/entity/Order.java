package com.ngntu10.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Order extends AbstractBaseEntity {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;

    @Column
    private Long itemsPrice;

    @Column(nullable = false)
    private Long totalPrice;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int isPaid = 0;

    @Column(nullable = false)
    private int isDelivered = 0;

    @Column
    private Date paidAt = new Date();

    @Column(name = "deliveryAt")
    private Date deliveryAt = new Date();

    @Column(name = "status")
    private int orderStatus = 0;
}
