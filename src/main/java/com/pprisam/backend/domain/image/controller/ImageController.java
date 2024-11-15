package com.pprisam.backend.domain.image.controller;

import com.pprisam.backend.domain.image.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Value("${image.storage.path}")
    private String IMAGE_FOLDER_PATH;

    private final FileStorageService fileStorageService;

    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/download-and-save")
    public ResponseEntity<String> downloadAndSaveImage(@RequestParam("url") String imageUrl) {
        try {
            String fileName = fileStorageService.downloadAndConvertToJPG(imageUrl);
            return ResponseEntity.ok(fileName); // 파일 이름만 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이미지 다운로드 및 변환 실패: " + e.getMessage());
        }
    }

    @GetMapping("/load-image")
    public ResponseEntity<String> loadImage(@RequestParam("fileName") String fileName) {
        try {
            String imagePath = Paths.get(IMAGE_FOLDER_PATH, fileName).toString();
            byte[] fileBytes = Files.readAllBytes(Paths.get(imagePath));
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);
            return ResponseEntity.ok(base64Data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = "/images/" + fileName;
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이미지 업로드 실패: " + e.getMessage());
        }
    }


}
