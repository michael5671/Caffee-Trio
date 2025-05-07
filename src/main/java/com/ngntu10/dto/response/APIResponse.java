package com.ngntu10.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse<T>{
    private Boolean error = false;
    private Integer statusCode;
    private T data;
    private String message;
}
