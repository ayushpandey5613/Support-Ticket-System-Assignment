# AI Review Log

Document where AI output was **reviewed and corrected** (not accepted blindly).

| Date | Context | AI suggestion / mistake | Correct approach |
|------|---------|-------------------------|------------------|
| 2026-09-25 | Phase 1 spec | Suggested updating `status` via generic `PATCH /tickets/{id}` | Status only via `POST /tickets/{id}/status`; PATCH explicitly excludes status (`spec/api-contract.md`) |
| 2026-09-25 | Phase 1 spec | Enforcing allowed transitions only in the React UI | Backend MUST return `409` with `INVALID_STATUS_TRANSITION`; UI buttons are hints only (`spec/ui-flow.md`, `rules/testing.md`) |
| 2026-09-25 | Phase 1 pre-push review | `data-model.md` had conflicting text (block edits on CLOSED vs allow all) | Single rule: field updates and comments allowed in any status; only transitions restricted (`spec/data-model.md`) |
| 2026-09-25 | Phase 1 pre-push review | `java-springboot.md` said “UUID or Long — pick one” while data model already fixed UUID | Aligned guidelines to UUID only (`rules/java-springboot.md`) |
| 2026-09-25 | Phase 1 pre-push review | `api-standards.md` transition error JSON omitted `timestamp`/`status` wrapper | All errors use one envelope; 409 includes `from`/`to` (`rules/api-standards.md`) |
| 2026-09-25 | Phase 1 pre-push review | `ai-review-log.md` used “placeholder” rows | Replaced with real spec decisions rejected during design (this table) |

Add a row after each `commands/review-code.md` session during implementation phases.
