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

    private String productId;
    
    private List<UploadImageDTO> images;
}
