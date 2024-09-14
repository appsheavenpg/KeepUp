FROM amazoncorretto:22 as builder

WORKDIR /app

COPY build/libs/server-all.jar /app/server-all.jar

FROM amazoncorretto:22 as runtime

COPY --from=builder /app/server-all.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-all.jar"]