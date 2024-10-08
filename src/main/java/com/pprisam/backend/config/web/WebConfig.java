package com.pprisam.backend.config.web;

import com.pprisam.backend.interceptor.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    //회원가입, 로그인 등 비로그인한 상태
    private List<String> OPEN_API = List.of(
            "/open-api/**"
    );

    //기본 제외 주소
    private List<String> DEFAULT_EXCLUDE = List.of(
            "/", //루트 주소
            "favicon.ico", //아이콘 요청 주소
            "/error" // 에러 처리 주소
    );

    //Swagger 관련 주소
    private List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                // Interceptor의 인증을 제외시킴
                .excludePathPatterns(OPEN_API)
                .excludePathPatterns(DEFAULT_EXCLUDE)
                .excludePathPatterns(SWAGGER)
        ;
    }
}
