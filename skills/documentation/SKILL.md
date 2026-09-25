---
name: documentation
description: >-
  Keep specs, API contract, and prompt history in sync when features change.
  Use when adding endpoints, changing the state machine, or shipping a phase.
---

# Documentation Skill

## When to use

- After any API or behavior change.
- Before marking a phase complete.
- When recording AI prompts and corrections.

## Checklist

1. Confirm phase aligns with `spec/development-workflow.md` and `spec/architecture.md` roadmap.
2. Update `spec/api-contract.md` if endpoints or payloads changed.
3. Update `spec/data-model.md` if entities or fields changed.
4. Update `spec/state-machine.md` if transitions changed.
5. Update `spec/ui-flow.md` if user journeys changed.
6. Append to `docs/prompt-history.md` (date, goal, prompt summary).
7. Log AI mistakes or bad suggestions in `docs/ai-review-log.md`.

## Writing style

- Specs are normative ("MUST", "SHOULD"); README-style fluff belongs in phase notes only.
- One source of truth: contract for API, state-machine for statuses.

## Phase completion note

Add a short entry under `docs/prompt-history.md`:

```markdown
### Phase N complete — YYYY-MM-DD
- Spec files touched: ...
- Known gaps for next phase: ...
```
