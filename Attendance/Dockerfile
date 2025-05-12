# Run stage
FROM eclipse-temurin:17-jre-alpine

ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN mkdir -p /app/fonts
COPY src/main/resources/fonts/NanumGothic-Regular.ttf /app/fonts/


WORKDIR /app
COPY  build/libs/*.jar app.jar

EXPOSE 6060
ENTRYPOINT ["java", "-Xms256m", "-Xmx2048m", "-jar", "app.jar"]