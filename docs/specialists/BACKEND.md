# BACKEND.md --- Backend Lead Specialist Document

## 1. Who This Is For
You are the **Backend Lead** on TogetherSafe.
You build the brain of the system --- all server-side Java code and REST APIs.

You work alongside:
- **JavaFX Developer** --- uses your REST API
- **Database + DevOps** --- owns schema migrations and deployment
- **AI Service Developer** --- your API calls the Python AI microservice
- **Product + QA** --- tests your endpoints

You own the backend. You do not edit the desktop app or the Python AI code.

## 2. What You Build in v1
- REST API for the desktop app (`/api/v1/**`)
- Service layer (business logic)
- Repository layer (database access via Spring Data JPA)
- JPA entities (map Java classes to tables)
- DTOs (request/response shapes)
- Mappers (entity ↔ DTO)
- Security (OAuth 2.0 / LDAP via Keycloak, JWT for desktop)
- Exception handling
- Audit logging for every state change
- Evidence file handling (upload, checksum, storage)
- Tests for all of the above

**Not in v1:** Analytics, email notifications, sharing, offline sync. Those are v2+.

## 3. Package Structure
```text
backend/src/main/java/com/togethersafe/backend/
|
|-- TogetherSafeApplication.java
|
|-- config/
|   |-- SecurityConfig.java
|   |-- JwtConfig.java
|   |-- KeycloakConfig.java
|   |-- WebConfig.java
|   |-- JacksonConfig.java
|   +-- OpenApiConfig.java
|
|-- security/
|   |-- JwtTokenProvider.java
|   |-- JwtAuthenticationFilter.java
|   |-- CustomUserDetailsService.java
|   |-- RateLimiter.java
|   +-- CurrentUser.java
|
|-- controller/
|   |-- AuthController.java
|   |-- CaseController.java
|   |-- EvidenceController.java
|   |-- ReferralController.java
|   |-- ServiceController.java
|   |-- AdminUserController.java
|   |-- AuditLogController.java
|   +-- AiController.java
|
|-- service/
|   |-- AuthService.java
|   |-- UserService.java
|   |-- CaseService.java
|   |-- EvidenceService.java
|   |-- ReferralService.java
|   |-- ServiceDirectoryService.java
|   |-- AuditService.java
|   |-- FileStorageService.java
|   +-- AiClientService.java
|
|-- repository/
|   |-- UserRepository.java
|   |-- CaseRepository.java
|   |-- EvidenceRepository.java
|   |-- ReferralRepository.java
|   |-- ServiceRepository.java
|   |-- CaseNoteRepository.java
|   +-- AuditEventRepository.java
|
|-- entity/
|   |-- User.java
|   |-- Case.java
|   |-- Evidence.java
|   |-- Referral.java
|   |-- Service.java
|   |-- CaseNote.java
|   |-- AuditEvent.java
|   +-- enums/
|       |-- Role.java
|       |-- CaseStatus.java
|       |-- CaseType.java
|       |-- ReferralStatus.java
|       +-- AuditAction.java
|
|-- dto/
|   |-- request/
|   |   |-- LoginRequest.java
|   |   |-- CreateCaseRequest.java
|   |   |-- UploadEvidenceRequest.java
|   |   |-- CreateReferralRequest.java
|   |   +-- UpdateCaseStatusRequest.java
|   +-- response/
|       |-- LoginResponse.java
|       |-- UserResponse.java
|       |-- CaseResponse.java
|       |-- EvidenceResponse.java
|       |-- ReferralResponse.java
|       |-- ServiceResponse.java
|       +-- ApiResponse.java
|
|-- mapper/
|   |-- UserMapper.java
|   |-- CaseMapper.java
|   |-- EvidenceMapper.java
|   |-- ReferralMapper.java
|   +-- ServiceMapper.java
|
|-- exception/
|   |-- GlobalExceptionHandler.java
|   |-- ResourceNotFoundException.java
|   |-- ValidationException.java
|   |-- BusinessException.java
|   |-- PermissionDeniedException.java
|   |-- AuthenticationException.java
|   +-- ErrorCode.java
|
+-- util/
    |-- DateUtils.java
    |-- FileHashUtil.java
    |-- SecurityUtils.java
    +-- Constants.java



   4. Layer Responsibilities
Controllers
Accept JSON, return JSON

Use @Valid on request bodies

Delegate to services --- no business logic

Never touch repositories directly

JWT authentication via header

Return ApiResponse<T> wrapper

Services
All business logic lives here

Transaction boundaries (@Transactional)

Validate inputs

Check permissions server-side

Write audit logs for every state change

Throw custom exceptions

Return DTOs, not entities

Repositories
Spring Data JPA interfaces

Standard CRUD inherited from JpaRepository

Custom finders via method names (findByCaseId)

Complex queries via @Query

No business logic

Entities
Map to database tables

JPA annotations (@Entity, @Table, @Column)

Relationships (@ManyToOne, @OneToMany)

Lombok for boilerplate (@Data, @NoArgsConstructor)

Auditing annotations (@CreatedDate, @LastModifiedDate)

DTOs
Use Java records (immutable, concise)

Validation annotations on request DTOs

No JPA annotations

No business logic

Mappers
Convert entity ↔ DTO

One mapper class per entity

Static methods for simplicity

5. Code Conventions
Entities
java
@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", length = 30, nullable = false)
    private CaseType caseType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private CaseStatus status = CaseStatus.OPEN;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
Rules:

@Table always specifies the table name explicitly

@Column always specifies name and constraints

Enums use @Enumerated(EnumType.STRING) --- never ORDINAL

Lazy loading on @ManyToOne

nullable and length always set

Timestamps: created_at is updatable = false

Services
java
@Service
@RequiredArgsConstructor
@Slf4j
public class CaseService {

    private final CaseRepository caseRepository;
    private final AuditService auditService;

    @Transactional
    public CaseResponse createCase(CreateCaseRequest request, Long currentUserId) {
        Case newCase = new Case();
        newCase.setReporter(userRepository.getReferenceById(currentUserId));
        newCase.setCaseType(request.caseType());
        newCase.setDescription(request.description());
        newCase.setCreatedAt(LocalDateTime.now());

        Case saved = caseRepository.save(newCase);

        auditService.log(AuditAction.CASE_CREATED, "Case", saved.getId(), currentUserId);

        log.info("Case {} created by user {}", saved.getId(), currentUserId);

        return CaseMapper.toResponse(saved);
    }
}
Rules:

Constructor injection via @RequiredArgsConstructor

@Transactional on write methods

Permission checks before business logic

Validation with meaningful errors

Audit log for every state change

Log at INFO for business events, DEBUG for detail

Return DTOs, not entities

Controllers
java
@RestController
@RequestMapping("/api/v1/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @PostMapping
    public ApiResponse<CaseResponse> createCase(
            @Valid @RequestBody CreateCaseRequest request,
            @CurrentUser Long currentUserId) {
        CaseResponse response = caseService.createCase(request, currentUserId);
        return ApiResponse.success(response);
    }
}
Rules:

@RestController for API

@RequestMapping at class level for base path

@Valid on request bodies

Return ApiResponse<T> wrapper

Delegate everything to services

Use @CurrentUser custom annotation to inject logged-in user id

DTOs (Records)
java
public record CreateCaseRequest(
    @NotNull CaseType caseType,
    @NotBlank @Size(max = 2000) String description,
    @Size(max = 200) String locationSummary
) {}
Exceptions
java
public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public ResourceNotFoundException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }
    public ErrorCode getErrorCode() { return errorCode; }
}
Global handler:

java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.getErrorCode()));
    }
}
6. Security
OAuth 2.0 / LDAP via Keycloak
All users authenticate through Keycloak.

Keycloak is configured with LDAP as the user federation provider.

The backend validates the JWT issued by Keycloak.

Claims include: userId, role, schoolId.

Passwords
BCrypt with cost factor 10

Stored in users.password_hash

Never logged, never returned in responses

Permission Checks
Always server-side

Role check + resource ownership check

Example: A survivor can only view their own case.

java
if (!aCase.getReporter().getId().equals(currentUserId)) {
    throw new PermissionDeniedException(ErrorCode.NOT_YOUR_CASE);
}
Audit Logging
Every state change is logged.

Fields: actor, action, entity type, entity id, timestamp.

Sensitive data (evidence content, full case narrative) is never written to the log.

7. Evidence Handling (Critical for GBV)
Files are stored on disk (not in the database).

The database stores metadata: filename, type, checksum (SHA-256), storage path.

Uploads are validated: file type, file size.

Evidence sharing requires explicit survivor consent (via a ShareLink or permission record).

Every access to evidence is logged.

8. Testing
Unit tests: JUnit 5 + Mockito for services.

Integration tests: @SpringBootTest + Testcontainers for PostgreSQL.

Naming: should<Behavior>When<Condition>()

java
@Test
@DisplayName("should throw when survivor accesses another user's case")
void shouldThrowWhenSurvivorAccessesAnotherUsersCase() { ... }
9. Definition of Done (Backend)
A backend task is done when:

□ Code follows MASTER.md and BACKEND.md conventions.
□ Unit tests written and passing.
□ Integration test written if it crosses layers.
□ Error cases handled with standard error codes.
□ Audit log written for state changes.
□ Permission check enforced server-side.
□ Input validation via @Valid.
□ API contract updated in API_CONTRACT.md.
□ No hardcoded values (use config).
□ No secrets in code.
□ PR reviewed and approved.
10. What NOT to Do
Don't put business logic in controllers.

Don't expose entities directly in API responses --- use DTOs.

Don't skip permission checks.

Don't write SQL strings manually --- use JPA.

Don't use @Enumerated(EnumType.ORDINAL) --- always STRING.

Don't log passwords, tokens, or full PII.

Don't hardcode values --- use application.yml or environment variables.

Don't write to the database outside a @Transactional method. 