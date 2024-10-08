package com.pprisam.backend.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER}) // 어노테이션을 어디에 붙일지? -> 파라미터
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 유지 기간 -> 런타임 시간
public @interface UserSession {
}
