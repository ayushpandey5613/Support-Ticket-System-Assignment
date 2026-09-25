# API Contract v1

Base URL: `/api/v1`  
Content-Type: `application/json`

> Status: **Approved for implementation (v1)** — breaking changes require v2 and spec review.

## Ticket resource

### Ticket JSON

```json
{
  "id": "uuid",
  "title": "string",
  "description": "string",
  "priority": "MEDIUM",
  "status": "OPEN",
  "assignee": "string | null",
  "createdAt": "2026-09-25T10:00:00Z",
  "updatedAt": "2026-09-25T10:00:00Z"
}
```

### POST `/tickets` — Create

**Request**

```json
{
  "title": "string",
  "description": "string",
  "priority": "MEDIUM",
  "assignee": "optional"
}
```

**Response:** `201` + Ticket (status `OPEN`)

**Errors:** `400` validation

---

### GET `/tickets` — List

**Query**

| Param | Description |
|-------|-------------|
| `page` | 0-based, default 0 |
| `size` | default 20 |
| `status` | optional; exact match on ticket status (e.g. `OPEN`) |
| `q` | optional; case-insensitive substring match on **title and description** |

When both `status` and `q` are present, results MUST match **both** (logical AND).

**Response:** `200`

```json
{
  "content": [ /* Ticket */ ],
  "page": 0,
  "size": 20,
  "totalElements": 42
}
```

---

### GET `/tickets/{id}` — Detail

**Response:** `200` Ticket + embedded comments:

```json
{
  "id": "uuid",
  "title": "...",
  "description": "...",
  "priority": "HIGH",
  "status": "OPEN",
  "assignee": null,
  "createdAt": "...",
  "updatedAt": "...",
  "comments": [
    {
      "id": "uuid",
      "author": "Ayush",
      "body": "...",
      "createdAt": "..."
    }
  ]
}
```

**Errors:** `404`

---

### PATCH `/tickets/{id}` — Update fields

Does **not** change status.

**Request** (all fields optional; at least one required). Send `assignee: null` to clear assignee.

```json
{
  "title": "string",
  "description": "string",
  "priority": "HIGH",
  "assignee": "name@example.com"
}
```

**Response:** `200` — Ticket JSON **without** the `comments` array (same shape as list items).

**Errors:** `400`, `404`

---

### POST `/tickets/{id}/status` — Transition status

**Request**

```json
{
  "status": "IN_PROGRESS"
}
```

**Response:** `200` Ticket

**Errors:**

- `404` not found
- `409` `INVALID_STATUS_TRANSITION` with `from` / `to`
- `400` if status missing or invalid enum

---

### POST `/tickets/{id}/comments` — Add comment

**Request**

```json
{
  "author": "string",
  "body": "string"
}
```

**Response:** `201`

```json
{
  "id": "uuid",
  "ticketId": "uuid",
  "author": "string",
  "body": "string",
  "createdAt": "2026-09-25T10:00:00Z"
}
```

**Errors:** `400`, `404`

---

## Error envelope

All error responses use the structure in `rules/api-standards.md`. Domain codes (e.g. `INVALID_STATUS_TRANSITION`) are returned with the appropriate HTTP status (`409`) and include `code`, `message`, and transition fields where applicable.

## CORS

`OPTIONS` + `GET,POST,PATCH` from configured frontend origin.

## Implementation phases (backend)

| Endpoint | Target phase |
|----------|----------------|
| POST/GET/PATCH `/tickets`, GET `/tickets/{id}` (no comments yet in Phase 2 if split) | Phase 2 |
| `POST /tickets/{id}/status` | Phase 3 |
| Comments + list `q` / `status` | Phase 4 |

Phase 2 MAY return empty `comments: []` on GET detail until Phase 4.
