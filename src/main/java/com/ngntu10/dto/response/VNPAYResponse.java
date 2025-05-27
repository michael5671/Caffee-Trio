package com.ngntu10.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPAYResponse {
    public String code;
    public String message;
    public String paymentUrl;
    public long amount;
}
