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

`docs/ai-review-log.md` — multiple real corrections across Phase 1–4.

## Token optimisation

`docs/ai-tooling.md` — scoped context, `@` spec files, optional MCP plugins.

## Application stack (assignment)

| Tech | Status |
|------|--------|
| Java 21 + Spring Boot REST | `backend/` — implemented |
| H2 dev / test | `application-dev.yml`, `application-test.yml` |
| PostgreSQL prod-like | **Phase 8** — `application-postgres.yml` stub only |
| React/Next.js | **Phase 5** — list + create in `frontend/`; detail/search UI Phase 6 |
| Cursor / Copilot steering | `rules/`, `.cursor/rules/spec-driven.mdc` |

## Features vs acceptance criteria

| Criterion | Backend (API/tests) | UI (Phase 5–7) |
|-----------|---------------------|----------------|
| Create ticket | Done — `POST /tickets` | Done — `/tickets/new` |
| List tickets | Done — paginated | Done — `/` |
| View details | Done — includes comments | Pending |
| Update fields / assignee | Done — `PATCH` | Pending |
| Comments | Done — `POST .../comments` | Pending |
| Search `q` | Done | Pending |
| Filter `status` | Done | Pending |
| Valid / invalid transitions | Done — `TicketStatusTransition*` tests | Pending |
| Data survives restart | H2 **file** dev (manual/IT-08 Phase 8) | — |
| Backend validation | Done — IT-07 + comment validation | — |
| UI meaningful errors | API envelope ready | Pending |
| State-machine integration tests | Done — `TicketStatusTransitionIntegrationTest` | — |
| No secrets in repo | `.gitignore`, empty H2 passwords in dev/test only | OK |

## API contract compliance (backend)

All v1 endpoints in `spec/api-contract.md` are implemented in `TicketController` (Phases 2–4). Run: `cd backend && mvn test`.

## Intentional incomplete items (not bugs)

1. **Frontend** — phased; specs in `spec/ui-flow.md`.  
2. **PostgreSQL profile** — Phase 8.  
3. **`requirements.md` checklist `[ ]`** — end-to-end product; see **Backend progress** section added in that file.  
4. **Copilot** — same `rules/`/`spec/` as Cursor; IDE-specific config not required by rubric.

## Latest cross-checks

- `docs/reviews/phase-2-cross-check.md` (Phase 2 slice)  
- This file (holistic, updated after Phase 4)
