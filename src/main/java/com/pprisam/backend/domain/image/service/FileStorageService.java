package com.pprisam.backend.domain.image.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${image.storage.path}") String storagePath) {
        this.fileStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
            System.out.println("이미지 저장 디렉토리 생성 또는 존재함: " + fileStorageLocation.toString());
        } catch (Exception ex) {
            throw new RuntimeException("이미지 저장 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    public String downloadAndConvertToJPG(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                throw new RuntimeException("이미지 파일 형식이 유효하지 않습니다.");
            }
            String fileName = "image_" + UUID.randomUUID() + ".jpg";
            Path targetPath = fileStorageLocation.resolve(fileName);
            System.out.println("이미지 저장 경로: " + targetPath.toString());
            ImageIO.write(image, "jpg", targetPath.toFile());
            return fileName;
        } catch (Exception e) {
            e.printStackTrace(); // 예외의 전체 스택 트레이스 출력
            throw new RuntimeException("이미지 다운로드 및 변환 실패: " + e.getMessage(), e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String fileName = "image_" + UUID.randomUUID() + ".jpg";
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("이미지 저장 실패: " + ex.getMessage(), ex);
        }
    }
}
