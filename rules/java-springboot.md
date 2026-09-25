# Java & Spring Boot Guidelines

## Stack

- Java 21 (records where appropriate; avoid preview features unless documented).
- Spring Boot 3.x: Web, Validation, Data JPA, optional springdoc-openapi.
- Build: Maven (default for this repo unless changed in `spec/architecture.md`).

## Package layout (`backend/`)

```
com.ticketsystem
├── config          # CORS, Jackson, exception handling
├── domain          # JPA entities, enums (TicketStatus, Priority)
├── repository
├── service         # business rules, state machine
├── web             # REST controllers, DTOs, mappers
└── exception       # domain + API errors
```

## Conventions

- **Controllers**: thin; no business logic. Return DTOs, not entities.
- **Validation**: Jakarta Bean Validation on request DTOs (`@Valid`); custom validators for status transitions in service layer.
- **State machine**: enforced only in `TicketService` (or dedicated `TicketStatusTransition` component); never in controllers or repositories.
- **IDs**: `UUID` for Ticket and Comment (see `spec/data-model.md`).
- **Timestamps**: `Instant` or `OffsetDateTime` in UTC; set via `@CreationTimestamp` / `@UpdateTimestamp` or auditing.
- **Errors**: use `@ControllerAdvice` + problem details style (`code`, `message`, `fieldErrors` for validation).
- **Security**: no secrets in repo; use `application-local.yml` (gitignored) or env vars for DB credentials.
- **Logging**: no PII in logs; log ticket id and transition, not full description bodies in INFO.

## Database

- **Dev/test**: H2 in-memory or file mode via Spring profile `dev` / `test`.
- **Prod-like**: PostgreSQL via profile `postgres`.
- Flyway/Liquibase optional; Phase 2 may use `ddl-auto=validate` once schema is stable.

## API alignment

- Implement endpoints exactly as `spec/api-contract.md` versions.
- When contract changes, bump version note in contract and update integration tests.

## Dependencies

- Prefer Spring starters over ad-hoc libraries.
- Add new dependencies only with justification in PR/spec notes.
