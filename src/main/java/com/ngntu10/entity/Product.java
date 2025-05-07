package com.ngntu10.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @Index(columnList = "name", name = "idx_products_name"),
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractBaseEntity {
    @Column(name = "name")
    private String name;    
    
    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;
    
    @Column(name = "slug")
    private String slug;
    
}
