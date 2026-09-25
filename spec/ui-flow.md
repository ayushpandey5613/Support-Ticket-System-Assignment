# UI Flow

## Global

- Environment: `NEXT_PUBLIC_API_URL` (default `http://localhost:8080/api/v1`)
- On API error: show `message` and field-level `fieldErrors` when present
- Loading and empty states on list and detail

## Screens

### 1. Ticket list (`/`)

- Table or cards: title, status, priority, assignee, updatedAt
- **Search:** input debounced → `GET /tickets?q=...`
- **Filter:** status dropdown → `GET /tickets?status=...`
- Pagination controls → `page`, `size`
- CTA: "New ticket" → create screen

### 2. Create ticket (`/tickets/new`)

- Form: title, description, priority (select), assignee (optional)
- Submit → `POST /tickets` → redirect to detail on success
- Show validation errors inline from API

### 3. Ticket detail (`/tickets/[id]`)

- Show all ticket fields
- **Edit mode** (or inline): PATCH title, description, priority, assignee
- **Status actions:** buttons only for legal next states from current status (UX hint; backend still authoritative)
  - Example OPEN: "Start progress" → IN_PROGRESS, "Cancel" → CANCELLED
- **Comments:** list + form (author, body) → POST comment → refresh
- Invalid transition: show API `message` (toast or alert)

## Status action map (UI helper)

| Current | Actions shown |
|---------|----------------|
| OPEN | → IN_PROGRESS, → CANCELLED |
| IN_PROGRESS | → RESOLVED, → CANCELLED |
| RESOLVED | → CLOSED |
| CLOSED | (none) |
| CANCELLED | (none) |

## Accessibility (should)

- Labels on inputs, focus on first error after failed submit

## Phase mapping

| Phase | UI |
|-------|-----|
| 5 | List + create |
| 6 | Detail, edit, comments, search, filter |
| 7 | Status buttons + error polish |
