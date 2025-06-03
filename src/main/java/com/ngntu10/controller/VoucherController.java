package com.ngntu10.controller;

import com.ngntu10.dto.request.voucher.VoucherDTO;
import com.ngntu10.dto.response.Voucher.VoucherResponse;
import com.ngntu10.repository.VoucherRepository;
import com.ngntu10.service.Voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;
    private final VoucherRepository voucherRepository;

    @PostMapping
    public ResponseEntity<VoucherResponse> createVoucher(@RequestBody VoucherDTO request) {
        VoucherResponse created = voucherService.createVoucher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherResponse> updateVoucher(@PathVariable UUID id, @RequestBody VoucherDTO request) {
        VoucherResponse updated = voucherService.updateVoucher(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable UUID id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public List<VoucherResponse> getAll() {
        return voucherService.getAllVouchers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(voucherService.getVoucherById(id));
    }

}
