# 단계 1: 빌드 단계
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradle Wrapper 스크립트 복사
COPY gradlew .
COPY gradle ./gradle

# Gradle 설정 파일 복사
COPY build.gradle settings.gradle ./

# 소스 코드 복사
COPY src ./src

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# 애플리케이션 빌드 (테스트 스킵)
RUN ./gradlew clean build -x test --no-daemon

# 단계 2: 실행 단계
FROM eclipse-temurin:21-jdk

WORKDIR /app

# FFmpeg 및 tzdata 설치
RUN apt-get update && \
    apt-get install -y ffmpeg tzdata && \
    # 시간대를 UTC로 설정
    ln -fs /usr/share/zoneinfo/UTC /etc/localtime && \
    echo "UTC" > /etc/timezone && \
    dpkg-reconfigure --frontend noninteractive tzdata && \
    rm -rf /var/lib/apt/lists/*

# 시간대 환경 변수 설정 (선택 사항)
ENV TZ=UTC

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 개방
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
