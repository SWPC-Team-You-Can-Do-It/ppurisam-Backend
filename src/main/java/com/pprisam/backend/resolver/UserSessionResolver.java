package com.pprisam.backend.resolver;

import com.pprisam.backend.config.annotation.UserSession;
import com.pprisam.backend.domain.user.model.User;
import com.pprisam.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) { // 인터셉터에 이어서 뒤에 있는 컨트롤러가 어노테이션이 있는지 체크 역할
        // 지원하는 파라미터 체크, 어노테이션 체크
        // 1. 어노테이션이 있는지 체크
        var annotation = parameter.hasParameterAnnotation(UserSession.class); // UserSession 어노테이션이 있는지
        // 2. 파라미터의 타입 체크
        var parameterType=parameter.getParameterType().equals(User.class); // 파라미터 타입이 User 인지

        return (annotation && parameterType); // 조건에 부합하는지 참/거짓 반환
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // supportsParameter 메서드가 true 반환 시 실행

        // RequestContext 로부터 인터셉터에서 저장한 userId 가져오기
        var requestContext=RequestContextHolder.getRequestAttributes();
        var userId=requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        log.info("userId 체크 {}", userId);

        // userId를 통해 entity 가져오기
        var userEntity=userService.getUserWithThrow(Long.parseLong(userId.toString()));

        return User.builder() // 파라미터로 반환해줄 User 객체 생성
                // entity를 보내는 것은 지양
                // 1. 엔티티 객체는 @Entity와 @Table 어노테이션의 많은 정보로 인해 무거움
                // 2. 양방향 연관관계 매핑으로 인한 무한 참조가 발생할 수 있음
                // 3. 데이터베이스와 직접적으로 매핑되는 객체라 보안에 문제
                // 4. password와 같은 민감한 정보를 숨기기 위해서
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .status(userEntity.getStatus())
                .inactiveAt(userEntity.getInactiveAt())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build()
                ;
    }

}
