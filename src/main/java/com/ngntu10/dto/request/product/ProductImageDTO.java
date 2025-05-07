package com.ngntu10.dto.request.product;

import com.ngntu10.dto.request.image.UploadImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {

    @NotBlank(message = "Product category is required")
    @Schema(
            name = "productId",
            description = "Product ID",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private String productId;
    
    @NotEmpty(message = "Product must have images")
    @Schema(
            name = "images",
            description = "List of product images",
            type = "Array",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<UploadImageDTO> images;
}
