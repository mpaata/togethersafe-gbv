# API_CONTRACT.md --- Shared Document

**The source of truth for every endpoint. Backend builds it. Frontend calls it.**

## 1. How to Use This Document
- **Backend devs:** implement exactly what is described here.
- **Frontend devs:** call exactly what is described here.
- **Never change an endpoint without updating this file.**
- **Never change this file without team agreement.**
- If the code and this document disagree, this document wins --- until someone updates both.

## 2. Conventions

### Base URL





/api/v1

text

### Authentication
| Client | Method | Header |
|---|---|---|
| Desktop app (JavaFX) | JWT | `Authorization: Bearer <token>` |

### Content Type
Content-Type: application/json
Accept: application/json

text

### Field Naming
camelCase everywhere in JSON: `caseId`, `createdAt`, `reporterId`.

### Dates and Times
- Dates: `YYYY-MM-DD` (e.g., `2026-10-12`)
- Timestamps: ISO 8601 UTC (e.g., `2026-10-12T10:30:00Z`)
- All timestamps stored in UTC.

### HTTP Status Codes
| Code | Meaning |
|---|---|
| 200 | OK --- success |
| 201 | Created --- resource created |
| 204 | No Content --- success, no body |
| 400 | Bad Request --- validation failed |
| 401 | Unauthorized --- not logged in or token expired |
| 403 | Forbidden --- logged in but not allowed |
| 404 | Not Found --- resource doesn't exist |
| 409 | Conflict --- duplicate, state conflict |
| 422 | Unprocessable Entity --- business rule violated |
| 500 | Server Error --- something broke |

## 3. Standard Response Shapes

### Success (single resource)
```json
{
  "data": {
    "id": 1,
    "caseId": "CASE-001"
  }
}
Success (list)
json
{
  "data": [
    { "id": 1, "caseId": "CASE-001" },
    { "id": 2, "caseId": "CASE-002" }
  ]
}
Success (paginated)
json
{
  "data": [ ],
  "page": 0,
  "size": 20,
  "total": 145,
  "totalPages": 8
}
Success (no content)
No body, status 204.

Error
json
{
  "error": {
    "code": "CASE_NOT_FOUND",
    "message": "Case not found.",
    "details": null
  }
}
Validation Error
json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "One or more fields are invalid.",
    "details": {
      "caseType": "must not be null",
      "description": "must not be blank"
    }
  }
}
4. Pagination
Query parameters on list endpoints:

text
?page=0&size=20&sort=createdAt,desc
page --- 0-based page number (default 0)

size --- items per page (default 20, max 100)

sort --- field,direction

5. Authentication Endpoints
POST /auth/login
Log in with username and password. (For admin/caseworker accounts; survivors may use anonymous reporting.)

Auth: none

Request:

json
{
  "username": "caseworker1",
  "password": "secret123"
}
Response 200:

json
{
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresAt": "2026-10-12T18:30:00Z",
    "user": {
      "id": 5,
      "username": "caseworker1",
      "fullName": "Jane Achieng",
      "role": "CASEWORKER",
      "orgId": 1,
      "orgName": "Safe Haven NGO"
    }
  }
}
Errors:

INVALID_CREDENTIALS (401)

ACCOUNT_LOCKED (403) --- after 5 failed attempts

ACCOUNT_INACTIVE (403)

POST /auth/logout
Invalidate the current token.

Auth: required
Response 204 (no body)

GET /auth/me
Get the currently logged-in user.

Auth: required

Response 200:

json
{
  "data": {
    "id": 5,
    "username": "caseworker1",
    "fullName": "Jane Achieng",
    "email": "jane@safehaven.org",
    "phone": "+256700000000",
    "role": "CASEWORKER",
    "orgId": 1
  }
}
POST /auth/change-password
Change your own password.

Auth: required

Request:

json
{
  "currentPassword": "oldpass123",
  "newPassword": "newpass456"
}
Response 204 (no body)

Errors:

INVALID_CREDENTIALS (401) --- current password wrong

WEAK_PASSWORD (400)

6. Case Endpoints
POST /cases
Create a new case. Can be called anonymously (no auth) OR authenticated.

Auth: optional (anonymous reporting allowed)

Request:

json
{
  "caseType": "PHYSICAL_ASSAULT",
  "description": "Physical abuse reported at home.",
  "incidentDate": "2026-10-10",
  "locationSummary": "Kampala, Kawempe",
  "priority": "HIGH",
  "contactMethod": "anonymous"
}
Response 201:

json
{
  "data": {
    "id": 1,
    "caseId": "CASE-2026-0001",
    "trackingReference": "TS-4K7P-9M2Q",
    "status": "OPEN",
    "priority": "HIGH",
    "createdAt": "2026-10-12T10:30:00Z"
  }
}
Notes:

The trackingReference is returned ONLY on creation and is used by anonymous reporters to check status later.

It is a one-way hash of the case ID, so it cannot be guessed.

Errors:

VALIDATION_FAILED (400)

GET /cases
List cases.

Auth: required

Survivor: returns own cases only.

Caseworker: returns assigned cases only.

Admin: returns all cases in the org.

Query parameters:

status --- OPEN / IN_PROGRESS / RESOLVED / CLOSED

priority --- LOW / MEDIUM / HIGH / CRITICAL

search --- matches case ID or location

page, size, sort

Response 200:

json
{
  "data": [
    {
      "id": 1,
      "caseId": "CASE-2026-0001",
      "caseType": "PHYSICAL_ASSAULT",
      "status": "OPEN",
      "priority": "HIGH",
      "locationSummary": "Kampala",
      "assignedCaseworkerId": 5,
      "createdAt": "2026-10-12T10:30:00Z",
      "updatedAt": "2026-10-12T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "total": 1,
  "totalPages": 1
}
GET /cases/{id}
Get full case details.

Auth: required (survivor can only see own; caseworker only assigned; admin all).

Response 200:

json
{
  "data": {
    "id": 1,
    "caseId": "CASE-2026-0001",
    "caseType": "PHYSICAL_ASSAULT",
    "description": "Physical abuse reported at home.",
    "incidentDate": "2026-10-10",
    "locationSummary": "Kampala, Kawempe",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "reporterId": 3,
    "assignedCaseworkerId": 5,
    "createdAt": "2026-10-12T10:30:00Z",
    "updatedAt": "2026-10-12T12:00:00Z"
  }
}
Errors:

CASE_NOT_FOUND (404)

NOT_YOUR_CASE (403) --- survivor accessing another survivor's case

PUT /cases/{id}/status
Update case status (caseworker/admin only).

Auth: CASEWORKER or ADMIN

Request:

json
{
  "status": "IN_PROGRESS",
  "note": "Assigned to Jane, initial contact made."
}
Response 200 (updated case)

Errors:

CASE_NOT_FOUND (404)

NOT_YOUR_CASE (403)

7. Evidence Endpoints
POST /cases/{id}/evidence
Upload evidence for a case.

Auth: required (survivor must own the case; caseworker must be assigned).

Content-Type: multipart/form-data

Request:

file --- the file (PDF, JPG, PNG, MP3, MP4; max 50 MB)

visibility --- PRIVATE / SHARED / PUBLIC_WITHIN_CASE

Response 201:

json
{
  "data": {
    "id": 10,
    "fileName": "photo.jpg",
    "fileType": "image/jpeg",
    "fileSizeBytes": 524288,
    "checksumSha256": "a3f5...e9b2",
    "visibility": "PRIVATE",
    "uploadedAt": "2026-10-12T11:00:00Z"
  }
}
Errors:

FILE_TOO_LARGE (413)

UNSUPPORTED_FILE_TYPE (415)

CASE_NOT_FOUND (404)

NOT_YOUR_CASE (403)

GET /cases/{id}/evidence
List evidence for a case.

Auth: required

Response 200:

json
{
  "data": [
    {
      "id": 10,
      "fileName": "photo.jpg",
      "fileType": "image/jpeg",
      "checksumSha256": "a3f5...e9b2",
      "visibility": "PRIVATE",
      "sharedWith": [
        { "userId": 5, "fullName": "Jane Achieng", "sharedAt": "2026-10-12T11:30:00Z" }
      ],
      "uploadedAt": "2026-10-12T11:00:00Z"
    }
  ]
}
POST /evidence/{id}/share
Explicitly share evidence with a specific user.

Auth: required (survivor must own the evidence).

Request:

json
{
  "sharedWithUserId": 5,
  "notes": "Sharing for case review."
}
Response 201:

json
{
  "data": {
    "shareId": 20,
    "evidenceId": 10,
    "sharedWithUserId": 5,
    "sharedAt": "2026-10-12T11:30:00Z"
  }
}
Errors:

EVIDENCE_NOT_FOUND (404)

NOT_YOUR_EVIDENCE (403)

DELETE /evidence/{id}/share/{shareId}
Revoke a share.

Auth: required (survivor must own the evidence).

Response 204 (no body)

8. Referral Endpoints
POST /referrals
Create a referral.

Auth: CASEWORKER or ADMIN

Request:

json
{
  "caseId": 1,
  "serviceId": 3,
  "notes": "Referred for counselling."
}
Response 201 (created referral)

GET /referrals
List referrals.

Auth: required (scoped to user)

Query parameters:

caseId, status, page, size

Response 200 (paginated list)

PUT /referrals/{id}/status
Update referral status.

Auth: CASEWORKER or ADMIN

Request:

json
{
  "status": "ACCEPTED",
  "responseNotes": "Intake scheduled for 2026-10-15."
}
Response 200

9. Service Directory Endpoints
GET /services
List verified services.

Auth: required

Query parameters:

category --- COUNSELLING / POLICE / MEDICAL / SHELTER / LEGAL

search

page, size

Response 200:

json
{
  "data": [
    {
      "id": 3,
      "name": "Safe Haven Counselling Centre",
      "category": "COUNSELLING",
      "location": "Kampala",
      "latitude": 0.3476,
      "longitude": 32.5825,
      "contactPhone": "+256700123456",
      "isVerified": true
    }
  ]
}
GET /services/{id}
Get service details.

Auth: required

10. AI Endpoints
POST /ai/chat
Send a message to the AI assistant.

Auth: required

Request:

json
{
  "sessionId": "abc-123",
  "message": "I'm scared and don't know what to do.",
  "language": "en"
}
Response 200:

json
{
  "data": {
    "reply": "Your safety comes first. Here are options you can consider...",
    "crisisDetected": false,
    "suggestedResources": ["counselling", "safety-planning"],
    "disclaimer": "I am an AI assistant. If you are in immediate danger, call 999."
  }
}
POST /ai/risk-score
(Internal, used by backend) Score case urgency.

Auth: required (SYSTEM or ADMIN role)

Request:

json
{
  "caseDescription": "...",
  "caseType": "PHYSICAL_ASSAULT",
  "hasChildren": true
}
Response 200:

json
{
  "data": {
    "riskLevel": "HIGH",
    "confidence": 0.82,
    "factors": ["threat to life", "repeat incident", "children involved"]
  }
}
11. Admin Endpoints
GET /admin/dashboard
Admin summary.

Auth: ADMIN

Response 200:

json
{
  "data": {
    "openCases": 12,
    "urgentCases": 3,
    "resolvedThisMonth": 8,
    "totalSurvivors": 20,
    "pendingReferrals": 5
  }
}
GET /admin/users
List users.

Auth: ADMIN

Query parameters: role, active, search, page, size

Response 200 (paginated list)

POST /admin/users
Create a user.

Auth: ADMIN

Request:

json
{
  "username": "caseworker2",
  "password": "initialpass123",
  "fullName": "John Okello",
  "email": "john@safehaven.org",
  "phone": "+256700000001",
  "role": "CASEWORKER"
}
Response 201

Errors:

USERNAME_TAKEN (409)

VALIDATION_FAILED (400)

GET /admin/audit
View audit log.

Auth: ADMIN

Query parameters: userId, action, entityType, from, to, page, size

Response 200 (paginated list)

12. Error Codes --- Full List
Full catalog in ERROR_CODES.md.

13. Notes for Frontend Devs (JavaFX)
After login, store the JWT and expiresAt in Session.

Add Authorization: Bearer <token> to every subsequent request.

On 401, redirect to login.

On 403, show a permission denied message.

On VALIDATION_FAILED, use the details object to highlight specific fields.

Never log the JWT to the console.

14. Notes for Backend Devs
Every endpoint requires @Valid on request bodies.

Every service method validates permission server-side.

Every state change writes to audit_events.

Every list endpoint supports pagination.

Every entity response is a DTO, never the raw entity.

Every error uses the standard shape.

Never expose password_hash in any response.

Never trust orgId from request body --- always derive it from the authenticated session.

The One-Sentence Summary
API_CONTRACT.md is the source of truth for every endpoint --- path, method, request, response, errors. Backend implements it exactly. Frontend calls it exactly. Nothing changes without updating both.s