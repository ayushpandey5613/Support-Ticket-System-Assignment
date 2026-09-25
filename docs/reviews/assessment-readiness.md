# Assessment readiness — AI / human reviewer guide

This document maps the **original SE/SSE assignment** to this repository. Use with `spec/implementation-status.md`.

## Required workflow (assignment)

```text
Requirement → Specification → Plan/Tasks → Implementation → Testing → Review → Fix
```

Evidence: `spec/development-workflow.md`, phase notes in `docs/prompt-history.md`, reviews under `docs/reviews/`.

## Required steering artefacts

| Assignment ask | Repository path | Status |
|----------------|-----------------|--------|
| Java Spring Boot guidelines | `rules/java-springboot.md` | OK |
| Testing guidelines | `rules/testing.md` | OK |
| API standards | `rules/api-standards.md` | OK |
| Documentation skill | `skills/documentation/SKILL.md` | OK |
| Review code / spec / generate tests | `commands/review-code.md`, `review-spec.md`, `generate-tests.md` | OK |

## Required spec artefacts

All present under `spec/`: `requirements.md`, `architecture.md`, `data-model.md`, `api-contract.md`, `state-machine.md`, `ui-flow.md`, `test-strategy.md`, plus `development-workflow.md` and `implementation-status.md`.

## Prompt history

| Assignment ask | Location |
|----------------|----------|
| `.specstory/history/` | Present (SpecStory optional; folder ready) |
| `docs/prompt-history.md` | Maintained per phase |

## AI not blindly accepted

`docs/ai-review-log.md` — multiple real corrections across Phase 1–8.

## Token optimisation

`docs/ai-tooling.md` — scoped context, `@` spec files, optional MCP plugins.

## Application stack (assignment)

| Tech | Status |
|------|--------|
| Java 21 + Spring Boot REST | `backend/` — implemented |
| H2 dev / test | `application-dev.yml`, `application-test.yml` |
| PostgreSQL prod-like | `application-postgres.yml`, `docker-compose.yml`, IT-08 |
| Next.js frontend | `frontend/` — Phases 5–7 complete per `spec/ui-flow.md` |
| Cursor steering | `rules/`, `.cursor/rules/spec-driven.mdc` |

## Features vs acceptance criteria

| Criterion | Backend (API/tests) | UI |
|-----------|---------------------|-----|
| Create ticket | Done — `POST /tickets` | Done — `/tickets/new` |
| List tickets | Done — paginated | Done — `/` |
| View details | Done — includes comments | Done — `/tickets/[id]` |
| Update fields / assignee | Done — `PATCH` | Done — detail edit form |
| Comments | Done — `POST .../comments` | Done — detail comments |
| Search `q` | Done | Done — list search |
| Filter `status` | Done | Done — list filter |
| Valid / invalid transitions | Done — `TicketStatusTransition*` tests | Done — status buttons + 409 |
| Data survives restart | H2 file dev + IT-08 Postgres (Docker) | — |
| Backend validation | Done — IT-07 + comment validation | Done — `ErrorAlert` / inline errors |
| UI meaningful errors | API envelope | Done — message + error code |
| State-machine integration tests | Done — `TicketStatusTransitionIntegrationTest` | — |
| No secrets in repo | `.gitignore`, dev/test only | OK |

## API contract compliance (backend)

All v1 endpoints in `spec/api-contract.md` are implemented in `TicketController` (Phases 2–4). Run: `cd backend && mvn test`.

## Reviewer quick start

1. Read `spec/implementation-status.md` and `spec/requirements.md` (checklists).  
2. `cd backend && mvn test`  
3. `./scripts/stop-dev.sh` then `cd backend && mvn spring-boot:run` and `cd frontend && npm run dev` → `http://localhost:3000`  
4. Optional Postgres: `docker compose up -d` and `./scripts/start-postgres-backend.sh`

## Cross-check documents

- `docs/reviews/phase-2-cross-check.md` (Phase 2 slice)  
- This file (holistic, final pass **2026-09-25** after Phase 8)
