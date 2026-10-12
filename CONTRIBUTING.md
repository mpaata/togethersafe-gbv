# Contributing to TogetherSafe

## 1. Our Workflow
We use a **Feature Branch Workflow**. 
- The `main` branch is sacred. It must always be stable and deployable.
- **NEVER push directly to `main`.** 
- All work must be done on a feature branch and merged via a Pull Request (PR).

## 2. Branch Naming
- `feature/name` (e.g., `feature/evidence-vault`)
- `fix/name` (e.g., `fix/sos-button-crash`)
- `docs/name` (e.g., `docs/update-readme`)
- `chore/name` (e.g., `chore/update-dependencies`)

## 3. The Pull Request Process
1. Create your branch: `git checkout -b feature/your-feature`
2. Do your work, commit often.
3. Push to GitHub: `git push origin feature/your-feature`
4. Open a Pull Request against `main`.
5. At least **one other team member must review and approve** the PR.
6. CI tests must pass.
7. Merge the PR (Squash and Merge is preferred to keep history clean).

## 4. Commit Message Conventions
We use Conventional Commits:
- `feat:` A new feature
- `fix:` A bug fix
- `docs:` Documentation only changes
- `style:` Changes that do not affect the meaning of the code (white-space, formatting)
- `refactor:` A code change that neither fixes a bug nor adds a feature
- `test:` Adding missing tests or correcting existing tests
- `chore:` Changes to the build process or auxiliary tools

Example: `git commit -m "feat: add evidence upload to case vault"`

## 5. Code Reviews
- Be kind, be constructive.
- Review the code, not the person.
- If you don't understand something, ask.
- No PR is too small for a review.

## 6. Security Rule
- **NEVER** commit `.env` files, passwords, API keys, or real survivor data.
- Use the `.gitignore` to prevent accidental commits.