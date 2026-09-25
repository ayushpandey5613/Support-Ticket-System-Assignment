# Support Ticket Management System

SE/SSE assignment — **spec-driven** monorepo (Java 21 + Spring Boot + Next.js).

## Start here

| Document | Purpose |
|----------|---------|
| [spec/requirements.md](spec/requirements.md) | Functional requirements & acceptance criteria |
| [spec/development-workflow.md](spec/development-workflow.md) | Requirement → Spec → Plan → Build → Test → Review |
| [spec/api-contract.md](spec/api-contract.md) | REST API v1 |
| [spec/state-machine.md](spec/state-machine.md) | Ticket status transitions (backend-enforced) |

## Repo layout

- `backend/` — Spring Boot API (phased implementation)
- `frontend/` — Next.js UI
- `spec/` — normative specifications
- `rules/`, `commands/`, `skills/` — reusable AI / team instructions
- `docs/prompt-history.md` — prompt index; `.specstory/history/` — raw chat exports

## Local run (after Phase 2+)

Documented in backend/frontend README sections as they are added.

## Status

- **Phase 1:** specifications and steering (complete)
- **Phase 2+:** implementation in progress
