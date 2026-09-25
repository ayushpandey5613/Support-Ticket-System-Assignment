# Command: Review Spec

Use before implementation of each phase.

---

Review specifications for completeness and internal consistency.

**Read**

- `spec/requirements.md`
- `spec/development-workflow.md`
- `spec/architecture.md`
- `spec/data-model.md`
- `spec/api-contract.md`
- `spec/state-machine.md`
- `spec/ui-flow.md`
- `spec/test-strategy.md`

**Instructions**

1. Trace each requirement ID to API, data model, UI flow, and test strategy.
2. Flag contradictions (e.g. contract allows transition spec forbids).
3. Flag ambiguities (assignee format, search scope, pagination).
4. Propose concrete edits to spec files — do not write application code.
5. Confirm acceptance criteria from requirements are testable.

**Output**

- Coverage matrix (requirement → artefacts)
- Gaps and recommended spec edits (priority ordered)

---
