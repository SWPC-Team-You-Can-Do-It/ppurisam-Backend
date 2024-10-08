package com.pprisam.backend.interceptor;

import com.pprisam.backend.domain.token.business.TokenBusiness;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;

    // Controller 실행 전에 검증을 담당하는 메소드
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청 URL 출력
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // OPTIONS 메소드는 CORS 프리플라이트 요청으로 통과
        // 프리플라이트 요청 : CORS 문제 방지를 위해 요청이 가능한지를 미리 확인하는 요청
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // 정적 리소스(js, html, png 등)에 대한 요청은 통과
        if(handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // Request-Header에서 JWT 토큰을 추출
        var accessToken = request.getHeader("authorization-token");

        // Token이 없을 경우 오류 처리
        if(accessToken == null) {
            throw new RuntimeException("Access Token Null");
        }

        // 토큰 검증 후, User Id 반환
        var userId = tokenBusiness.validationAccessToken(accessToken);

        // User Id가 있으면, RequestContext에 User Id 저장
        // RequestContext : 요청에 대한 정보를 저장하고 관리하는 컨테이너 역할
        if(userId != null) {
            var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            // SCOPE_REQUEST에 의해 해당 요청이 끝날 때까지  userId 속성 유지
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        // Token 검증 실패
        throw new RuntimeException("인증 실패");
    }
}

