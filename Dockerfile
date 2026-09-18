FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY . .

RUN chmod +x mvnw \
    && ./mvnw clean package -DskipTests \
    && cp target/*.jar app.jar


FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]