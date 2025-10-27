# 繁體中文註解：後端使用多階段建置，區分開發熱部署與正式包裝流程。
FROM maven:3.9.6-eclipse-temurin-21 AS base
WORKDIR /workspace
COPY pom.xml .
# 繁體中文註解：預先下載依賴，加快容器啟動速度。
RUN mvn -B dependency:go-offline

# 繁體中文註解：開發階段直接用 Maven 啟動 Spring Boot，支援 DevTools 熱重啟。
FROM base AS development
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]

# 繁體中文註解：正式環境透過 Maven 建置可執行 JAR。
FROM base AS build
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
