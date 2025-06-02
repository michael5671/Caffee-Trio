package com.ngntu10.service.Voucher;

import com.ngntu10.dto.request.voucher.VoucherDTO;
import com.ngntu10.dto.response.Voucher.VoucherResponse;
import com.ngntu10.entity.Voucher;
import com.ngntu10.repository.VoucherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final ModelMapper modelMapper;

    public VoucherResponse createVoucher(VoucherDTO request) {
        Voucher voucher = modelMapper.map(request, Voucher.class);
        voucher.setId(UUID.randomUUID());
        voucherRepository.save(voucher);
        return modelMapper.map(voucher, VoucherResponse.class);
    }

    public VoucherResponse updateVoucher(UUID id, VoucherDTO request) {
        Voucher existing = voucherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voucher not found"));

        modelMapper.map(request, existing); // override fields
        voucherRepository.save(existing);
        return modelMapper.map(existing, VoucherResponse.class);
    }

    public void deleteVoucher(UUID id) {
        if (!voucherRepository.existsById(id)) {
            throw new EntityNotFoundException("Voucher not found");
        }
        voucherRepository.deleteById(id);
    }
    public List<VoucherResponse> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(v -> modelMapper.map(v, VoucherResponse.class))
                .collect(Collectors.toList());
    }
    public VoucherResponse getVoucherById(UUID id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voucher not found"));
        return modelMapper.map(voucher, VoucherResponse.class);
    }
}
