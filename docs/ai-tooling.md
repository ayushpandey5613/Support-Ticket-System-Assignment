# AI Tooling & Token Optimisation

## IDE setup

- **Cursor** — project rules in `.cursor/rules/`; steering in `rules/`, `commands/`, `skills/`.
- **GitHub Copilot** — use same `rules/` and `spec/` as context via open files or Copilot instructions where supported.

## Token efficiency (assessment)

Prefer narrow context over pasting the whole repo:

| Approach | Use when |
|----------|----------|
| Open only relevant `spec/` slice + target module | Implementing a phase |
| `commands/review-spec.md` / `review-code.md` | Review passes |
| Codebase index / memory plugins (e.g. Graphify, Caveman, codebase-memory MCP) | Large codebase after Phase 5+ |
| `@` file references in Cursor | Pointing AI at `api-contract.md`, `state-machine.md` |

## Hygiene

- Do not embed secrets in prompts.
- Paste API examples from `spec/api-contract.md` instead of duplicating in chat.
