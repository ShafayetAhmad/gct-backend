package com.example.auth.service;

import com.example.auth.dto.PlayRequest;
import com.example.auth.dto.PlayResponse;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.Play;
import com.example.auth.repository.PlayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayService {
    private final PlayRepository playRepository;

    public PlayResponse createPlay(PlayRequest request) {
        Play play = new Play();
        play.setTitle(request.getTitle());
        play.setDescription(request.getDescription());
        return mapToPlayResponse(playRepository.save(play));
    }

    public List<PlayResponse> getAllPlays() {
        return playRepository.findAll().stream()
                .map(this::mapToPlayResponse)
                .collect(Collectors.toList());
    }

    public PlayResponse getPlayById(Long id) {
        return mapToPlayResponse(findPlayById(id));
    }

    public PlayResponse updatePlay(Long id, PlayRequest request) {
        Play play = findPlayById(id);
        play.setTitle(request.getTitle());
        play.setDescription(request.getDescription());
        return mapToPlayResponse(playRepository.save(play));
    }

    public void deletePlay(Long id) {
        Play play = findPlayById(id);
        playRepository.delete(play);
    }

    private Play findPlayById(Long id) {
        return playRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Play not found with id: " + id));
    }

    private PlayResponse mapToPlayResponse(Play play) {
        return PlayResponse.builder()
                .id(play.getId())
                .title(play.getTitle())
                .description(play.getDescription())
                .build();
    }
}