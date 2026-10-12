# MASTER.md --- TogetherSafe

## 1. Project Overview
**TogetherSafe** is a secure, survivor-centered desktop application for Gender-Based Violence (GBV) case management, evidence vault, and survivor support. It is designed for use by authorized caseworkers, administrators, and survivors themselves.

**v1 Goal:** A secure JavaFX desktop application that allows a survivor to report a case, upload evidence, and track their status, while giving administrators a dashboard to manage cases and referrals.

## 2. Stack
| Layer | Technology |
|---|---|
| Language | Java 25 |
| Desktop UI | JavaFX 27 + FXML + CSS |
| Backend API | Spring Boot 3.2.x |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security + OAuth 2.0 / LDAP (Keycloak) |
| AI Service | Python FastAPI (Custom NLP, Risk Scoring) |
| Build | Maven |
| Testing | JUnit 5, TestFX, Mockito |
| Deployment | jpackage (Windows, macOS, Linux) |

## 3. Non-Negotiable Design Rules
1. **Survivor-Centered:** Safety, privacy, and dignity are paramount. The UI must be calm, clear, and trauma-informed.
2. **Security First:** Role-based access control (RBAC) is enforced server-side. Evidence is explicitly shared, never by default.
3. **AI Boundaries:** The AI provides first-line guidance and resource signposting. It is *not* a replacement for emergency services or professional care.
4. **Privacy by Design:** Disguised mode, minimal data collection, and strict audit logging.
5. **Desktop Only:** v1 is a JavaFX desktop application. No web client.
6. **Audit Log for State Changes:** Every case update, evidence upload, and permission change is logged.

## 4. Repository Structure
```text
togethersafe-gbv/
├── docs/
│   ├── master/
│   │   └── MASTER.md
│   ├── specialists/
│   │   ├── BACKEND.md
│   │   ├── DESKTOP.md
│   │   ├── DATABASE.md
│   │   ├── DEVOPS.md
│   │   ├── AI_SERVICE.md
│   │   └── PRODUCT_QA.md
│   └── shared/
│       ├── API_CONTRACT.md
│       ├── SCHEMA.md
│       ├── ERROR_CODES.md
│       └── DESIGN_SYSTEM.md
├── backend/          (Spring Boot API)
├── desktop/          (JavaFX Application)
├── ai-service/       (Python FastAPI)
├── pom.xml
├── docker-compose.yml
├── README.md
├── ROADMAP.md
└── ARCHITECTURE.md