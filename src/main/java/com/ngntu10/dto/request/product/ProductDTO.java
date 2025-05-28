package com.ngntu10.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Product name is required")
    @Schema(
            name = "name",
            description = "Product name",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Cửa sắt AKO-120"
    )
    private String name;

    @Schema(
            name = "description",
            description = "Product description",
            type = "String",
            example = "Đây là cửa tự động"
    )
    private String description;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(
            name = "price",
            description = "Product price",
            type = "number",
            format = "decimal",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1500000.00"
    )
    private BigDecimal price;

    @Schema(
            name = "imageUrl",
            description = "Product image URL",
            type = "String",
            example = "https://example.com/images/product.jpg"
    )
    private String imageUrl;

    @NotNull(message = "Category ID is required")
    @Schema(
            name = "categoryId",
            description = "Category ID",
            type = "integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1"
    )
    private Long categoryId;
}
