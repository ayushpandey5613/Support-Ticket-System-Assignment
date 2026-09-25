# Requirements — Support Ticket Management System

## Overview

Web application for creating and managing support tickets with comments, search, filters, and a strict ticket status lifecycle enforced by the backend.

## Functional requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-01 | Create a ticket (title, description, priority; default status OPEN) | Must |
| FR-02 | List tickets (paginated) | Must |
| FR-03 | View ticket details (including comments) | Must |
| FR-04 | Update title, description, priority, assignee | Must |
| FR-05 | Add comments to a ticket | Must |
| FR-06 | Search tickets by keyword (title and description) | Must |
| FR-07 | Filter tickets by status | Must |
| FR-08 | Change status only via allowed transitions (see state machine) | Must |
| FR-09 | Persist data in a database; data survives restart | Must |
| FR-10 | Backend validates input; reject invalid payloads | Must |
| FR-11 | UI displays meaningful errors from API | Must |

## Non-functional requirements

| ID | Requirement |
|----|-------------|
| NFR-01 | Java 21, Spring Boot, REST API |
| NFR-02 | PostgreSQL for prod-like; H2 for local dev/test |
| NFR-03 | React or Next.js frontend |
| NFR-04 | No secrets committed to repository |
| NFR-05 | Spec-driven development; specs updated before implementation (`spec/development-workflow.md`) |
| NFR-06 | Prompt history maintained (`.specstory/history/`, `docs/prompt-history.md`) |
| NFR-07 | AI assistance validated; mistakes recorded (`docs/ai-review-log.md`) |

## Out of scope (Phase 1+ MVP)

- Authentication / RBAC (assignee is a free-text or optional identifier)
- Email notifications
- File attachments on tickets
- Multi-tenant organizations

## Core acceptance criteria (checklist)

Full product (UI + backend). **Backend/API progress** is tracked in `spec/implementation-status.md` and `docs/reviews/assessment-readiness.md`.

### Backend / API progress (Phases 2–4)

- [x] Ticket created via API (`POST /api/v1/tickets`)
- [x] Tickets listed (paginated)
- [x] Ticket details viewed (with comments)
- [x] Ticket fields updated; assignee changed (`PATCH`)
- [x] Comments added (`POST .../comments`)
- [x] Search works (`q` on list)
- [x] Status filter works (`status` on list)
- [x] Valid status transitions work
- [x] Invalid transitions rejected by backend (`409`)
- [x] Backend validation works
- [x] State-machine integration tests pass
- [x] Data survives restart (H2 file dev; IT-08 Postgres + Docker)
- [x] No secrets committed

### End-to-end (requires frontend Phases 5–7)

- [x] Ticket created from UI (Phase 5 — `/tickets/new`)
- [x] Tickets listed (Phase 5 — `/`)
- [x] Ticket details viewed (`/tickets/[id]`)
- [x] Ticket fields updated; assignee changed
- [x] Comments added
- [x] Search works (list UI)
- [x] Status filter works (list UI)
- [x] Valid status transitions work (detail status actions)
- [x] Invalid transitions rejected by backend (409 + `INVALID_STATUS_TRANSITION` in UI)
- [x] Data survives application restart (IT-08 / H2 file manual)
- [x] Backend validation works (UI shows API field errors on create/edit)
- [x] UI shows meaningful errors (API message + code on detail)
- [x] State-machine integration tests pass (`mvn test`)
- [x] No secrets committed

## Traceability

| FR | Spec sections |
|----|----------------|
| FR-01–08 | `api-contract.md`, `data-model.md`, `state-machine.md`, `ui-flow.md` |
| FR-09 | `architecture.md`, `data-model.md`, `test-strategy.md` (IT-08) |
| FR-10–11 | `api-contract.md`, `rules/api-standards.md`, `ui-flow.md`, `test-strategy.md` |
| NFR-05–07 | `development-workflow.md`, `docs/prompt-history.md`, `docs/ai-review-log.md` |

## Phased implementation

Delivery is incremental per `spec/architecture.md`. **Built vs planned** is tracked in `spec/implementation-status.md` (not all FRs are complete until later phases).
