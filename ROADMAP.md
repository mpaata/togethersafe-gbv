# TogetherSafe Development Roadmap

This roadmap outlines the phased approach to building the TogetherSafe desktop application. We will build this incrementally, focusing on a solid foundation before adding complex features.

## Milestone 0: Project Setup & Infrastructure
- [ ] Create GitHub Repository (Add topics: `gbv`, `javafx`, `spring-boot`, `postgresql`, `python`, `social-impact`).
- [ ] Initialize Maven project with JavaFX, Spring Boot, PostgreSQL, JUnit 5, and TestFX dependencies.
- [ ] Setup `docker-compose.yml` for local PostgreSQL and Keycloak.
- [ ] Create the foundational folder structure (`src/main/java`, `src/main/resources`, `ai-service/`).

## Milestone 1: Core Foundation & Authentication
- [ ] Build the JavaFX Shell (Main window, navigation sidebar, basic CSS theme).
- [ ] Setup Spring Boot REST API backend.
- [ ] Integrate Keycloak for OAuth 2.0 / LDAP authentication.
- [ ] Implement Role-Based Access Control (RBAC) for Survivor, Caseworker, and Admin roles.
- [ ] **Deliverable:** Users can log in securely, and the UI shows different views based on their role.

## Milestone 2: Database & Core Case Management
- [ ] Design and implement PostgreSQL schema (Users, Cases, Evidence, Referrals).
- [ ] Implement JPA/Hibernate entities and Repository interfaces.
- [ ] Build Admin Dashboard (Summary cards, recent reports table).
- [ ] Build Survivor "My Cases" and Case Tracker views.
- [ ] **Deliverable:** A functional CRUD application for managing cases.

## Milestone 3: Survivor Safety & Evidence
- [ ] Implement SOS Center (Emergency contacts, quick actions).
- [ ] Build Safety Planning module.
- [ ] Implement Evidence Vault (File upload, SHA-256 cryptographic hashing).
- [ ] Implement explicit evidence sharing controls.
- [ ] Implement Disguised Mode (JNA to hide taskbar/process list).
- [ ] **Deliverable:** Survivors can safely store and share evidence, with a hidden mode for shared devices.

## Milestone 4: AI Integration & Services
- [ ] Build Python FastAPI microservice for the AI chatbot.
- [ ] Integrate AI chatbot into JavaFX (via REST API).
- [ ] Implement crisis detection and escalation.
- [ ] Integrate Map API (e.g., OpenStreetMap) for the "Find Help Map".
- [ ] Build Service Directory and Referral History.
- [ ] **Deliverable:** AI-guided support, crisis detection, and location-based service discovery.

## Milestone 5: Advanced Security & Audit
- [ ] Implement Audit Logging for all sensitive actions.
- [ ] Implement OAuth 2.0 / LDAP via Keycloak for all roles.
- [ ] Add SHA-256 evidence integrity verification.
- [ ] Add EXIF metadata stripping for uploaded images.
- [ ] **Deliverable:** A highly secure, privacy-first, auditable application.

## Milestone 6: Testing, Polish & Deployment
- [ ] Write comprehensive JUnit 5 unit tests.
- [ ] Write TestFX UI tests for critical workflows.
- [ ] Perform privacy and security testing (ensure no data leaks).
- [ ] Perform usability testing with 5 users (see UX_RESEARCH.md).
- [ ] Package the application for Windows, macOS, and Linux using `jpackage`.
- [ ] **Deliverable:** Production-ready release candidate.

## Milestone 7: First Pilot Deployment
- [ ] Onboard the first partner organization.
- [ ] Import services into the Service Directory.
- [ ] Train caseworkers and admins.
- [ ] Monitor for one full term (or pilot period).
- [ ] Collect feedback and iterate.
- [ ] **Deliverable:** A real organization using TogetherSafe in production.

## Success Metrics for v1

The v1 release is considered successful when:
- A survivor can report a case anonymously in under 3 minutes.
- A caseworker can triage 5 cases in under 15 minutes.
- Evidence uploads are private by default and require explicit consent to share.
- The AI detects and escalates crisis messages.
- Disguised Mode hides the app from the taskbar.
- Zero security incidents (data leaks, unauthorized access).
- Users say: "I would use this again."

## What is NOT in v1

- Web portal (planned for v2+)
- Mobile apps (iOS/Android)
- Push notifications
- 1:1 messaging
- Forums and community features
- Offline mode
- Multi-organization management
- Analytics dashboards
- Advanced AI (voice, image analysis)

## Guiding Principles

1. **Safety first.** No feature ships if it puts a survivor at risk.
2. **Survivor-centred.** Every decision is tested against survivor needs.
3. **Privacy by default.** Data collection is minimal, sharing is explicit.
4. **Slowly but surely.** Ship small, ship often, ship safely.
5. **Document everything.** See `docs/` for the full documentation set.

---

**Last updated:** 2026-10-13
**Current phase:** Documentation complete — ready to start Milestone 0.