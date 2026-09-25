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
| [docs/reviews/phase-2-cross-check.md](docs/reviews/phase-2-cross-check.md) | Latest spec-vs-code review |

## Repo layout

- `backend/` — Spring Boot API (phased implementation)
- `frontend/` — Next.js UI
- `spec/` — normative specifications
- `rules/`, `commands/`, `skills/` — reusable AI / team instructions
- `docs/prompt-history.md` — prompt index; `.specstory/history/` — raw chat exports

## Local run — backend (Phase 2)

```bash
cd backend
mvn spring-boot:run
```

API base: `http://localhost:8080/api/v1`  
H2 file DB: `backend/data/` (dev profile, survives restarts)

Tests: `cd backend && mvn test`

## Status

- **Phase 1:** specifications and steering (complete)
- **Phase 2:** backend ticket CRUD + validation + H2 (complete)
- **Phase 3:** status state machine + integration tests (complete)
- **Phase 4+:** comments/search, frontend, Postgres
