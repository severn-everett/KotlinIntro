package com.severett.kotlinintro.service;

import com.severett.kotlinintro.model.OfferedService;
import com.severett.kotlinintro.repo.OfferedServiceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
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

    public static class CreateOfferedServiceRequest {
        private final String name;
        private final BigDecimal price;

        public CreateOfferedServiceRequest(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CreateOfferedServiceRequest that)) return false;
            return getName().equals(that.getName()) && getPrice().equals(that.getPrice());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getPrice());
        }
    }

    public static class SetDiscountRequest {
        private final int id;
        private final BigDecimal discount;

        public SetDiscountRequest(int id, BigDecimal discount) {
            this.id = id;
            this.discount = discount;
        }

        public int getId() {
            return id;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SetDiscountRequest that)) return false;
            return getId() == that.getId() && getDiscount().equals(that.getDiscount());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getDiscount());
        }
    }
}
