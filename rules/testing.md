# Testing Guidelines

## Pyramid

1. **Unit tests** — state machine, validators, service rules (fast, no Spring context where possible).
2. **Integration tests** — `@SpringBootTest` + Testcontainers or H2 for repositories and full HTTP (`@AutoConfigureMockMvc` or `WebTestClient`).
3. **Contract tests** — assert responses match `spec/api-contract.md` (status codes, error shapes).
4. **Frontend** — component tests for forms/errors; E2E optional in later phase.

## Backend (JUnit 5)

- Name: `MethodName_condition_expectedOutcome`.
- **State machine**: dedicated test class covering every allowed edge and every rejected transition from `spec/state-machine.md`.
- **Validation**: 400 with field errors for invalid create/update payloads.
- **Persistence**: at least one test that creates data, restarts context (or uses separate transaction + clear cache), and verifies data still present when using file H2 or Testcontainers PostgreSQL.

## Test data

- Use builders or small fixtures in `src/test/java/.../support/`.
- No reliance on execution order; tests must be isolated.

## Running

```bash
cd backend && ./mvnw test
```

CI should run `test` on every push (to be added with backend module).

## AI-generated tests

- Do not accept tests that only mock the class under test without asserting behavior.
- Reject tests that duplicate production logic for transitions (assert outcomes via public API).
- Require negative cases for invalid status transitions.

## Coverage expectations

- 100% branch coverage on status transition logic.
- Controllers: happy path + main error paths per endpoint.
