package com.example.auth.service;

import com.example.auth.exception.ValidationException;
import com.example.auth.model.TheatrePackage;
import com.example.auth.repository.TheatrePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TheatrePackageService {
    private final TheatrePackageRepository theatrePackageRepository;

    public boolean isEligibleForFreeTicket(Long userId) {
        return theatrePackageRepository.findActivePackageByUserId(userId)
                .map(pkg -> pkg.getPlaysBooked() >= 4)
                .orElse(false);
    }

    public void useFreeTicket(Long userId) {
        TheatrePackage pkg = theatrePackageRepository.findActivePackageByUserId(userId)
                .orElseThrow(() -> new ValidationException("No active package found"));

        pkg.setPlaysBooked(0);
        pkg.setFreeTicketsEarned(pkg.getFreeTicketsEarned() + 1);
        theatrePackageRepository.save(pkg);
    }
}