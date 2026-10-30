FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test
RUN find build/libs -type f -name "*.jar" ! -name "*-plain.jar" -exec cp {} app.jar \;

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/app.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]