# Stage 1: Frontend Build
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Backend Build
FROM maven:3.9.6-eclipse-temurin-21 AS backend-builder
WORKDIR /app
# 1. Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B
# 2. Copy the built frontend from the previous stage
COPY --from=frontend-builder /app/frontend/dist ./frontend/dist
# 3. Copy backend source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:21-jre-jammy
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=backend-builder /app/target/*.jar app.jar
ENV PORT=8080
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/ping || exit 1
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
