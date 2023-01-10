package com.severett.kotlinintro.controller;

import com.severett.kotlinintro.model.OfferedService;
import com.severett.kotlinintro.service.OfferedServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/offered_services", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class OfferedServiceController {
    private final OfferedServiceService offeredServiceService;

    @GetMapping("/{id}")
    public ResponseEntity<OfferedService> getOfferedService(@PathVariable int id) {
        return offeredServiceService.getOfferedService(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OfferedService> createOfferedService(
            @RequestBody OfferedServiceService.CreateOfferedServiceRequest request
    ) {
        try {
            return new ResponseEntity<>(offeredServiceService.createOfferedService(request), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Unable to persist {}", request, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/setDiscount")
    public ResponseEntity<OfferedService> setDiscount(@PathVariable int id, @RequestParam BigDecimal discount) {
        try {
            var request = new OfferedServiceService.SetDiscountRequest(id, discount);
            return ResponseEntity.ok(offeredServiceService.setDiscount(request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unable to set discount for offered service #{}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
