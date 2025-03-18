package com.example.auth.service;

import com.example.auth.dto.PlayRequest;
import com.example.auth.dto.PlayResponse;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.Play;
import com.example.auth.repository.PlayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayServiceTest {

    @Mock
    private PlayRepository playRepository;

    @InjectMocks
    private PlayService playService;

    private Play play;
    private PlayRequest playRequest;

    @BeforeEach
    void setUp() {
        play = new Play();
        play.setId(1L);
        play.setTitle("Test Play");
        play.setDescription("Test Description");

        playRequest = new PlayRequest();
        playRequest.setTitle("Test Play");
        playRequest.setDescription("Test Description");
    }

    @Test
    void createPlay_Success() {
        when(playRepository.save(any(Play.class))).thenReturn(play);

        PlayResponse response = playService.createPlay(playRequest);

        assertNotNull(response);
        assertEquals(play.getId(), response.getId());
        assertEquals(play.getTitle(), response.getTitle());
        assertEquals(play.getDescription(), response.getDescription());
        verify(playRepository).save(any(Play.class));
    }

    @Test
    void getPlay_Success() {
        when(playRepository.findById(1L)).thenReturn(Optional.of(play));

        PlayResponse response = playService.getPlay(1L);

        assertNotNull(response);
        assertEquals(play.getId(), response.getId());
        verify(playRepository).findById(1L);
    }

    @Test
    void getPlay_NotFound() {
        when(playRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playService.getPlay(1L));
        verify(playRepository).findById(1L);
    }
}