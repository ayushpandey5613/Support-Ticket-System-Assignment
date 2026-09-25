# Support Ticket Management System

SE/SSE assignment — **spec-driven** monorepo (Java 21 + Spring Boot + Next.js).

## Start here

| Document | Purpose |
|----------|---------|
| [spec/requirements.md](spec/requirements.md) | Functional requirements & acceptance criteria |
| [spec/development-workflow.md](spec/development-workflow.md) | Requirement → Spec → Plan → Build → Test → Review |
| [spec/implementation-status.md](spec/implementation-status.md) | **What is built per phase (reviewers start here)** |
| [spec/api-contract.md](spec/api-contract.md) | REST API v1 |
| [spec/state-machine.md](spec/state-machine.md) | Ticket status transitions (backend-enforced) |
| [docs/reviews/assessment-readiness.md](docs/reviews/assessment-readiness.md) | **Assignment ↔ repo map (AI reviewers)** |
| [docs/ai-review-log.md](docs/ai-review-log.md) | AI suggestions rejected / corrected |

## Repo layout

- `backend/` — Spring Boot API (phased implementation)
- `frontend/` — Next.js UI
- `spec/` — normative specifications
- `rules/`, `commands/`, `skills/` — reusable AI / team instructions
- `docs/prompt-history.md` — prompt index; `.specstory/history/` — raw chat exports

## Daily dev workflow (port 8080 / 3000 busy?)

Har baar naya terminal khulne se **purana backend/frontend chalta reh jata hai** — isliye port badalta hai ya “already in use” aata hai.

**Pehle sab band karo (repo root):**

```bash
chmod +x scripts/stop-dev.sh   # once
./scripts/stop-dev.sh
```

Phir **sirf ek** backend aur **sirf ek** frontend:

```bash
# Terminal 1
cd backend && mvn spring-boot:run

# Terminal 2
cd frontend && npm run dev
```

- Frontend ab hamesha **3000** pe try karega (`next dev -p 3000`). Agar busy ho to `stop-dev.sh` chalao, dubara `npm run dev`.
- Backend hamesha **8080** — doosra `mvn spring-boot:run` mat chalao (Cursor background task + tumhara terminal = conflict).
- Browser: `http://localhost:3000` (3001/3002 mat kholo jab tak zarurat na ho).

## Local run — backend (Phase 2)

```bash
cd backend
mvn spring-boot:run
```

API base: `http://localhost:8080/api/v1`  
H2 file DB: `backend/data/` (dev profile, survives restarts)

Tests: `cd backend && mvn test`

## Local run — frontend (Phase 5)

```bash
cd backend && mvn spring-boot:run
```

In another terminal:

```bash
cd frontend
cp .env.example .env.local   # optional; default API URL is localhost:8080
npm install
npm run dev
```

Open `http://localhost:3000`

### Backend won't start (Hibernate / JDBC / Dialect error)

Usually **H2 file locked** by another Java process, or **`postgres` profile** without `DB_URL`.

```bash
./scripts/stop-dev.sh
cd backend
mvn spring-boot:run
```

Wait for: `Started TicketSystemApplication`.  
Do **not** set `SPRING_PROFILES_ACTIVE=postgres` until Phase 8 env vars exist.

If lock persists, stop apps and remove stale lock (data file stays):

```bash
rm -f backend/data/ticketdb.lock.db
```

### UI stuck on "Creating…" or "Failed to fetch" / CORS

1. Backend must be **started** (see above).
2. Test: `curl -X POST http://localhost:8080/api/v1/tickets -H 'Content-Type: application/json' -d '{"title":"T","description":"D"}'` — under ~1s.
3. Frontend: `NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1` in `frontend/.env.local`.
4. **CORS:** Dev profile allows any `http://localhost:*` / `127.0.0.1:*` (so port 3002 is fine). **Restart** the backend after CORS changes. Prefer a single `npm run dev` on port **3000** (stop extra Next processes if ports 3001/3002 were picked).

## Status

- **Phase 1:** specifications and steering (complete)
- **Phase 2:** backend ticket CRUD + validation + H2 (complete)
- **Phase 3:** status state machine + integration tests (complete)
- **Phase 4:** comments, search & status filter APIs (complete)
- **Phase 5:** Next.js list + create (complete)
- **Phase 6:** detail, edit, comments, search/filter UI (complete)
- **Phase 7:** status actions UI + 409 errors (complete)
- **Phase 8:** Postgres
