package com.ngntu10.dto.response.Product;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    
    private String name;

    private String description;

    private String imageUrl;

    private String category;
}
