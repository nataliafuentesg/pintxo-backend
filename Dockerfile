# Build
FROM gradle:8.7-jdk17 AS build
WORKDIR /src
COPY . .
RUN gradle clean bootJar -x test

# Run (JRE ligera)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -XX:MaxRAMPercentage=60 -XX:InitialRAMPercentage=15 -XX:+ExitOnOutOfMemoryError"
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java","-jar","/app/app.jar"]
