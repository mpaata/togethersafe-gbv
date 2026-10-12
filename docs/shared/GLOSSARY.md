# GLOSSARY.md --- Shared Document

**Every term, acronym, and concept used in TogetherSafe. The single source of truth for language.**

## 1. How to Use This Document
- **Every contributor:** read this before writing code, docs, or user-facing text.
- **When in doubt about a term:** check here first.
- **New term needed?** Add it here in a PR before using it in the codebase.
- **Never invent terminology** that is not defined here.

## 2. Why This Matters (GBV Context)

In GBV work, **words have weight**. Wrong terminology can:
- Blame the survivor ("she was assaulted" vs. "she was raped by him").
- Minimize violence ("domestic dispute" vs. "intimate partner violence").
- Expose identities (using "victim" when "survivor" is safer).
- Misrepresent consent or coercion.

**We use survivor-centred language everywhere.**

---

## 3. GBV Domain Terms

### A

**Anonymity**
The state of being unidentifiable. In TogetherSafe, anonymous reports do not store IP addresses, User-Agents, or any identifier linking the report to a person.

**Assault**
A physical attack. In GBV context, "assault" specifically refers to non-consensual physical violence. Prefer specific terms (e.g., "physical assault," "sexual assault") over generic "assault."

### B

**Bystander**
A person who witnesses or is aware of GBV but is not directly involved. Bystander intervention programs train people to safely intervene.

### C

**Case**
A record of a reported incident of GBV, tracked through the platform from intake to closure.

**Case Reference**
A human-readable identifier for a case (e.g., `CASE-2026-0001`).

**Caseworker**
An authorized professional (social worker, counsellor, legal aid officer) who handles cases on behalf of survivors.

**Child Marriage**
Marriage of a person under 18. Illegal in Uganda and most jurisdictions. Considered a form of GBV.

**Consent**
Freely given, informed, and ongoing agreement. In GBV context, absence of "no" is not consent. Coerced consent is not consent.

**Coercive Control**
A pattern of domination and control (isolation, monitoring, threats, financial control) that does not always involve physical violence but is deeply harmful.

**Crisis**
A situation where a survivor is in immediate danger to themselves or others (e.g., suicidal ideation, active threat from a perpetrator).

**Crisis Detection**
The AI service's function that flags messages containing crisis signals (e.g., "I don't want to live anymore") and triggers escalation to human support.

**CVSS (Common Vulnerability Scoring System)**
A standard for rating the severity of security vulnerabilities. Version 4.0 is used in `SECURITY.md`.

### D

**De-anonymization**
The process of linking an "anonymous" record back to a specific person. TogetherSafe actively prevents this.

**Disguised Mode**
A privacy feature that hides the TogetherSafe app from the taskbar/process list and changes its visible branding to something neutral (e.g., "System Update"). Designed for shared or unsafe devices.

**Domestic Violence (DV)**
See **Intimate Partner Violence (IPV)**. "Domestic violence" is broader and can include violence between any household members.

### E

**Economic Abuse**
A form of GBV where the perpetrator controls a survivor's finances (denying money, forcing debt, sabotaging employment).

**Emotional Abuse**
A form of GBV involving psychological manipulation, threats, humiliation, or isolation.

**Evidence**
Any material (photo, document, audio, message, medical record) uploaded to a case to support a survivor's account. Always stored with a SHA-256 checksum for integrity.

**Evidence Vault**
The TogetherSafe module where evidence is stored, organized, and shared with explicit survivor consent.

**EXIF Metadata**
Hidden data in photos (GPS coordinates, camera model, timestamp). TogetherSafe strips EXIF data from uploaded images to prevent de-anonymization.

### F

**FGM (Female Genital Mutilation)**
A harmful traditional practice involving partial or total removal of female genitalia. A form of GBV and a crime in Uganda.

**Forced Marriage**
Marriage without the free and full consent of both parties. A form of GBV.

### G

**GBV (Gender-Based Violence)**
Any harmful act perpetrated against a person's will that is based on socially ascribed differences between males and females. Includes physical, sexual, emotional, economic, and structural violence.

**GBV AoR (Area of Responsibility)**
A coordination mechanism under the UN Protection Cluster for GBV response in emergencies.

### H

**Harassment**
Unwanted conduct (verbal, physical, digital) that creates a hostile environment. In GBV context, often refers to sexual harassment or online harassment.

**Harm Reduction**
Approaches that reduce the negative consequences of a behavior without requiring abstinence. Relevant to GBV in supporting survivors who cannot safely leave a perpetrator.

### I

**Informed Consent**
A survivor's explicit, informed agreement for a specific action (e.g., sharing evidence, referring to a service). Must be revocable at any time.

**Intake**
The first stage of a case, where initial information is gathered.

**Intimate Partner Violence (IPV)**
Physical, sexual, or psychological harm by a current or former intimate partner. Often called "domestic violence" in everyday speech.

**IPV (see Intimate Partner Violence)**

### J

**JWT (JSON Web Token)**
The authentication token used by TogetherSafe's API. Never logged; never exposed to third parties.

### L

**LGBTQ+ GBV**
GBV targeting people based on sexual orientation or gender identity. Higher risk of under-reporting due to criminalization and stigma.

### M

**Mandatory Reporting**
A legal obligation for certain professionals (teachers, doctors) to report suspected GBV. TogetherSafe does not auto-report; it supports human judgment.

**MOH (Ministry of Health)**
Uganda's Ministry of Health. Partner in GBV response for medical services.

**MGLSD (Ministry of Gender, Labour, and Social Development)**
Uganda's ministry responsible for GBV policy and the National GBV Database.

### N

**NITA-U (National Information Technology Authority Uganda)**
Uganda's data protection authority.

**NGBVD (National Gender-Based Violence Database)**
Uganda's government platform for structured GBV case data.

### O

**OAuth 2.0**
An authorization framework. TogetherSafe uses it via Keycloak.

**Online Harassment**
GBV that occurs through digital channels (threats, stalking, doxxing, non-consensual sharing of intimate images).

### P

**Perpetrator**
The person who commits GBV. Prefer this term over "abuser" (which can sound minimizing) in formal documentation.

**PHI (Protected Health Information)**
Health data protected by law. In TogetherSafe, any medical details in a case are treated as PHI.

**PIN**
A short numeric code used to lock the TogetherSafe app on a device.

**PII (Personally Identifiable Information)**
Data that can identify a specific person (name, ID, phone, address). Treated as Confidential or Restricted in TogetherSafe.

**Protection Order**
A legal order restraining a perpetrator from contact with a survivor. Also called a "restraining order."

### R

**Referral**
The act of connecting a survivor to a service provider (counselling, legal aid, shelter, medical).

**RBAC (Role-Based Access Control)**
Authorization model where permissions are assigned to roles (ADMIN, CASEWORKER, SURVIVOR).

**Rape**
Non-consensual sexual penetration. A specific form of sexual violence. Use the precise term; never soften.

### S

**Safe House / Shelter**
A secure residential facility for survivors and their children.

**Safeguarding**
Policies and practices that protect vulnerable people (children, at-risk adults) from harm.

**SEA (Sexual Exploitation and Abuse)**
Abuse of power by humanitarian workers. A specific concern in GBV response.

**SGBV (Sexual and Gender-Based Violence)**
A synonym for GBV that emphasizes sexual violence. Used interchangeably with GBV in many contexts.

**SHA-256**
A cryptographic hash function. TogetherSafe uses SHA-256 to verify evidence file integrity.

**SOS**
Emergency button. Instantly visible. One tap connects to emergency contacts.

**Survivor**
A person who has experienced GBV. **Preferred term** over "victim." Emphasizes agency and resilience.

**Survivor-Centred**
An approach that prioritizes the survivor's safety, needs, and choices in every decision.

### T

**Tracking Reference**
A code (e.g., `TS-4K7P-9M2Q`) given to anonymous reporters so they can check case status later. One-way hash; cannot be reverse-engineered.

**Trauma-Informed**
An approach that recognizes the widespread impact of trauma and avoids re-traumatization.

**TLS (Transport Layer Security)**
The encryption protocol for network traffic. TogetherSafe requires TLS 1.3.

### V

**VAW (Violence Against Women)**
A synonym for GBV, focused on women and girls. The UN uses "violence against women and girls (VAWG)."

**Victim**
A person who has experienced a crime. In TogetherSafe, prefer **Survivor** except in legal/medical contexts where "victim" is legally defined.

### W

**WHO (World Health Organization)**
Source of the clinical guidelines for GBV response used by TogetherSafe.

---

## 4. Technical Terms

### A

**API (Application Programming Interface)**
A contract between two systems. TogetherSafe's API is defined in `docs/shared/API_CONTRACT.md`.

**Audit Event**
A record of a state-changing action (who did what, when). Stored in `audit_events` table. Append-only.

**Authentication**
Verifying who someone is (login).

**Authorization**
Verifying what someone is allowed to do (permissions).

### B

**BCrypt**
Password hashing algorithm. Cost factor 10+.

**Backend**
The Spring Boot Java server. Handles business logic, security, and database access.

### C

**CI/CD (Continuous Integration / Continuous Deployment)**
Automated pipeline that runs tests and deploys code. Uses GitHub Actions.

**Checksum**
A hash value used to verify file integrity. TogetherSafe uses SHA-256.

### D

**DAO (Data Access Object)**
See **Repository**.

**DTO (Data Transfer Object)**
An object that carries data between layers. TogetherSafe uses Java records for DTOs.

### E

**Entity**
A Java class mapped to a database table using JPA.

### F

**FastAPI**
A Python web framework used for the AI microservice.

**Flyway**
Database migration tool. Manages versioned SQL scripts.

**FXML**
XML-based markup for JavaFX UIs.

### G

**GDPR (General Data Protection Regulation)**
EU data protection law. Applies if TogetherSafe processes EU data subjects.

### H

**Hibernate**
JPA implementation used by Spring Boot.

### J

**JDBC (Java Database Connectivity)**
The standard Java API for database access.

**JPA (Java Persistence API)**
The standard for ORM in Java.

**JWT (JSON Web Token)**
See GBV domain terms.

### K

**Keycloak**
Open-source identity and access management. Provides OAuth 2.0 and LDAP integration.

### L

**LDAP (Lightweight Directory Access Protocol)**
A directory service protocol. Used by TogetherSafe for organizational user management.

### M

**Maven**
Build and dependency management tool for Java. TogetherSafe uses Maven.

**Microservice**
A small, independently deployable service. TogetherSafe's AI service is a microservice.

### O

**ORM (Object-Relational Mapping)**
Mapping Java objects to database rows. Hibernate is the ORM used.

**OWASP**
Open Worldwide Application Security Project. Publishes the Top 10 and Dependency-Check tool.

### P

**PostgreSQL**
The relational database used by TogetherSafe.

**PR (Pull Request)**
A request to merge changes from one Git branch into another. Required for all changes to `main`.

### R

**RBAC**
See GBV domain terms.

**Repository**
A Spring Data JPA interface for database access. Also: a Git repository.

### S

**SAST (Static Application Security Testing)**
Automated analysis of source code for vulnerabilities.

**SCA (Software Composition Analysis)**
Automated analysis of dependencies for vulnerabilities.

**SemVer (Semantic Versioning)**
Versioning scheme `MAJOR.MINOR.PATCH`. See `CHANGELOG.md`.

**Spring Boot**
Java framework for building backend applications.

**systemd**
Linux init system. Runs TogetherSafe services.

### T

**TestFX**
Framework for UI testing in JavaFX.

**Toast**
A small, transient notification in the UI (top-right corner).

### U

**Uvicorn**
ASGI server for FastAPI.

### W

**WCAG (Web Content Accessibility Guidelines)**
Accessibility standard. TogetherSafe targets WCAG 2.1 AA.

**WHO**
See GBV domain terms.

---

## 5. Acronyms Quick Reference

| Acronym | Meaning |
|---|---|
| API | Application Programming Interface |
| BCrypt | Blowfish Crypt (password hashing) |
| CI/CD | Continuous Integration / Continuous Deployment |
| CVSS | Common Vulnerability Scoring System |
| DAO | Data Access Object |
| DTO | Data Transfer Object |
| DV | Domestic Violence |
| EXIF | Exchangeable Image File Format |
| FGM | Female Genital Mutilation |
| GBV | Gender-Based Violence |
| GDPR | General Data Protection Regulation |
| IPV | Intimate Partner Violence |
| JPA | Java Persistence API |
| JWT | JSON Web Token |
| LDAP | Lightweight Directory Access Protocol |
| LGBTQ+ | Lesbian, Gay, Bisexual, Transgender, Queer/Questioning, and others |
| MGLSD | Ministry of Gender, Labour, and Social Development (Uganda) |
| NGBVD | National Gender-Based Violence Database (Uganda) |
| NITA-U | National Information Technology Authority Uganda |
| OAuth | Open Authorization |
| ORM | Object-Relational Mapping |
| OWASP | Open Worldwide Application Security Project |
| PHI | Protected Health Information |
| PII | Personally Identifiable Information |
| PIN | Personal Identification Number |
| PR | Pull Request |
| RBAC | Role-Based Access Control |
| SAST | Static Application Security Testing |
| SCA | Software Composition Analysis |
| SEA | Sexual Exploitation and Abuse |
| SemVer | Semantic Versioning |
| SGBV | Sexual and Gender-Based Violence |
| SHA | Secure Hash Algorithm |
| SLA | Service-Level Agreement |
| SOS | Save Our Souls (emergency signal) |
| TLS | Transport Layer Security |
| UI | User Interface |
| UX | User Experience |
| VAW | Violence Against Women |
| VAWG | Violence Against Women and Girls |
| WCAG | Web Content Accessibility Guidelines |
| WHO | World Health Organization |

---

## 6. Terms We Do NOT Use (and Why)

| Avoid | Use Instead | Why |
|---|---|---|
| "Victim" (in user-facing text) | "Survivor" | Emphasizes agency; less stigmatizing |
| "Alleged victim" | "Survivor" or "person who reported" | "Alleged" implies doubt; not our role |
| "Domestic dispute" | "Intimate partner violence" | "Dispute" minimizes violence |
| "Sex scandal" | "Sexual assault" or "sexual violence" | "Scandal" implies mutual culpability |
| "Prostitute" | "Sex worker" or "person who trades sex" | "Prostitute" is stigmatizing |
| "Honor killing" | "Femicide" or "so-called honour killing" | "Honour" is not honour |
| "Forced to have sex" | "Raped" or "sexually assaulted" | Be precise; do not soften |
| "She was abused" | "He abused her" (or neutral: "She experienced abuse") | Passive voice hides perpetrator |
| "Child prostitute" | "Sexually exploited child" | Children cannot consent |
| "Lover" / "Boyfriend" (of a minor) | "Abuser" / "Perpetrator" | Do not romanticize abuse of children |
| "Committed suicide" | "Died by suicide" / "Killed themselves" | "Committed" implies crime |
| "Crazy" / "Insane" (about a person) | "Person with a mental health condition" | Stigmatizing |
| "Handicapped" | "Person with a disability" | Person-first language |
| "Drug addict" | "Person with a substance use disorder" | Person-first language |

---

## 7. How to Add a New Term

1. Open a PR with the change.
2. Place the term in the correct section (GBV Domain or Technical).
3. Alphabetize within the section.
4. Include: **term** (bold), then a clear definition.
5. If the term has a preferred synonym or an "avoid" counterpart, note it.
6. Update the Acronyms Quick Reference if applicable.
7. Reference the PR in `CHANGELOG.md`.

---

## The One-Sentence Summary

**GLOSSARY.md defines every GBV and technical term used in TogetherSafe, including preferred survivor-centred language, terms we avoid, and acronyms. Every contributor reads it. New terms are added via PR, never invented in code.**