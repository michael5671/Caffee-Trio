package com.ngntu10.dto.request.voucher;


import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class VoucherDTO {
    private BigDecimal discountPercentage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
