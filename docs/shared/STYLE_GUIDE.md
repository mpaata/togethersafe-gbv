# STYLE_GUIDE.md --- Shared Document

**How we write code, docs, commits, and user-facing text in TogetherSafe.**

## 1. How to Use This Document
- **Every contributor** reads this before writing anything.
- **Code style** is enforced by tools (Checkstyle, Prettier, Black).
- **Documentation style** is enforced by review.
- **User-facing text** is reviewed by Product + QA.
- **Never invent a new convention** — propose it in a PR first.

## 2. Guiding Principles

1. **Clarity over cleverness.** Write code and prose that a tired teammate can understand at 2 AM.
2. **Consistency above preference.** Follow the existing style, even if you would choose differently.
3. **Explicit over implicit.** Name things clearly. Avoid magic.
4. **Survivor-centred language.** See `GLOSSARY.md`.
5. **Minimal surprise.** Code, docs, and UI should do exactly what they say.

---

## 3. Java Code Style

### 3.1 Formatting
- **Indentation:** 4 spaces (never tabs).
- **Line length:** 120 characters maximum.
- **Braces:** K&R style (opening brace on the same line).
- **Blank lines:** One between methods, no double blank lines.
- **Imports:** No wildcard imports (`import java.util.*` is forbidden). Group by: java, javax, third-party, com.togethersafe.

### 3.2 Naming
| Thing | Convention | Example |
|---|---|---|
| Package | lowercase, dot-separated | `com.togethersafe.backend.service` |
| Class | PascalCase | `CaseService` |
| Interface | PascalCase | `CaseRepository` |
| Method | camelCase, verb-first | `createCase()`, `findByReference()` |
| Variable | camelCase | `caseReference` |
| Constant | UPPER_SNAKE_CASE | `MAX_FILE_SIZE_BYTES` |
| Enum | PascalCase, values UPPER_SNAKE | `CaseStatus.IN_PROGRESS` |
| Boolean | `isX` or `hasX` | `isActive`, `hasEvidence` |
| DTO (record) | PascalCase + `Request`/`Response` | `CreateCaseRequest` |
| Test method | `shouldXWhenY()` | `shouldThrowWhenCaseNotFound()` |

### 3.3 Class Structure
Order within a class:
1. Static constants
2. Static fields
3. Instance fields (final first)
4. Constructors
5. Public methods
6. Package-private methods
7. Private methods
8. Inner classes

### 3.4 Methods
- Keep methods **under 40 lines** where possible.
- One method = one responsibility.
- Prefer **early returns** over nested if/else.
- Use **`final`** on parameters and local variables where reasonable.
- Never use `System.out.println` — use SLF4J (`log.info`, `log.debug`).

### 3.5 Null Handling
- Prefer `Optional<T>` for repository finders.
- Use `Objects.requireNonNull()` for required constructor args.
- Never return `null` from a public method if you can return an empty collection or Optional.
- Use `@NonNull` / `@Nullable` annotations where clarity helps.

### 3.6 Exceptions
- Never catch `Exception` and swallow it. Log or rethrow.
- Use TogetherSafe custom exceptions (`ResourceNotFoundException`, `PermissionDeniedException`, etc.) — see `ERROR_CODES.md`.
- Never throw `RuntimeException` directly — use a specific subclass.
- Every message must be clear and human-readable.

### 3.7 Logging
- Use SLF4J, never `System.out.println`.
- `log.error` for 500s and unexpected failures.
- `log.warn` for misuse (4xx that indicates a client problem).
- `log.info` for business events (case created, user logged in).
- `log.debug` for detail useful during development.
- **Never log:** passwords, tokens, full PII, case narratives, evidence content.
- Use placeholders: `log.info("Case {} created by user {}", caseId, userId);`

### 3.8 Comments and Javadoc
- **Javadoc on all public service methods** — describe parameters, return value, exceptions.
- Inline comments explain **why**, not **what** (the code shows what).
- No commented-out code in commits. Delete it — Git remembers.
- No `TODO` without an issue number: `// TODO(#42): Add retry logic`.

### 3.9 Tooling
- **Checkstyle** enforces formatting.
- **SpotBugs** finds bugs.
- **Maven** runs all checks in `mvn verify`.
- **No commit passes CI if it fails any of these.**

---

## 4. JavaFX (FXML + CSS) Style

### 4.1 FXML Files
- **kebab-case** filenames: `case-vault.fxml`
- **camelCase** `fx:id` matching the controller field.
- One root container per FXML.
- No inline styles — use CSS classes.
- Never put logic in FXML.

### 4.2 Controllers
- Annotate fields with `@FXML`.
- Implement `Initializable`, initialize in `initialize()`.
- Never do network calls on the FX thread — use `TaskRunner`.
- Show errors via `Toast` or `Modal`.
- See `docs/specialists/DESKTOP.md` for full conventions.

### 4.3 CSS Classes
- **kebab-case** class names: `.btn-primary`, `.badge-open`.
- Follow `DESIGN_SYSTEM.md` for colors, spacing, and radii.
- Never use inline styles in FXML.
- Never invent new colors.

---

## 5. SQL / Database Style

- **Table names:** snake_case, plural (`cases`, `evidence_files`)
- **Column names:** snake_case (`case_reference`)
- **Foreign keys:** `<table_singular>_id` (`case_id`)
- **Indexes:** `idx_<table>_<column>`
- **Unique constraints:** `uk_<table>_<column>`
- **FK constraints:** `fk_<table>_<column>`
- **Check constraints:** `ck_<table>_<rule>`
- **Migrations:** `V<n>__<description>.sql`
- **Always use uppercase for SQL keywords** (`SELECT`, `FROM`, `WHERE`).
- **Never use `SELECT *`** — list columns explicitly.
- **Always use parameterized queries** — never string concatenation.
- See `docs/specialists/DATABASE.md` for full conventions.

---

## 6. Python (AI Service) Style

- Follow **PEP 8**.
- Use **Black** for formatting.
- Use **Ruff** for linting.
- Use **type hints** everywhere.
- **snake_case** for functions and variables.
- **PascalCase** for classes.
- Use **Pydantic** for all request/response models.
- Never log raw chat messages or case narratives.
- See `docs/specialists/AI_SERVICE.md` for full conventions.

---

## 7. Documentation Style

### 7.1 Markdown Files
- **One H1 per file** (the title).
- **H2 for major sections** (`## 1. Overview`), **H3 for sub-sections**.
- Use **numbered sections** for long docs (`## 1.`, `## 2.`).
- Use **fenced code blocks** with a language tag:


java
public class Example {}
text
- Use **tables** for structured comparisons.
- Use **bold** for key terms, `code` for identifiers.
- Blank line between all block elements.

### 7.2 File Naming
- **UPPER_SNAKE_CASE** for root-level docs (`README.md`, `SECURITY.md`).
- **UPPER_SNAKE_CASE** for shared and specialist docs (`API_CONTRACT.md`).
- **kebab-case** for non-canonical docs if needed.

### 7.3 Writing Rules
- **Plain English.** Short sentences. Active voice.
- **No jargon** unless defined in `GLOSSARY.md`.
- **Second person** for user guides ("You can report a case...").
- **Third person** for technical docs ("The service validates input...").
- **No emojis** in technical docs. Emojis are OK in READMEs sparingly.
- **Oxford comma:** use it (a, b, and c).

---

## 8. Commit Message Style

We use **Conventional Commits**.

### 8.1 Format
<type>(<scope>): <subject>

<body><footer> ```
8.2 Types
Type	Use
feat	A new feature
fix	A bug fix
docs	Documentation only
style	Formatting, no logic change
refactor	Code change that neither fixes a bug nor adds a feature
test	Adding or fixing tests
chore	Build, tooling, deps
perf	Performance improvement
security	Security-relevant change
revert	Revert a previous commit
8.3 Examples
Good:

text
feat(cases): add anonymous reporting endpoint

- Allows POST /cases without authentication.
- Returns a one-way tracking reference.
- Does not store IP or User-Agent.

Closes #42
Bad:

text
fixed stuff
8.4 Rules
Subject line: 50 characters max, imperative mood ("add," not "added").

Body: explain why, not just what. Wrap at 72 characters.

Reference issues: Closes #42, Fixes #88.

Never commit secrets.

Never commit real survivor data.

9. Branch Naming
feature/<short-name> — new feature

fix/<short-name> — bug fix

docs/<short-name> — documentation

refactor/<short-name> — code refactor

chore/<short-name> — tooling/build

security/<short-name> — security fix

Examples: feature/evidence-vault, fix/sos-button-crash, docs/api-contract.

10. User-Facing Text Style
This is critical for a GBV application. Every word matters.

10.1 Tone of Voice
Calm, not clinical.

Supportive, not paternalistic.

Empowering, not commanding. Provide options, not instructions.

Neutral, not judgmental. Never imply blame.

Plain language. No technical jargon.

10.2 Rules
Use second person ("You can...", "Your safety...").

Use "survivor", never "victim" (see GLOSSARY.md).

Avoid ALL CAPS except for emergency labels (SOS).

No exclamation marks in error messages or warnings.

No technical error messages to the user (log them, show a friendly message).

No emojis in the UI (except perhaps celebratory moments, rarely).

10.3 Examples
Bad: "ERROR: Invalid credentials. Please re-enter."
Good: "We couldn't sign you in. Please check your username and password."

Bad: "Case #1234 has been submitted successfully!"
Good: "Your case has been received. You can track it with the reference number below."

Bad: "You must upload evidence."
Good: "You can add evidence now, or come back later. Your safety is what matters most."

Bad: "Failed to publish. Cannot publish: 3 students have no marks."
Good: "We can't publish yet — 3 people haven't received a mark. Add their marks or save as draft."

10.4 Crisis Messaging
Never hide crisis resources behind a menu.

Never require the user to dismiss a modal to access emergency numbers.

Never say "Calm down" or "Don't worry."

Always include the disclaimer for AI responses: "I am an AI assistant. If you are in immediate danger, call 999."

Always escalate on suicide ideation or threats to life.

11. Testing Style
11.1 Java (JUnit 5)
Test method names: should<Behavior>When<Condition>()

One behavior per test.

Use @DisplayName for human-readable descriptions.

Mock external dependencies with Mockito.

Use Testcontainers for real PostgreSQL integration tests.

Example:

java
@Test
@DisplayName("should throw when survivor accesses another survivor's case")
void shouldThrowWhenSurvivorAccessesAnotherSurvivorsCase() {
    // ...
}
11.2 Python (pytest)
Test file names: test_<module>.py

Test function names: test_<behavior>()

Use fixtures for setup.

Never hit real external services — mock them.

11.3 Test Coverage
Aim for 80%+ coverage on new code.

Do not test trivial getters/setters.

Always test error paths — happy path alone is 25% of the coverage.

12. What NOT to Do
Don't use System.out.println in Java — use SLF4J.

Don't use wildcard imports.

Don't write methods over 40 lines.

Don't use SELECT * in SQL.

Don't hardcode colors, URLs, or secrets.

Don't commit commented-out code.

Don't commit .env files or credentials.

Don't commit real survivor data.

Don't use "victim" in user-facing text.

Don't use exclamation marks in error messages.

Don't say "Calm down" or "Don't worry" in user-facing text.

Don't write a "fix bug" commit message — be specific.

Don't skip the body of a commit message when the change is complex.

Don't use emojis in technical docs.

Don't skip tests for "obvious" code.

13. Enforcement
Rule	Enforced by
Java formatting	Checkstyle (build fails)
Java bugs	SpotBugs (build fails)
Python formatting	Black + Ruff (build fails)
Commit message format	commitlint (build fails)
Branch naming	Branch protection rules
Documentation style	PR review
User-facing text	Product + QA review
Survivor-centred language	Every reviewer
If CI fails, fix the code — never disable the check.

The One-Sentence Summary
STYLE_GUIDE.md defines how we write everything in TogetherSafe — Java, FXML, SQL, Python, documentation, commits, and user-facing text. Code style is tool-enforced. Documentation and user-facing text are review-enforced. Survivor-centred language is non-negotiable.