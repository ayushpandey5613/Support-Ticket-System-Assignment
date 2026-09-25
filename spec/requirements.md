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

- [ ] Ticket created from UI
- [ ] Tickets listed
- [ ] Ticket details viewed
- [ ] Ticket fields updated; assignee changed
- [ ] Comments added
- [ ] Search works
- [ ] Status filter works
- [ ] Valid status transitions work
- [ ] Invalid transitions rejected by backend
- [ ] Data survives application restart
- [ ] Backend validation works
- [ ] UI shows meaningful errors
- [ ] State-machine integration tests pass
- [ ] No secrets committed

## Traceability

| FR | Spec sections |
|----|----------------|
| FR-01–08 | `api-contract.md`, `data-model.md`, `state-machine.md`, `ui-flow.md` |
| FR-09 | `architecture.md`, `data-model.md`, `test-strategy.md` (IT-08) |
| FR-10–11 | `api-contract.md`, `rules/api-standards.md`, `ui-flow.md`, `test-strategy.md` |
| NFR-05–07 | `development-workflow.md`, `docs/prompt-history.md`, `docs/ai-review-log.md` |
