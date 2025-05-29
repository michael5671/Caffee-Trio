package com.ngntu10.dto.response.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    @Schema(description = "Product ID", example = "1")
    private UUID id;

    @Schema(description = "Product name", example = "Cửa sắt AKO-120")
    private String name;

    @Schema(description = "Product description", example = "Đây là cửa tự động")
    private String description;

    @Schema(description = "Product price", example = "1500000.00")
    private BigDecimal price;

    @Schema(description = "Product image URL", example = "https://example.com/images/product.jpg")
    private String imageUrl;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Category name", example = "Cửa tự động")
    private String categoryName;

    @Schema(description = "Created date time")
    private LocalDateTime createdAt;

    @Schema(description = "Updated date time")
    private LocalDateTime updatedAt;
}
