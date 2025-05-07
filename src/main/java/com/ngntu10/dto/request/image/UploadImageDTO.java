package com.ngntu10.dto.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageDTO {
    @JsonProperty("isThumbnail")
    @NotBlank(message = "Thumbnail check is required")
    @Schema(
            name = "isThumbnail",
            description = "Indicates whether the product image is thumbnail or not",
            type = "boolean",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "true"
    )
    private boolean thumbnail;
    
    @JsonProperty("secure_url")
    @NotBlank(message = "Secure URL is required")
    @Schema(
            name = "secure_url",
            description = "The secure URL of the image after upload",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "https://res.cloudinary.com/your_cloud_name/image/upload/v1614770203/mbjf0dum95iijqwrxdzx.jpg"
    )
    private String url;

    @NotBlank(message = "Format is required")
    @Schema(
            name = "format",
            description = "The file format of the image (e.g., JPG, PNG)",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "jpg"
    )
    private String format;

    @NotBlank(message = "Asset folder is required")
    @Schema(
            name = "asset_folder",
            description = "The folder where the image is stored in Cloudinary",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "ngntu10-product"
    )
    private String assetFolder;

    @NotNull(message = "Width is required")
    @Schema(
            name = "width",
            description = "Width of the image in pixels",
            type = "Integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1920"
    )
    private int width;

    @NotNull(message = "Height is required")
    @Schema(
            name = "height",
            description = "Height of the image in pixels",
            type = "Integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1080"
    )
    private int height;

    @NotNull(message = "File size (bytes) is required")
    @Schema(
            name = "bytes",
            description = "The size of the image file in bytes",
            type = "Integer",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "105575"
    )
    private int bytes;

    @NotBlank(message = "Display name is required")
    @Schema(
            name = "display_name",
            description = "The display name for the image",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "HSG"
    )
    private String displayName;
}
