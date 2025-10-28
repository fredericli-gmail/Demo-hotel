# 繁體中文註解：前端使用 Node 建置靜態檔案，再交由 Nginx 提供服務與 API 反向代理。
FROM node:20-alpine AS base
WORKDIR /app
COPY package.json package-lock.json ./
ARG TARGETARCH
RUN npm install && \
    case "$TARGETARCH" in \
      amd64) npm install @rollup/rollup-linux-x64-musl --no-save ;; \
      arm64) npm install @rollup/rollup-linux-arm64-musl --no-save ;; \
      *) echo "不支援的建置架構：$TARGETARCH" && exit 1 ;; \
    esac
COPY . .

# 繁體中文註解：開發階段直接啟動 Vite Dev Server，支援容器內熱重載。
FROM base AS development
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "5173"]

# 繁體中文註解：正式環境仍由 Node 進行打包，並交由 Nginx 提供靜態檔案。
FROM base AS build
RUN npm run build

FROM nginx:1.25-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html/demohotel
EXPOSE 80
