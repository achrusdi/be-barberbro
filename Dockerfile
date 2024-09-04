# Gunakan image resmi Maven untuk build tahap pertama
FROM maven:3.8.5-openjdk-17 AS build

# Set work directory di dalam container
WORKDIR /app

# Salin file pom.xml dan unduh dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Salin seluruh source code ke dalam container
COPY src ./src

# Build aplikasi Spring Boot
RUN mvn clean package -DskipTests

# Gunakan image OpenJDK untuk runtime tahap kedua
FROM openjdk:17-jdk-slim

# Set work directory di dalam container
WORKDIR /app

# Salin hasil build dari tahap pertama
COPY --from=build /app/target/*.jar app.jar

# Salin keystore untuk HTTPS
COPY src/main/resources/keystore.p12 keystore.p12

# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.ssl.key-store=/app/keystore.p12", "--server.ssl.key-store-password=barberbro", "--server.ssl.key-alias=my-ssl-cert"]
