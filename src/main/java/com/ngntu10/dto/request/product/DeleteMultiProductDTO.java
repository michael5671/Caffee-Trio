package com.ngntu10.dto.request.product;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for deleting multiple products")
public class DeleteMultiProductDTO {
    private List<String> productIds;
}
