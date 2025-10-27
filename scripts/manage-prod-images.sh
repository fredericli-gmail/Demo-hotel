#!/usr/bin/env bash
set -euo pipefail

# 繁體中文註解：此腳本協助建置、匯出與部署正式環境的容器映像。
# 使用方法：
#   ./scripts/manage-prod-images.sh export [輸出檔案]
#   ./scripts/manage-prod-images.sh deploy [輸入檔案]
# 如未指定檔案路徑，預設會放在專案根目錄下的 dist/demo-hotel-images.tar。

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BACKEND_IMAGE="${BACKEND_IMAGE:-demo-hotel-backend:prod}"
FRONTEND_IMAGE="${FRONTEND_IMAGE:-demo-hotel-frontend:prod}"
EXPORT_DIR="${EXPORT_DIR:-$PROJECT_ROOT/dist}"
ARCH="${ARCH:-linux/amd64}"
TAR_NAME="${TAR_NAME:-demo-hotel-images.tar}"
COMPOSE_FILES=(-f "$PROJECT_ROOT/docker-compose.yml" -f "$PROJECT_ROOT/docker-compose-prod.yml")

usage() {
  cat <<USAGE
用法：$(basename "$0") <export|deploy> [檔案路徑]

  export  建置 Linux x64 映像並匯出為 tar 檔，可選擇輸出路徑。
  deploy  載入 tar 檔後執行 docker compose up -d，如未指定檔案則讀取預設路徑。

環境變數：
  BACKEND_IMAGE     後端映像標籤（預設 demo-hotel-backend:prod）
  FRONTEND_IMAGE    前端映像標籤（預設 demo-hotel-frontend:prod）
  ARCH              建置平台架構（預設 linux/amd64）
  EXPORT_DIR        匯出目錄（預設 <專案>/dist）
  TAR_NAME          匯出檔名（預設 demo-hotel-images.tar）
  DB_DATA_PATH      PostgreSQL 掛載路徑，deploy 時會自動建立
USAGE
}

ensure_db_path() {
  local configured_path
  configured_path="${DB_DATA_PATH:-./prod-db-data}"

  if [[ "$configured_path" = /* ]]; then
    mkdir -p "$configured_path"
  else
    mkdir -p "$PROJECT_ROOT/$configured_path"
  fi
}

build_images() {
  docker build --platform "$ARCH" -t "$BACKEND_IMAGE" "$PROJECT_ROOT/backend"
  docker build --platform "$ARCH" -t "$FRONTEND_IMAGE" "$PROJECT_ROOT/frontend"
}

export_images() {
  local output_path
  output_path="${1:-$EXPORT_DIR/$TAR_NAME}"

  mkdir -p "$(dirname "$output_path")"
  build_images
  docker save "$BACKEND_IMAGE" "$FRONTEND_IMAGE" > "$output_path"
  echo "已匯出映像至 $output_path"
}

deploy_images() {
  local input_path
  input_path="${1:-$EXPORT_DIR/$TAR_NAME}"

  if [[ ! -f "$input_path" ]]; then
    echo "找不到映像檔：$input_path" >&2
    exit 1
  fi

  docker load -i "$input_path"
  ensure_db_path
  (cd "$PROJECT_ROOT" && DB_DATA_PATH="${DB_DATA_PATH:-./prod-db-data}" docker compose "${COMPOSE_FILES[@]}" up -d)
}

main() {
  if [[ $# -lt 1 ]]; then
    usage
    exit 1
  fi

  case "$1" in
    export)
      export_images "${2:-}"
      ;;
    deploy)
      deploy_images "${2:-}"
      ;;
    -h|--help)
      usage
      ;;
    *)
      usage
      exit 1
      ;;
  esac
}

main "$@"
