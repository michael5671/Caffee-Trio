package com.ngntu10.dto.request.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for deleting multiple documents")
public class DeleteMultiDocumentDTO {
    @NotEmpty(message = "Documents IDs cannot be empty")
    @Schema(
            name = "documentIds",
            description = "Danh sách ID tài liệu",
            type = "array",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "[\"54505b04-6e56-4435-8870-eb968badf7ec\", " +
                    "\"b79433f9-69fe-4289-825d-8865b693d13e\", " +
                    "\"7e9aa8d1-3341-401b-a15a-3436dd163603\"]"
    )
    private List<String> documentIds;
}
