package com.severett.kotlinintro.service;

import com.severett.kotlinintro.model.OfferedService;
import com.severett.kotlinintro.repo.OfferedServiceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class OfferedServiceService {
    private final OfferedServiceRepo offeredServiceRepo;

    public Optional<OfferedService> getOfferedService(int id) {
        return offeredServiceRepo.findById(id);
    }

    @Transactional
    public OfferedService createOfferedService(CreateOfferedServiceRequest request) {
        var offeredService = OfferedService.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        return offeredServiceRepo.save(offeredService);
    }

    @Transactional
    public OfferedService setDiscount(SetDiscountRequest request) {
        var newOfferedService = offeredServiceRepo.findById(request.id).map(offeredService ->
                offeredService.toBuilder()
                        .discount(request.getDiscount())
                        .build()
        ).orElseThrow(() -> new EntityNotFoundException("No offered service found with id " + request.getId()));
        return offeredServiceRepo.save(newOfferedService);
    }

    @AllArgsConstructor
    @Value
    public static class CreateOfferedServiceRequest {
        String name;
        BigDecimal price;
    }

    @AllArgsConstructor
    @Value
    public static class SetDiscountRequest {
        int id;
        BigDecimal discount;
    }
}
