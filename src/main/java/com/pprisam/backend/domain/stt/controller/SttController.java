package com.pprisam.backend.domain.stt.controller;

import com.pprisam.backend.domain.stt.model.SttResponse;
import com.pprisam.backend.domain.stt.service.SttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stt")
public class SttController {

    private final SttService sttService;

    @Value("${csr.client.id}")
    String clientId;

    @Value("${csr.client.secret}")
    String clientSecret;

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<SttResponse> speechToText(@RequestPart MultipartFile file) {
        try{
            return ResponseEntity.ok().body(sttService.speechToText(file));
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(null);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
