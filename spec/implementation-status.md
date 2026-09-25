# Implementation Status (normative for phased delivery)

Tracks **requirements vs spec vs code**. AI/reviewers: incomplete rows are **planned later phases**, not spec drift.

Last updated: Phase 8 complete (2026-09-25).

## Workflow artefacts (assignment)

| Artefact | Path | Status |
|----------|------|--------|
| Java / Spring guidelines | `rules/java-springboot.md` | Present |
| Testing guidelines | `rules/testing.md` | Present |
| API standards | `rules/api-standards.md` | Present |
| Documentation skill | `skills/documentation/` | Present |
| Review commands | `commands/review-code.md`, `review-spec.md`, `generate-tests.md` | Present |
| Spec set | `spec/*.md` | Present |
| SDD workflow | `spec/development-workflow.md` | Present |
| Prompt history | `docs/prompt-history.md`, `.specstory/history/` | Present (transcripts optional via SpecStory) |
| AI review log | `docs/ai-review-log.md` | Present |
| Token / context notes | `docs/ai-tooling.md` | Present |

## Functional requirements

| ID | Requirement | Phase | Backend | Frontend | Tests |
|----|-------------|-------|---------|----------|-------|
| FR-01 | Create ticket | 2 | Done | 5 | Done (UI create) / IT-01 |
| FR-02 | List (paginated) | 2 | Done (`page`, `size`) | 5 | Done (UI list) / IT-01 |
| FR-03 | View details + comments | 4 | Done | 6 | Done (UI) / IT-03 |
| FR-04 | Update fields | 2 | Done (PATCH) | 6 | Done (UI) / IT-02 |
| FR-05 | Comments | 4 | Done | 6 | Done (UI) / IT-03 |
| FR-06 | Search `q` | 4 | Done | 6 | Done (UI) / IT-04 |
| FR-07 | Filter `status` | 4 | Done | 6 | Done (UI) / IT-04 |
| FR-08 | Status transitions | 3 | Done | 7 | Done (UI) / IT-05, IT-06 |
| FR-09 | Persist / restart | 2/8 | H2 file dev (2); Postgres (8) | — | IT-08 (Postgres + Docker) |
| FR-10 | Backend validation | 2 | Done | — | IT-07 |
| FR-11 | UI errors | 5–7 | API shape ready | Done (detail status 409 + codes) | Manual / E2E |

## API contract vs backend (v1)

| Endpoint | Spec | Implemented |
|----------|------|-------------|
| `POST /api/v1/tickets` | Yes | Phase 2 |
| `GET /api/v1/tickets` | Yes | Phase 4 (`q`, `status` AND) |
| `GET /api/v1/tickets/{id}` | Yes | Phase 2 |
| `PATCH /api/v1/tickets/{id}` | Yes | Phase 2 (rejects `status`) |
| `POST /api/v1/tickets/{id}/status` | Yes | Phase 3 (done) |
| `POST /api/v1/tickets/{id}/comments` | Yes | Phase 4 (done) |

## State machine

Enforcement: **done** (`TicketStatusTransition` + `POST .../status`). Spec: `spec/state-machine.md`.

## Acceptance criteria checklist

- **Backend (Phases 2–4):** see checked items in `spec/requirements.md` § Backend / API progress.  
- **Full product:** UI Phases 5–7 + Postgres Phase 8 — see `docs/reviews/assessment-readiness.md`.
