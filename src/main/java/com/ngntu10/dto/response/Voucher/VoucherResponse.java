package com.ngntu10.dto.response.Voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponse {

    private BigDecimal discountPercentage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
