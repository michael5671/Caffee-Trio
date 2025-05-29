package com.ngntu10.dto.response.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Cửa tự động")
    private String name;

    @Schema(description = "Category description", example = "Danh mục các loại cửa tự động")
    private String description;

    @Schema(description = "Created date time")
    private LocalDateTime createdAt;

    @Schema(description = "Updated date time")
    private LocalDateTime updatedAt;
} 