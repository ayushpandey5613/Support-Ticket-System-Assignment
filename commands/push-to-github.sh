#!/usr/bin/env bash
# Push via SSH (personal account ayushpandey5613). One-time: add public key on GitHub.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

git remote set-url origin git@github-ayushpandey5613:ayushpandey5613/Support-Ticket-System-Assignment.git

echo "Remote:"
git remote -v
echo ""
git log -1 --oneline || { echo "No commit yet."; exit 1; }

if ! ssh -o BatchMode=yes -T git@github-ayushpandey5613 2>&1 | grep -q "Hi ayushpandey5613"; then
  echo "SSH not set up for ayushpandey5613 yet."
  echo "1) Open: https://github.com/settings/ssh/new"
  echo "2) Title: laptop-ticket-system"
  echo "3) Paste this public key:"
  echo ""
  cat "$HOME/.ssh/id_ed25519_ayushpandey5613.pub"
  echo ""
  echo "4) Save, then run this script again."
  exit 1
fi

git push -u origin main
echo "Done: https://github.com/ayushpandey5613/Support-Ticket-System-Assignment"
