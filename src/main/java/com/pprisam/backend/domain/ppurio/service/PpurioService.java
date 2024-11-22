package com.pprisam.backend.domain.ppurio.service;

import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PpurioService {

    private final RequestService requestService;

    public void send(SendRequest sendRequest, User user) {
        requestService.requestSend(sendRequest, user);
    }
}