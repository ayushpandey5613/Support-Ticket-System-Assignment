# Command: Review Code

Use this prompt in Cursor/Copilot after implementing a slice.

---

Review the changed code against project rules and specs.

**Context to load**

- `rules/java-springboot.md` or frontend conventions in `spec/architecture.md`
- `rules/testing.md`
- `rules/api-standards.md`
- `spec/api-contract.md`
- `spec/state-machine.md`

**Instructions**

1. List files changed and summarize behavior impact.
2. Check: validation on boundary, state machine only in service layer, no secrets, error shape matches API standards.
3. Check: tests exist for new behavior and invalid transitions.
4. Find at least 2 issues (bugs, spec drift, missing tests, security). If none, explain what was verified.
5. Suggest minimal fixes — no drive-by refactors.

**Output format**

- Findings (severity: high/medium/low)
- Spec drift (if any)
- Suggested patch list (file + one-line change)

---
