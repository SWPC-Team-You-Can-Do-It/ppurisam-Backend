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
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SttService {
    @Value("${csr.client.id}")
    String clientId;

    @Value("${csr.client.secret}")
    String clientSecret;

    public File convertToMp3(MultipartFile file) throws IOException {
        // 임시 WEBM 파일 생성
        File tempWebmFile = File.createTempFile("upload-", ".webm");

        // 업로드된 파일을 임시 WEBM 파일로 저장
        file.transferTo(tempWebmFile);

        // WEBM 파일의 경로와 파일 크기 로그 출력
        log.info("WEBM 파일 경로: {}", tempWebmFile.getAbsolutePath());
        log.info("WEBM 파일 크기: {} bytes", tempWebmFile.length());

        // 파일이 비어 있거나 존재하지 않는 경우 예외 처리
        if (tempWebmFile.length() == 0) {
            throw new IOException("업로드된 WEBM 파일이 비어 있습니다.");
        }
        if (!tempWebmFile.exists()) {
            throw new IOException("임시 WEBM 파일이 존재하지 않습니다.");
        }

        // FFmpeg가 변환할 출력 파일 경로 지정
        String outputMp3Path = System.getProperty("java.io.tmpdir") + "/converted-" + UUID.randomUUID() + ".mp3";
        File mp3File = new File(outputMp3Path);

        try {
            // FFmpeg 프로세스 생성
            ProcessBuilder pb = new ProcessBuilder(
                    "/usr/bin/ffmpeg",
                    "-i", tempWebmFile.getAbsolutePath(),
                    "-vn",
                    "-ar", "44100",
                    "-ac", "2",
                    "-b:a", "192k",
                    "-c:a", "libmp3lame",
                    mp3File.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // FFmpeg의 출력 로그 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("FFmpeg: {}", line);
                }
            }

            // 변환 완료 대기 및 상태 확인
            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (!finished) {
                log.warn("FFmpeg 변환이 시간이 초과되었습니다. 프로세스를 종료합니다.");
                process.destroy(); // 먼저 정상 종료 요청
                // 잠시 대기 후 강제 종료
                try {
                    if (!process.waitFor(5, TimeUnit.SECONDS)) {
                        process.destroyForcibly(); // 강제 종료
                        log.warn("FFmpeg 프로세스가 강제 종료되었습니다.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("프로세스 종료 중 인터럽트 발생", e);
                }
                throw new IOException("FFmpeg 변환 타임아웃 발생");
            }

            // 변환 결과 확인
            if (process.exitValue() != 0) {
                throw new IOException("FFmpeg 변환 실패, 종료 코드: " + process.exitValue());
            }

            // 변환된 MP3 파일의 존재 여부 및 크기 확인
            if (!mp3File.exists() || mp3File.length() == 0) {
                throw new IOException("MP3 파일이 생성되지 않았거나 빈 파일입니다.");
            }

            log.info("변환 완료: {} -> {}", tempWebmFile.getAbsolutePath(), mp3File.getAbsolutePath());
            return mp3File; // 변환된 MP3 파일 반환

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg 변환 중단됨", e);
        } finally {
            if (tempWebmFile.exists() && !tempWebmFile.delete()) {
                log.warn("임시 WEBM 파일 삭제 실패: {}", tempWebmFile.getAbsolutePath());
            }
        }
    }



    public SttResponse speechToText(MultipartFile file) {
//        log.info("클라이언트 id {}", clientId);
//        log.info("클라이언트 시크릿 {}", clientSecret);

        File tempFile = null;
        try {
            // 파일이 비어있는지 확인
            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어 있습니다.");
            }

            // WEBM 파일을 MP3로 변환하여 클로바 API와 호환되도록 조정
            tempFile = convertToMp3(file);

            // MultipartFile을 File 객체로 변환하여 처리 (임시 파일 생성)
            // 임시 파일 생성
//            File tempFile = File.createTempFile("upload-", ".mp3");
            // 업로드된 파일을 임시 파일에 저장
//            file.transferTo(tempFile);

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
        } finally {
            // 변환된 mp3 파일 삭제
            if (tempFile != null && tempFile.exists() && !tempFile.delete()) {
                log.warn("변환된 mp3 파일 삭제 실패: {}", tempFile.getAbsolutePath());
            }
        }
    }
}
