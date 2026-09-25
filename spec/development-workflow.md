# Spec-Driven Development Workflow

This project follows the assessment workflow end-to-end.

```text
Requirement  →  Specification  →  Plan / Tasks  →  Implementation  →  Testing  →  Review  →  Fix
```

## Artefact mapping

| Step | Where it lives |
|------|----------------|
| Requirement | `spec/requirements.md` (FR/NFR IDs) |
| Specification | `spec/architecture.md`, `data-model.md`, `api-contract.md`, `state-machine.md`, `ui-flow.md` |
| Plan / Tasks | `spec/architecture.md` (phase roadmap), phase notes in `docs/prompt-history.md` |
| Implementation | `backend/`, `frontend/` |
| Testing | `spec/test-strategy.md`, `rules/testing.md` |
| Review | `commands/review-code.md`, `commands/review-spec.md` |
| Fix | Code + spec updates + `docs/ai-review-log.md` |

## Rules for AI-assisted work

1. **No** “build the entire application” in one prompt — implement one phase or slice.
2. Load `rules/` and relevant `spec/` files before coding.
3. Save prompts: SpecStory → `.specstory/history/`; summary → `docs/prompt-history.md`.
4. Reject and document bad AI output in `docs/ai-review-log.md` (minimum: one real example per phase).
5. Token efficiency: keep context scoped (see `docs/ai-tooling.md`).

## Phase gate checklist

Before closing a phase:

- [ ] Spec matches code (or code matches spec if spec was updated first).
- [ ] Tests added per `test-strategy.md` for that slice.
- [ ] `commands/review-code.md` run; findings addressed or logged.
- [ ] Commit message references phase (e.g. `chore: phase 1 spec scaffolding`).
- [ ] Update `spec/implementation-status.md` when FR/API slices ship.
