# TogetherSafe
**Survivor Support & Gender-Based Violence (GBV) Case Management Platform**

TogetherSafe is a secure, desktop-based platform designed to support survivors of Gender-Based Violence (GBV) and facilitate case management for authorized administrators. The system prioritizes survivor safety, privacy, dignity, and control, while providing administrators with structured tools for case review, referrals, and evidence management.

## 🎯 Project Overview
- **Type:** Desktop Application (Survivor/User and Administrator experiences)
- **Language:** Java (JDK 25+)
- **UI Framework:** JavaFX 27
- **Backend:** Spring Boot (REST API)
- **Database:** PostgreSQL
- **Authentication:** Keycloak (OAuth 2.0 / LDAP)
- **AI Microservice:** Python (FastAPI)
- **Build Tool:** Maven
- **Testing:** JUnit 5, TestFX
- **Status:** Design and Requirements Phase (October 2026)

## 📂 Project Structure & Modules
The system is divided into two primary interfaces: **Survivor/User** and **Administrator/Case Officer**, with the following modules:

1. **Public Portal:** Home, About, Impact Dashboard, Sponsor Recognition, Resources Library.
2. **Emergency & Safety:** SOS Center, Safety Planning, Disguised Mode Settings.
3. **AI Chatbot & Support:** AI Chat Interface, Language Selection, Crisis Resources.
4. **Evidence Vault:** My Cases, Upload Evidence, Share Links, Legal Guide.
5. **Services & Case Management:** Find Help Map, Service Directory, Referral History, Case Tracker.
6. **Healing & Wellbeing:** Course Library, 1:1 Messaging, Forums, Grounding Exercises, Story Library.
7. **Community & Volunteers:** Champion Portal, Volunteer Sign-Up, Referral Logging.
8. **Account & Admin:** Admin Dashboard, Settings, Privacy Controls, Notification Preferences, Security (PIN), Audit Logs.

## 🛡️ Core Principles
- **Survivor-Centered:** Dignity, choice, safety, and privacy are paramount.
- **Security First:** Role-based access control (RBAC), audit logging, secure password hashing, and data minimization.
- **AI Boundaries:** The AI assistant provides first-line guidance and resource signposting but is *not* a replacement for emergency services or professional care.
- **Privacy by Design:** Disguised mode, explicit evidence sharing controls, and minimal data collection.

## 🚀 Getting Started (Development)
*To be updated once the codebase is initialized.*

1. Ensure **JDK 25** or later is installed.
2. Ensure **Maven** is installed.
3. Clone the repository:
   ```bash
   git clone https://github.com/mpaata/togethersafe-gbv.git