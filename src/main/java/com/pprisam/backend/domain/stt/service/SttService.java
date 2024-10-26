package com.pprisam.backend.domain.stt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pprisam.backend.domain.stt.model.SttResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class SttService {
    @Value("${csr.client.id}")
    String clientId;

    @Value("${csr.client.secret}")
    String clientSecret;

    public SttResponse speechToText(MultipartFile file) {
        log.info("클라이언트 id {}", clientId);
        log.info("클라이언트 시크릿 {}", clientSecret);
        try {
            // 파일이 비어있는지 확인
            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어 있습니다.");
            }
            // MultipartFile을 File 객체로 변환하여 처리 (임시 파일 생성)
            // 임시 파일 생성
            File tempFile = File.createTempFile("upload-", ".mp3");
            // 업로드된 파일을 임시 파일에 저장
            file.transferTo(tempFile);

            // URL 생성
            String language = "Kor";        // 언어 코드 ( Kor, Jpn, Eng, Chn )
            String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
            URL url = new URL(apiURL);

            // http 연결 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // http 헤더 설정
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            // 파일 데이터를 API로 전송
            // 파일을 inputStream으로 읽고 outputStream을 통해 보내기
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(tempFile); // 임시 파일로부터 값을 읽어오는 역할
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            // 응답 읽어오기
            BufferedReader br = null;
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 오류 발생
                throw new RuntimeException("잘못된 응답");
            }

            // 응답텍스트 붙이기
            String inputLine;
            StringBuilder response = new StringBuilder();
            if (br != null) {
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine); // SringBuilder로 문자열 이어붙이기
                }
                br.close();
            }

            log.info("변환된 response.toString()의 결과 {}", response.toString());

            // JSON 파싱하여 "text" 필드만 추출
            // 텍스트 -> json -> "text" 부분 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString()); //
            String extractedText = jsonNode.get("text").asText();  // "text" 필드 값만 추출

            return SttResponse.builder()
                    .text(extractedText)  // 추출한 텍스트를 DTO에 담음
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
