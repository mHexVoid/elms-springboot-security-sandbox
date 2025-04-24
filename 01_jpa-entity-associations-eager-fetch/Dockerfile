FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy everything into container
COPY . .

# Install Maven
RUN apk add --no-cache maven

# Run Spring Boot app (using plugin)

CMD ["mvn", "spring-boot:run"]
