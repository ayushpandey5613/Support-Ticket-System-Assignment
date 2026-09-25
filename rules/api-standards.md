# API Standards

## Style

- REST over HTTP/JSON; UTF-8.
- Base path: `/api/v1`.
- Nouns for resources: `/tickets`, `/tickets/{id}/comments`.
- Use HTTP semantics:
  - `GET` read
  - `POST` create (and non-idempotent actions if needed)
  - `PATCH` partial update (title, description, priority, assignee)
  - Dedicated `POST` or `PATCH` for status transition (see contract)

## Request / response

- JSON field names: `camelCase`.
- Dates: ISO-8601 strings in UTC.
- Enums: string values matching Java enum names (`OPEN`, `IN_PROGRESS`, …).

## Pagination (list)

- Query: `page`, `size` (default size 20, max 100).
- Response wrapper: `{ "content": [], "page", "size", "totalElements" }` (or Spring `Page` serialized consistently).

## Search & filter

- `q` — keyword search (title + description).
- `status` — filter by a single ticket status (exact match); combinable with `q` (AND).

## Errors

Unified error body:

```json
{
  "timestamp": "2026-09-25T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "code": "VALIDATION_ERROR",
  "message": "Human-readable summary",
  "path": "/api/v1/tickets",
  "fieldErrors": [
    { "field": "title", "message": "must not be blank" }
  ]
}
```

Domain errors (e.g. invalid transition) use the same envelope with `status` 409:

```json
{
  "timestamp": "2026-09-25T12:00:00Z",
  "status": 409,
  "error": "Conflict",
  "code": "INVALID_STATUS_TRANSITION",
  "message": "Cannot transition from CLOSED to OPEN",
  "path": "/api/v1/tickets/{id}/status",
  "from": "CLOSED",
  "to": "OPEN"
}
```

HTTP status:

| Situation | Status |
|-----------|--------|
| Validation | 400 |
| Invalid transition | 409 Conflict |
| Not found | 404 |
| Success create | 201 |
| Success read/update | 200 |

## Versioning

- URL version `v1`; breaking changes require `v2` and spec update.

## CORS

- Dev: allow frontend origin from env `FRONTEND_ORIGIN` (default `http://localhost:3000`).

## Documentation

- Prefer springdoc OpenAPI generated from code; must stay aligned with `spec/api-contract.md`.
