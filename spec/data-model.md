# Data Model

## Entities

### Ticket

| Field | Type | Constraints |
|-------|------|-------------|
| id | UUID | PK, generated |
| title | string | not blank, max 200 |
| description | string | not blank, max 5000 |
| priority | enum | LOW, MEDIUM, HIGH, URGENT |
| status | enum | OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED |
| assignee | string | optional, max 120 (display name or email) |
| createdAt | timestamp | UTC, immutable |
| updatedAt | timestamp | UTC |

**Defaults on create:** `status = OPEN`, `priority = MEDIUM` if omitted.

### Comment

| Field | Type | Constraints |
|-------|------|-------------|
| id | UUID | PK |
| ticketId | UUID | FK → Ticket, not null |
| author | string | not blank, max 120 |
| body | string | not blank, max 2000 |
| createdAt | timestamp | UTC, immutable |

## Relationships

- Ticket 1 — * Comment (cascade delete comments when ticket deleted — optional; default: prevent delete if comments exist OR soft-delete; **MVP: cascade delete** for simplicity).

## Indexes (PostgreSQL / H2)

- `ticket(status)`
- `ticket(created_at DESC)` for listing
- Full-text or `LIKE` search on title + description for MVP (`q` parameter)

## Enums

### TicketStatus

`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`

### Priority

`LOW`, `MEDIUM`, `HIGH`, `URGENT`

## State vs stored fields

- `status` is the only field governed by the state machine (`spec/state-machine.md`).
- **Field updates (PATCH):** title, description, priority, and assignee MAY be updated in any status, including `CLOSED` and `CANCELLED`.
- **Comments:** MAY be added in any status.
- **Status changes:** ONLY via `POST /tickets/{id}/status` and ONLY for allowed transitions.
