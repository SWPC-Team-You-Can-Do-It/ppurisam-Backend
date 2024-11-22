package com.pprisam.backend.domain.ppurio.service;

import com.pprisam.backend.domain.message.service.MessageService;
import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.ppurio.model.Target;
import com.pprisam.backend.domain.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class RequestService {
    @Value("${ppurio.uri}")
    private String URI;

    @Value("${ppurio.image.path}")
    private String IMAGE_FOLDER_PATH;

    private final RestTemplate restTemplate;
    private final PpurioAuthService ppurioAuthService;
    private final MessageService messageService;

    public RequestService(RestTemplate restTemplate, PpurioAuthService ppurioAuthService, MessageService messageService) {
        this.restTemplate = restTemplate;
        this.ppurioAuthService = ppurioAuthService;
        this.messageService=messageService;
    }

    /**
     * MMS/SMS 발송 메서드 (요청 데이터 기반)
     *
     * @param sendRequest MMS/SMS 발송 요청 데이터
     */
    public void requestSend(SendRequest sendRequest, User user) {
        String accessToken = ppurioAuthService.getAccessToken().getToken();

        Map<String, Object> sendParams;
        try {
            sendParams = createSendParams(sendRequest);
        } catch (IOException e) {
            throw new RuntimeException("발송 파라미터 생성 중 오류 발생: " + e.getMessage(), e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(sendParams, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    URI + "/v1/message",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Message sent successfully: " + response.getBody());
                messageService.saveMessage(sendRequest, user, true);
            } else {
                messageService.saveMessage(sendRequest, user, false);
                throw new RuntimeException("메시지 발송 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            messageService.saveMessage(sendRequest, user, false);
            throw new RuntimeException("메시지 발송 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * MMS/SMS 발송 파라미터 생성 (요청 데이터 기반)
     *
     * @param sendRequest MMS/SMS 발송 요청 데이터
     * @return Map<String, Object>
     * @throws IOException
     */
    public Map<String, Object> createSendParams(SendRequest sendRequest) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", sendRequest.getAccount());
        params.put("messageType", sendRequest.getMessageType());
        params.put("from", sendRequest.getFrom());
        params.put("content", sendRequest.getContent());
        params.put("duplicateFlag", sendRequest.getDuplicateFlag());
        params.put("targetCount", sendRequest.getTargetCount());

        List<Map<String, Object>> targets = new ArrayList<>();
        for (Target target : sendRequest.getTargets()) {
            Map<String, Object> targetMap = new HashMap<>();
            targetMap.put("to", target.getTo());
            targetMap.put("name", target.getName());
            targetMap.put("changeWord", target.getChangeWord());
            targets.add(targetMap);
        }
        params.put("targets", targets);

        params.put("refKey", sendRequest.getRefKey());

        if (sendRequest.getRejectType() != null && !sendRequest.getRejectType().isEmpty()) {
            params.put("rejectType", sendRequest.getRejectType());
        }

        if (sendRequest.getSendTime() != null && !sendRequest.getSendTime().isEmpty()) {
            params.put("sendTime", sendRequest.getSendTime());
        }

        if (sendRequest.getSubject() != null && !sendRequest.getSubject().isEmpty()) {
            params.put("subject", sendRequest.getSubject());
        }

        // messageType이 MMS 또는 LMS인 경우에만 files 필드 추가
        if ("MMS".equalsIgnoreCase(sendRequest.getMessageType()) || "LMS".equalsIgnoreCase(sendRequest.getMessageType())) {
            if (sendRequest.getFiles() != null && !sendRequest.getFiles().isEmpty()) {
                // 이미지 파일의 전체 경로 생성
                String imagePath = IMAGE_FOLDER_PATH + sendRequest.getFiles().get(0).getName(); // 첫 번째 파일만 처리
                params.put("files", List.of(
                        createFileParams(imagePath)
                ));
            } else {
                throw new IllegalArgumentException("MMS/LMS 발송 시 파일은 필수입니다.");
            }
        }

        return params;
    }

    /**
     * MMS/SMS 발송 요청 시 파일 파라미터 생성 (MMS/LMS에만 사용)
     *
     * @param imagePath 이미지 파일의 전체 경로
     * @return Map<String, Object>
     * @throws IOException
     */
    private Map<String, Object> createFileParams(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + imagePath);
        }

        String fileName = imageFile.getName();
        long fileSize = imageFile.length();

        // 파일 이름 검증
        if (fileName == null || (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".jpeg"))) {
            throw new IllegalArgumentException("MMS 발송 시 지원되는 파일 형식은 jpg, jpeg 입니다.");
        }

        // 파일 크기 검증
        if (fileSize > 300 * 1024) { // 300KB
            throw new IllegalArgumentException("파일 크기가 300KB를 초과합니다.");
        }

        // 파일 읽기
        byte[] fileBytes = Files.readAllBytes(imageFile.toPath());

        // Base64 인코딩
        String encodedFileData = Base64.getEncoder().encodeToString(fileBytes);

        // 파라미터 맵 생성
        HashMap<String, Object> fileParams = new HashMap<>();
        fileParams.put("name", fileName);
        fileParams.put("size", fileSize);
        fileParams.put("data", encodedFileData);
        return fileParams;
    }

}
