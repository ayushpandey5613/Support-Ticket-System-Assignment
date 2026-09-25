# Phase 2 cross-check (spec-driven review)

Date: 2026-09-25  
Scope: `backend/` Phase 2 vs `spec/`, `rules/`

## Coverage matrix

| Spec | Verified |
|------|----------|
| `api-contract.md` Phase 2 endpoints | POST/GET/PATCH match paths and shapes |
| `data-model.md` Ticket fields | UUID, enums, lengths, defaults OPEN/MEDIUM |
| `rules/api-standards.md` errors | 400 `VALIDATION_ERROR` + `fieldErrors`; 404 `NOT_FOUND` |
| `rules/java-springboot.md` layering | Controller → Service → Repository; no status logic in web |
| `test-strategy.md` IT-01, IT-02, IT-07 | `TicketControllerIntegrationTest` |

## Findings addressed in this review

| Severity | Issue | Fix |
|----------|-------|-----|
| Low | `pom.xml` invalid `<n>` element | Corrected to `<name>` |
| Medium | No single traceability doc for phased FRs | Added `spec/implementation-status.md` |
| Low | List pagination response not asserted in tests | Added test for `content`, `page`, `size`, `totalElements` |
| Low | Empty PATCH body edge case | Added test for `{}` → 400 |

## Intentional deferrals (documented)

- `GET /tickets?q` and `?status` → Phase 4 (`spec/api-contract.md` implementation phases)
- Status transitions → Phase 3
- PostgreSQL profile → Phase 8
- Frontend → Phase 5+

## Review command

Ran equivalent of `commands/review-code.md` manually; log updates in `docs/ai-review-log.md`.
