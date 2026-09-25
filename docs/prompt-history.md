# Prompt History

Normative specs live in `spec/`. This file is a human-readable index; raw transcripts go under `.specstory/history/` (SpecStory or manual copies).

## How to record

For each meaningful AI session:

1. Date (UTC or local)
2. Goal / phase
3. Prompt (paste or summarize)
4. Outcome (files changed, decisions)
5. Corrections (what you rejected from AI) — also log in `ai-review-log.md`

---

## 2026-09-25 — Phase 1: Hygiene + specifications

**Goal:** Set up rules, commands, skills, and full spec skeleton before backend/frontend code.

**User prompts (summary)**

1. Shared SE/SSE assignment requirements (Support Ticket System, spec-driven workflow, monorepo, stepwise phases).
2. Asked how to segregate frontend/backend and approach stepwise (not one big commit).
3. `"Phase 1 start karo"` / `ok start kro`

**Outcome**

- Created `rules/`, `commands/`, `skills/documentation/`, `spec/*`, `docs/prompt-history.md`, `docs/ai-review-log.md`, `.specstory/history/`, `.gitignore`, `.cursor/rules/`.

**Decisions**

- Monorepo: `backend/` + `frontend/` (folders created in Phase 2/5).
- Next.js + Maven Spring Boot (documented in `architecture.md`).
- Assignee and comment author as strings (no auth in MVP).
- Status changes via `POST /tickets/{id}/status` separate from PATCH.

---

### Phase 1 complete — 2026-09-25

- Spec files touched: all under `spec/`
- Pre-push: ran internal spec review; fixed UUID/ID drift, PATCH vs status endpoint, error envelope, data-model contradictions, added `development-workflow.md`, `README.md`, `docs/ai-tooling.md`
- Next phase: **Phase 3** — status state machine + integration tests

---

## 2026-09-25 — Phase 2: Backend CRUD

**Prompt:** Phase 2 start — Spring Boot CRUD per spec (no status endpoint yet).

**Outcome:** `backend/` Spring Boot 3.4, Java 21, JPA Ticket entity, POST/GET list/GET detail/PATCH, validation + error envelope, H2 file dev profile, MockMvc tests (IT-01, IT-02, IT-07).

**Branch:** `cursor/phase-2-backend-crud`

### Phase 2 complete — 2026-09-25

- Tests: `mvn test` in `backend/`
- Cross-check: `docs/reviews/phase-2-cross-check.md`, `spec/implementation-status.md`
- Next: **Phase 3** — `POST /tickets/{id}/status` + `TicketStatusTransitionIntegrationTest`

---

## 2026-09-25 — Phase 2 spec cross-check (pre-AI review)

**User prompt:** Cross-check everything for AI review and spec-driven compliance.

**Outcome:** Traceability doc, phase-2 review log, pom fix, extra integration tests, architecture status column.

---

## 2026-09-25 — Phase 3: Status state machine

**Prompt:** Ok ab next phase start kro (Phase 3).

**Outcome:** `POST /api/v1/tickets/{id}/status`, `TicketStatusTransition` component, `409 INVALID_STATUS_TRANSITION` with `from`/`to`, unit + `TicketStatusTransitionIntegrationTest`.

### Phase 3 complete — 2026-09-25

- Tests: `mvn test` (includes state-machine suite)
- Next: **Phase 5** — Next.js list + create

---

## 2026-09-25 — Holistic assessment cross-check (pre-AI review)

**User prompt:** Full cross-check for AI review; must match assignment spec-driven requirements.

**Outcome:** `docs/reviews/assessment-readiness.md`, backend acceptance ticks in `requirements.md`, invalid query param handling, comment validation test, `application-postgres.yml` stub, `frontend/.gitkeep`.

---

## 2026-09-25 — Phase 4: Comments, search, filter

**Prompt:** Phase 4 start.

**Outcome:** `Comment` entity, `POST /tickets/{id}/comments`, GET detail with comments, list `q` + `status` (AND), `TicketCommentAndSearchIntegrationTest` (IT-03, IT-04).

### Phase 4 complete — 2026-09-25

- Tests: `mvn test`
- Next: **Phase 5** — frontend list + create

---

## 2026-09-25 — Pre-push spec review (AI-assisted)

**User prompt (summary):** Review Phase 1 thoroughly so AI assignment review does not fail later.

**Outcome:** Spec consistency fixes and assignment workflow artefact `spec/development-workflow.md`. See `docs/ai-review-log.md` for rejected/conflicting suggestions.
