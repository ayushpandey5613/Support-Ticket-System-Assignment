#!/usr/bin/env bash
# Start API with postgres profile (requires docker compose postgres).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if ! docker compose ps --status running 2>/dev/null | grep -q postgres; then
  echo "Postgres container not running. Start it first:"
  echo "  docker compose up -d"
  exit 1
fi

export DB_URL="${DB_URL:-jdbc:postgresql://localhost:5432/tickets}"
export DB_USER="${DB_USER:-ticket}"
export DB_PASSWORD="${DB_PASSWORD:-ticket}"

echo "Using DB_URL=$DB_URL"
cd backend
SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run
