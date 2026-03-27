# Stage 1: Build
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# Set build-time variables
ARG VITE_API_URL=/api
ENV VITE_API_URL=$VITE_API_URL
RUN npm run build

# Stage 2: Serve
FROM nginx:stable-alpine
# Copy SPA files
COPY --from=build /app/dist /usr/share/nginx/html
# Copy template config to the official templates directory
COPY default.conf.template /etc/nginx/templates/default.conf.template

# Default env vars (will be overridden by Railway)
ENV PORT=80
ENV BACKEND_URL=http://backend:8080

EXPOSE 80

# The official Nginx image has a built-in entrypoint that runs envsubst 
# on any .template files in /etc/nginx/templates/ and outputs them to /etc/nginx/conf.d/
