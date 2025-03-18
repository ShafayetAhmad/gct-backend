package com.example.auth.service;

import com.example.auth.dto.PackageResponse;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.TheatrePackage;
import com.example.auth.repository.TheatrePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final TheatrePackageRepository theatrePackageRepository;

    @Transactional(readOnly = true)
    public PackageResponse getUserPackage(Long userId) {
        TheatrePackage theatrePackage = theatrePackageRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre package not found"));
        return PackageResponse.builder()
                .playsBooked(theatrePackage.getPlaysBooked())
                .freeTicketsEarned(theatrePackage.getFreeTicketsEarned())
                .isActive(theatrePackage.getIsActive())
                .build();
    }

    public boolean isEligibleForFreeTicket(Long userId) {
        return theatrePackageRepository.findByUserId(userId)
                .map(tp -> tp.getIsActive() && tp.getFreeTicketsEarned() > 0)
                .orElse(false);
    }

    @Transactional
    public void useFreeTicket(Long userId) {
        TheatrePackage theatrePackage = theatrePackageRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre package not found"));

        if (theatrePackage.getFreeTicketsEarned() <= 0) {
            throw new ResourceNotFoundException("No free tickets available");
        }

        theatrePackage.setFreeTicketsEarned(theatrePackage.getFreeTicketsEarned() - 1);
        theatrePackageRepository.save(theatrePackage);
    }
}