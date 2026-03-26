# StyleMatch Railway Deployment Guide

This guide will help you deploy the StyleMatch application to Railway.

## 1. Provision PostgreSQL

1. Log in to your [Railway Dashboard](https://railway.app/).
2. Click **"New Project"** -> **"Provision PostgreSQL"**.
3. Once provisioned, Railway will automatically set environment variables like `DATABASE_URL`, `PGUSER`, `PGPASSWORD`, etc.

## 2. Deploy the Backend

1. Click **"New Service"** -> **"GitHub Repo"** -> Select your **StyleMatch** repository.
2. Railway should automatically detect the `Dockerfile` in the root and start building it.
3. Go to the **Variables** tab for the backend service and add:
   - `SPRING_DATASOURCE_URL`: `jdbc:${{Postgres.DATABASE_URL}}` (This is the most reliable way)
   - Alternatively, ensure `DATABASE_USER` is `postgres` and `DATABASE_NAME` is `railway` (or whatever your Postgres service shows).
   - `SPRING_LIQUIBASE_ENABLED`: `true`
4. Wait for the deployment to finish. Note the **Public/Private URL** of this service (e.g., `https://backend-production.up.railway.app`).

## 3. Deploy the Frontend

1. Click **"New Service"** again -> **"GitHub Repo"** -> Select the same **StyleMatch** repository.
2. Go to **Settings** -> **General** -> **Root Directory** and set it to `/frontend`. This tells Railway to use the `frontend/Dockerfile`.
3. Go to the **Variables** tab for the frontend service and add:
   - `BACKEND_URL`: The URL of your backend service (e.g., `https://backend-production.up.railway.app`).
   - `VITE_API_URL`: `/api`
4. Wait for the deployment to finish.

## 4. Final Verification

1. Open the URL assigned to your **frontend** service.
2. Log in and verify that you can see your results and navigate the app.
3. Check the **Logs** in Railway if you encounter any errors.
