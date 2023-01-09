package com.severett.kotlinintro.service;

import com.severett.kotlinintro.model.OfferedService;
import com.severett.kotlinintro.repo.OfferedServiceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
                .name(request.name())
                .price(request.price())
                .build();
        return offeredServiceRepo.save(offeredService);
    }

    @Transactional
    public OfferedService setDiscount(SetDiscountRequest request) {
        var newOfferedService = offeredServiceRepo.findById(request.id).map(offeredService ->
                offeredService.toBuilder()
                        .discount(request.discount())
                        .build()
        ).orElseThrow(() -> new EntityNotFoundException("No offered service found with id " + request.id()));
        return offeredServiceRepo.save(newOfferedService);
    }

    public record CreateOfferedServiceRequest(String name, BigDecimal price) {
    }

    public record SetDiscountRequest(int id, BigDecimal discount) {
    }
}
