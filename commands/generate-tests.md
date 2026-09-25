# Command: Generate Tests

Use after backend or frontend slice is implemented.

---

Generate tests aligned with `spec/test-strategy.md` and `rules/testing.md`.

**Scope**

- Name the module: `backend` or `frontend`.
- Name the feature slice (e.g. "status transitions", "comment API").

**Instructions**

1. Read `spec/state-machine.md` for transition cases — generate parameterized tests for all invalid pairs.
2. Integration tests for REST: status codes and error `code` fields per `rules/api-standards.md`.
3. Do not generate trivial tests (getters/setters, empty assertions).
4. Use existing test utilities and naming conventions in the repo if present.
5. List tests added and which requirement IDs they cover.

**Backend example targets**

- `TicketStatusTransitionTest` (unit)
- `TicketControllerIntegrationTest` (MockMvc/WebTestClient)

**Reject**

- Tests that encode transition rules duplicated incorrectly from production
- Hardcoded secrets or real DB URLs

---
