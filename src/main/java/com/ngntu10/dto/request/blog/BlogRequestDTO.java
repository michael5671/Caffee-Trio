package com.ngntu10.dto.request.blog;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequestDTO {
    @JsonProperty("thumbnail_url")
    @NotBlank(message = "{not_blank}")
    @Schema(
            name = "thumbnail",
            description = "Thumbnail's url of the blog",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "https://res.cloudinary.com/your_cloud_name/image/upload/v1614770203/mbjf0dum95iijqwrxdzx.jpg"
    )
    private String thumbnailURL;
    
    @NotBlank(message = "{not_blank}")
    @Schema(
            name = "category",
            description = "Category of the blog",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Tin tức"
    )
    private String category;

    @NotBlank(message = "{not_blank}")
    @Schema(
            name = "title",
            description = "Title of the blog",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Tiêu đề 1"
    )
    private String title;

    @NotBlank(message = "{not_blank}")
    @Schema(
            name = "slug",
            description = "Slug of the blog",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "tieu-de-1-e2f836u1"
    )
    private String slug;

    @NotBlank(message = "{not_blank}")
    @Schema(
            name = "content",
            description = "Content of the blog",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = ""
    )
    private String content;
}