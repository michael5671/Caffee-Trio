package com.ngntu10.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem extends AbstractBaseEntity {

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne
    @JsonUnwrapped
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private Order order;
}
