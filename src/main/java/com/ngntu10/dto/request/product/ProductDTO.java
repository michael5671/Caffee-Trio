package com.ngntu10.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
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

    @NotBlank(message = "Product description is required")
    @Schema(
            name = "description",
            description = "Product description",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Đây là cửa tự động"
    )
    private String description;

    @NotBlank(message = "Product slug is required")
    @Schema(
            name = "slug",
            description = "Product description",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "cua-tu-dong"
    )
    private String slug;

    @NotBlank(message = "Product category is required")
    @Schema(
            name = "category",
            description = "Product category",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Cửa tự động"
    )
    private String category;

    @NotEmpty(message = "Product attributes cannot be empty")
    @Schema(
            name = "attributes",
            description = "Map of product attributes",
            type = "object", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = """
        {
          "Kích thước": "120x200cm",
          "Chất liệu": "Thép không gỉ",
          "Màu sắc": "Đen",
          "Xuất xứ": "Việt Nam"
        }
        """,
            implementation = Map.class
    )
    private Map<String, String> attributes;

}
