FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY . .

RUN chmod 777 ./gradlew && ./gradlew build -x test --parallel
RUN mkdir -p build/extracted && (java -Djarmode=tools -jar build/libs/flownews.jar extract --layers --launcher --destination build/extracted)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

ARG EXTRACTED=/workspace/app/build/extracted

RUN apk update
RUN apk --no-cache add curl
RUN apk --no-cache add tzdata

ENV TZ=Asia/Seoul
RUN cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

ENV SERVER_PORT=80

COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]