#!/usr/bin/env bash
# Stop local Support Ticket System dev servers (stale backend/frontend).
# Run from repo root: ./scripts/stop-dev.sh

set -euo pipefail

echo "Stopping Spring Boot (ticket-system)…"
pkill -f 'com.ticketsystem.TicketSystemApplication' 2>/dev/null || true
pkill -f 'ticket-system.*spring-boot:run' 2>/dev/null || true
pkill -f 'TIcket System/backend.*spring-boot:run' 2>/dev/null || true
pkill -f 'Ticket_System/backend.*spring-boot:run' 2>/dev/null || true

sleep 1

echo "Freeing ports 8080 and 3000–3002 (old Next.js instances)…"
for port in 8080 3000 3001 3002; do
  if command -v fuser >/dev/null 2>&1; then
    fuser -k "${port}/tcp" 2>/dev/null || true
  else
    pids=$(lsof -ti "tcp:${port}" -sTCP:LISTEN 2>/dev/null || true)
    if [ -n "${pids}" ]; then
      kill ${pids} 2>/dev/null || true
    fi
  fi
done

echo "Done. Start fresh:"
echo "  cd backend && mvn spring-boot:run"
echo "  cd frontend && npm run dev   # always http://localhost:3000"
