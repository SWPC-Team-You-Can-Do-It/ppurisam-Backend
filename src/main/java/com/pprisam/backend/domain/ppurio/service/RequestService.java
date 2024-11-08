package com.pprisam.backend.domain.ppurio.service;

import com.pprisam.backend.domain.ppurio.model.SendRequest;
import com.pprisam.backend.domain.ppurio.model.Target;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;

@Service
public class RequestService {
    @Value("${ppurio.uri}")
    private String URI;

    @Value("${ppurio.image.path}")
    private String IMAGE_FOLDER_PATH; // 클래스패스 기반 이미지 폴더 경로

    private final RestTemplate restTemplate;
    private final PpurioAuthService ppurioAuthService;

    public RequestService(RestTemplate restTemplate, PpurioAuthService ppurioAuthService) {
        this.restTemplate = restTemplate;
        this.ppurioAuthService = ppurioAuthService;
    }

    /**
     * MMS/SMS 발송 메서드 (요청 데이터 기반)
     *
     * @param sendRequest MMS/SMS 발송 요청 데이터
     */
    public void requestSend(SendRequest sendRequest) {
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
            } else {
                throw new RuntimeException("메시지 발송 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
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
    private Map<String, Object> createSendParams(SendRequest sendRequest) throws IOException {
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
     * @param resourcePath 클래스패스 기반의 리소스 경로 (예: "static/images/cat.jpg")
     * @return Map<String, Object>
     * @throws IOException
     */
    private Map<String, Object> createFileParams(String resourcePath) throws IOException {
        // 클래스패스 내의 리소스를 로드
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + resourcePath);
        }

        String fileName = resource.getFilename();
        long fileSize = resource.contentLength();

        // 파일 이름 검증
        if (fileName == null || (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".jpeg"))) {
            throw new IllegalArgumentException("MMS 발송 시 지원되는 파일 형식은 jpg, jpeg 입니다.");
        }

        // 파일 크기 검증
        if (fileSize > 300 * 1024) { // 300KB
            throw new IllegalArgumentException("파일 크기가 300KB를 초과합니다.");
        }

        byte[] fileBytes;
        // InputStream을 사용하여 파일을 읽음
        try (InputStream inputStream = resource.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }

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