# Test Strategy

## Goals

- Prove acceptance criteria in `requirements.md`
- Lock state machine behavior with automated tests
- Prevent spec drift via contract-oriented integration tests

## Backend

### Unit

| Area | Class (planned) | Cases |
|------|-----------------|-------|
| Transitions | `TicketStatusTransition` | All allowed + forbidden from `state-machine.md` |
| Validation | DTO validators | blank title, enum typos |

### Integration (`@SpringBootTest` + MockMvc)

| ID | Scenario | FR |
|----|----------|-----|
| IT-01 | POST ticket → GET by id | FR-01, FR-03 |
| IT-02 | PATCH updates fields | FR-04 |
| IT-03 | POST comment appears on GET detail | FR-05 |
| IT-04 | List with `q` and `status` | FR-06, FR-07 |
| IT-05 | Valid transition chain OPEN → … → CLOSED | FR-08 |
| IT-06 | CLOSED → OPEN returns 409 | FR-08 |
| IT-07 | Invalid body returns 400 + fieldErrors | FR-10 |
| IT-08 | Restart persistence (profile-specific) | FR-09 |

**State-machine suite:** dedicated class `TicketStatusTransitionIntegrationTest` — required for assignment sign-off.

## Frontend (Phase 6+)

- Manual test checklist derived from `ui-flow.md`
- Optional: Playwright smoke (create → list → detail) in later iteration

## Test data

- Minimal fixtures; no shared mutable state between tests

## CI (future)

```bash
cd backend && ./mvnw -q test
cd frontend && npm test --if-present
```

## Review gate

Before phase complete:

1. Run `commands/generate-tests.md` and prune low-value tests
2. Run `commands/review-code.md`
3. Update `docs/ai-review-log.md` if AI suggested wrong transitions or skipped 409 cases
