# TogetherSafe Architecture & Technology Stack

## 1. Technology Stack
| Component | Technology | Role |
| :--- | :--- | :--- |
| **Desktop UI** | JavaFX (JDK 25+) | Cross-platform desktop interface (Survivor & Admin). |
| **Build Tool** | Maven | Dependency management, JavaFX packaging, TestFX setup. |
| **Testing** | JUnit 5 + TestFX | Unit testing, UI integration testing. |
| **Backend API** | Java Spring Boot | REST API, Security, Database ORM (Hibernate/JPA). |
| **Database** | PostgreSQL | Centralized, multi-user, secure relational database. |
| **Auth** | Keycloak (OAuth2 / LDAP) | Centralized identity management, Single Sign-On (SSO). |
| **AI Engine** | Python (FastAPI) | Custom NLP, risk scoring, chatbot logic. |
| **Native Integration** | JNA (Java Native Access) | OS-level window hiding, taskbar manipulation (Disguised Mode). |

## 2. System Architecture
The application follows a **client-server architecture** with a microservice for AI:

[ JavaFX Desktop App (Client) ]
       | (REST / HTTPS)
[ Spring Boot Backend API ] <--> [ Keycloak (OAuth2 / LDAP) ]
       | (JDBC / JPA)
[ PostgreSQL Database ]
       |
       | (REST)
[ Python AI Microservice (FastAPI) ]

## 3. Layered Architecture (Backend)
1. **Presentation Layer:** JavaFX controls, FXML, CSS.
2. **Application/Service Layer:** Spring Boot controllers, services, DTOs.
3. **Domain Layer:** JPA Entities (Case, User, Evidence, Referral).
4. **Data Access Layer:** Spring Data JPA Repositories.
5. **Persistence Layer:** PostgreSQL.
6. **Integration Layer:** REST clients for AI and external services.

## 4. Disguised Mode Implementation
- **Windows:** Use JNA to modify the window style (removing taskbar presence) and launch the JVM with a masked process name.
- **macOS/Linux:** Use wrapper shell scripts to rename the process ID.
- *Warning:* OS-level hiding may trigger antivirus software. This must be an opt-in "Paranoid Mode" with clear instructions for the user.

## 5. Cross-Platform Deployment
- **Windows:** `.msi` or `.exe` via `jpackage`.
- **macOS:** `.dmg` or `.pkg` via `jpackage`.
- **Linux:** `.deb` or `.rpm` via `jpackage`.

## 6. Folder Structure (Suggested)
```text
togethersafe-gbv/
├── src/main/java/com/togethersafe/
│   ├── App.java
│   ├── controller/
│   ├── service/
│   ├── model/
│   ├── repository/
│   ├── security/
│   ├── util/
│   └── integration/
├── src/main/resources/
│   ├── fxml/
│   ├── css/
│   ├── images/
│   └── config/
├── src/test/java/
├── ai-service/          (Python FastAPI microservice)
├── docker-compose.yml   (PostgreSQL + Keycloak)
├── pom.xml
├── README.md
├── ROADMAP.md
└── ARCHITECTURE.md