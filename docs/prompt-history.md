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
- Next phase: **Phase 2** — Spring Boot module in `backend/`, H2, ticket CRUD + validation per `api-contract.md` (status transitions in Phase 3)

---

## 2026-09-25 — Pre-push spec review (AI-assisted)

**User prompt (summary):** Review Phase 1 thoroughly so AI assignment review does not fail later.

**Outcome:** Spec consistency fixes and assignment workflow artefact `spec/development-workflow.md`. See `docs/ai-review-log.md` for rejected/conflicting suggestions.
