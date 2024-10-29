package com.pprisam.backend.domain.ai.service;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.service.OpenAiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

@Slf4j
@Service
public class ImageAIService {

    private final OpenAiService openAiService;

    public ImageAIService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    // 이미지 생성 후, 로컬에 저장하여 그 경로를 반환
    public String generate(String prompt) {
        log.info("prompt : {}", prompt);

        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt) // 프롬프트 설정
                .model("dall-e-3")  // DALL-E 3 모델 사용
                .n(1) // 생성할 이미지 수
                .build()
                ;

        // 이미지 생성 요청
        ImageResult response = openAiService.createImage(request);

        // 생성된 이미지 URL 확인
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            log.error("이미지 생성이 실패했습니다.");
            return null;
        }

        // 생성된 이미지 URL
        String imageUrl = response.getData().getFirst().getUrl();

        try {
            // URL을 통해 이미지 다운로드
            URI uri = new URI(imageUrl);
            URL url = uri.toURL();
            BufferedImage image = ImageIO.read(url);

            // 이미지 파일 크기 조정 (필요한 경우)
            image = reduceFileSize(image, 300 * 1024); // 300KB

            return saveImage(image);
        } catch (Exception e) {
            log.error("이미지 처리 중 오류 발생", e);
            return null;
        }
    }

    // 이미지 파일 용량 조정
    private BufferedImage reduceFileSize (BufferedImage originalImage, int maxSizeInBytes) throws Exception {
        BufferedImage modifiedImage = originalImage;
        float quality = 1.0f;

        while (true) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(modifiedImage, "jpg", baos);

            if (baos.size() <= maxSizeInBytes) {
                log.info("이미지 파일 크기가 {} 이내입니다 (이미지 크기 : {})", maxSizeInBytes, baos.size());
                break;
            }

            quality -= 0.1f;

            if (quality < 0.1f) {
                throw new Exception("이미지를 충분히 압축할 수 없습니다.");
            }

            baos.reset();

            // 품질을 낮추어 파일 크기 줄이기
            modifiedImage = compressImage(originalImage, quality);
        }

        return modifiedImage;
    }

    // 이미지 압축
    private BufferedImage compressImage(BufferedImage image, float quality) {
        log.info("이미지 압축 실행 품질 - {}", quality);

        BufferedImage compressedImage = null; // 압축된 이미지를 저장할 변수

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

            // JPEG 형식의 ImageWriter 가져오기
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(ios);

            // ImageWriteParam을 통해 품질 설정
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);  // quality 변수로 품질 설정
            }

            // 이미지 압축 및 쓰기
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
            writer.dispose();
            ios.close();

            // 압축된 이미지를 BufferedImage로 변환하여 반환
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            compressedImage = ImageIO.read(is);
        } catch (Exception e) {
            log.error("이미지 압축 중 오류 발생", e);
        }

        return compressedImage;
    }

    // 이미지를 로컬에 저장
    private String saveImage(BufferedImage image) throws Exception {
        int count = 1;
        File outputFile;

        do {
            outputFile = new File("/app/static/images/ai_image_" + count + ".jpg");
            count++;
        } while (outputFile.exists());

        // 이미지 저장
        ImageIO.write(image, "jpg", outputFile);

        return "/images/ai_image_" + (count - 1) + ".jpg";
    }
}
