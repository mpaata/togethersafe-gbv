# Changelog

All notable changes to TogetherSafe will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## How to Read This Changelog

- **Added** for new features.
- **Changed** for changes in existing functionality.
- **Deprecated** for soon-to-be-removed features.
- **Removed** for features removed in this release.
- **Fixed** for any bug fixes.
- **Security** for security-relevant changes.

## Versioning Rules

We use **Semantic Versioning** (`MAJOR.MINOR.PATCH`):

- **MAJOR** version when we make incompatible API changes.
- **MINOR** version when we add functionality in a backward-compatible manner.
- **PATCH** version when we make backward-compatible bug fixes.

Pre-release versions use suffixes: `1.0.0-alpha.1`, `1.0.0-beta.2`, `1.0.0-rc.1`.

## [Unreleased]

### Added
- Initial project documentation:
  - `README.md` — project overview and setup.
  - `ARCHITECTURE.md` — system architecture and technology stack.
  - `ROADMAP.md` — phased development plan.
  - `CONTRIBUTING.md` — contribution workflow and rules.
  - `MASTER.md` — master project context for AI-assisted development.
  - `SECURITY.md` — security policy, threat model, and responsible disclosure.
  - `CODE_OF_CONDUCT.md` — community behavior guidelines.
  - `docs/specialists/DESKTOP.md` — JavaFX developer guide.
  - `docs/specialists/BACKEND.md` — Spring Boot backend guide.
  - `docs/specialists/DATABASE.md` — PostgreSQL schema and migration guide.
  - `docs/specialists/DEVOPS.md` — server, deployment, and backup guide.
  - `docs/specialists/AI_SERVICE.md` — Python FastAPI AI microservice guide.
  - `docs/specialists/PRODUCT_QA.md` — Product and QA testing guide.
  - `docs/specialists/UX_RESEARCH.md` — Personas, journeys, and design principles.
  - `docs/shared/API_CONTRACT.md` — REST API endpoints and response shapes.
  - `docs/shared/SCHEMA.md` — full database schema (9 tables for v1).
  - `docs/shared/ERROR_CODES.md` — standard error catalog (28 codes).
  - `docs/shared/DESIGN_SYSTEM.md` — colors, typography, and JavaFX CSS.
  - `docs/shared/GLOSSARY.md` — GBV and technical terminology.
  - `docs/shared/STYLE_GUIDE.md` — code, docs, and commit conventions.
  - `docs/operations/RUNBOOK.md` — deployment and incident procedures.

## [0.1.0] - 2026-10-13

### Added
- Initial repository creation.
- MIT License.
- Base project structure.

---

## How to Update This File

**Every Pull Request that ships user-visible changes must update this file.**

1. Add your changes under the `[Unreleased]` section.
2. Use the correct category (Added, Changed, Fixed, Security, etc.).
3. Be clear and specific — no jargon.
4. Reference the issue number if applicable.
5. When a release is made, move the `[Unreleased]` items under a new version heading with the release date.

**Example:**

    ## [1.2.0] - 2026-11-15

    ### Added
    - Evidence Vault with SHA-256 checksums (#88).

    ### Fixed
    - Crash when uploading files larger than 25 MB (#91).

    ### Security
    - Patched JWT expiry bypass in `/auth/refresh` (#94).

## What NOT to Do

- Don't change the format of past releases. They are a historical record.
- Don't skip the Security section if you patched a vulnerability.
- Don't write "fixed bugs" — be specific.
- Don't add entries for internal refactors that don't affect users.
- Don't let `[Unreleased]` pile up for more than one release cycle.

## Links

- [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
- [Semantic Versioning](https://semver.org/spec/v2.0.0.html)
- [Common Changelog](https://common-changelog.org/)   


