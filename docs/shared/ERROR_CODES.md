# ERROR_CODES.md --- Shared Document

## 1. How to Use This Document
- **Backend devs:** throw exceptions with the codes defined here.
- **Frontend devs:** handle the codes you receive.
- **QA:** verify correct codes are returned in error cases.
- **Never invent an error code** without adding it here first.
- **Never change a message** without updating both the code and this file.
- Every error response follows the standard shape defined in `API_CONTRACT.md`.

## 2. Error Code Rules

### Naming
- `UPPER_SNAKE_CASE`
- Descriptive --- the code alone should tell you what happened
- Grouped by domain (auth, cases, evidence, etc.)

### Messages
- Human-readable, plain English
- Start with a capital letter
- End with a period
- No exclamation marks
- No technical jargon
- No stack traces or database names
- Use `{placeholders}` for dynamic values

### Details
- Optional field for extra context
- Use for: field-level validation, extra params
- Always an object, never a string

### Structure
Every error entry has:
- **Code** --- the exact string
- **HTTP status** --- the response status
- **Message** --- the message returned to the client
- **Details** --- optional extra data
- **When thrown** --- the condition
- **How to handle (frontend)** --- what the UI should do

## 3. Authentication Errors

### INVALID_CREDENTIALS
- **HTTP:** 401
- **Message:** "Invalid username or password."
- **Details:** null
- **When thrown:** Username does not exist, OR password is wrong.
- **Note:** Never reveal which one failed --- prevents user enumeration.
- **Frontend:** Show message under the login form.

---

### ACCOUNT_LOCKED
- **HTTP:** 403
- **Message:** "Your account is locked. Try again in {minutes} minutes."
- **Details:** `{ "minutes": 15 }`
- **When thrown:** 5 failed login attempts within 15 minutes.
- **Frontend:** Show message with the remaining lock time. Disable the login button until time expires.

---

### ACCOUNT_INACTIVE
- **HTTP:** 403
- **Message:** "Your account has been deactivated. Contact an administrator."
- **Details:** null
- **When thrown:** User exists but `is_active = false`.
- **Frontend:** Show message. Do not allow login.

---

### SESSION_EXPIRED
- **HTTP:** 401
- **Message:** "Your session has expired. Please log in again."
- **Details:** null
- **When thrown:** JWT expired.
- **Frontend:** Clear session, redirect to login.

---

### WEAK_PASSWORD
- **HTTP:** 400
- **Message:** "Password must be at least 8 characters and include a number."
- **Details:** `{ "requirements": ["minLength:8", "containsNumber"] }`
- **When thrown:** New password does not meet policy.
- **Frontend:** Show requirements inline with the password field.

## 4. User Errors

### USER_NOT_FOUND
- **HTTP:** 404
- **Message:** "User not found."
- **When thrown:** Requested user ID does not exist.
- **Frontend:** Show a "not found" page or redirect to list.

---

### USERNAME_TAKEN
- **HTTP:** 409
- **Message:** "This username is already taken."
- **Details:** `{ "username": "caseworker1" }`
- **When thrown:** Creating a user with a username that exists in the same org.
- **Frontend:** Highlight the username field, show message below.

---

### CANNOT_DEACTIVATE_SELF
- **HTTP:** 422
- **Message:** "You cannot deactivate your own account."
- **When thrown:** Admin tries to deactivate themselves.
- **Frontend:** Disable the deactivate button for the current user.

## 5. Case Errors

### CASE_NOT_FOUND
- **HTTP:** 404
- **Message:** "Case not found."
- **When thrown:** Requested case ID does not exist.
- **Frontend:** Redirect to the case list.

---

### NOT_YOUR_CASE
- **HTTP:** 403
- **Message:** "You do not have access to this case."
- **When thrown:** A survivor tries to access another survivor's case, OR a caseworker tries to access an unassigned case.
- **Frontend:** Show a permission denied message. Do not reveal the case exists.

---

### INVALID_CASE_STATUS_TRANSITION
- **HTTP:** 422
- **Message:** "Cannot change status from {from} to {to}."
- **Details:** `{ "from": "CLOSED", "to": "OPEN" }`
- **When thrown:** Trying to reopen a closed case without admin permission.
- **Frontend:** Show the current status. Explain the allowed transitions.

---

### CASE_ALREADY_CLOSED
- **HTTP:** 409
- **Message:** "This case is already closed."
- **When thrown:** Trying to modify a closed case.
- **Frontend:** Disable edit buttons on closed cases.

## 6. Evidence Errors

### EVIDENCE_NOT_FOUND
- **HTTP:** 404
- **Message:** "Evidence not found."
- **When thrown:** Requested evidence ID does not exist.
- **Frontend:** Refresh the evidence list.

---

### NOT_YOUR_EVIDENCE
- **HTTP:** 403
- **Message:** "You do not have permission to access this evidence."
- **When thrown:** A survivor tries to share/delete evidence they do not own.
- **Frontend:** Show a permission denied message.

---

### FILE_TOO_LARGE
- **HTTP:** 413
- **Message:** "File size exceeds the {max} MB limit."
- **Details:** `{ "max": 50 }`
- **When thrown:** Upload exceeds size limit.
- **Frontend:** Show message. Prevent submission.

---

### UNSUPPORTED_FILE_TYPE
- **HTTP:** 415
- **Message:** "Only {types} files are supported."
- **Details:** `{ "types": [".pdf", ".jpg", ".png", ".mp3", ".mp4"] }`
- **When thrown:** Wrong file type uploaded.
- **Frontend:** Show message. Filter file picker.

---

### EVIDENCE_ALREADY_SHARED
- **HTTP:** 409
- **Message:** "This evidence is already shared with this user."
- **When thrown:** Creating a duplicate share.
- **Frontend:** Show existing share. Offer to revoke first.

## 7. Referral Errors

### REFERRAL_NOT_FOUND
- **HTTP:** 404
- **Message:** "Referral not found."
- **When thrown:** Requested referral ID does not exist.
- **Frontend:** Refresh the referral list.

---

### INVALID_REFERRAL_STATUS
- **HTTP:** 422
- **Message:** "Cannot change referral status from {from} to {to}."
- **Details:** `{ "from": "COMPLETED", "to": "PENDING" }`
- **When thrown:** Trying to reopen a completed referral.
- **Frontend:** Show the current status. Explain allowed transitions.

## 8. Service Errors

### SERVICE_NOT_FOUND
- **HTTP:** 404
- **Message:** "Service not found."
- **When thrown:** Requested service ID does not exist.
- **Frontend:** Refresh the service list.

---

### SERVICE_NOT_VERIFIED
- **HTTP:** 422
- **Message:** "This service is not yet verified."
- **When thrown:** Trying to create a referral to an unverified service.
- **Frontend:** Show a warning. Only allow verified services.

## 9. AI Errors

### AI_SERVICE_UNAVAILABLE
- **HTTP:** 503
- **Message:** "The AI assistant is temporarily unavailable. Please try again shortly."
- **When thrown:** Python AI service is down or timed out.
- **Frontend:** Show message. Offer to retry. Provide crisis resources anyway.

---

### AI_CRISIS_DETECTED
- **HTTP:** 200 (special case --- this is a successful response with a flag)
- **Message:** Included in the response body: `"crisisDetected": true`
- **When thrown:** AI detects suicidal ideation or immediate danger.
- **Frontend:** Immediately display crisis resources prominently. Do NOT hide behind a modal.

---

### AI_INVALID_RESPONSE
- **HTTP:** 500
- **Message:** "The AI assistant returned an invalid response."
- **When thrown:** AI service returned malformed data.
- **Frontend:** Show a generic error. Log for the developer.

## 10. Validation Errors

### VALIDATION_FAILED
- **HTTP:** 400
- **Message:** "One or more fields are invalid."
- **Details:** `{ "fieldName": "error message", ... }`
- **When thrown:** Any bean validation failure (`@Valid`).
- **Frontend:** Show field-level errors below the corresponding inputs.

Example details:
```json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "One or more fields are invalid.",
    "details": {
      "caseType": "must not be null",
      "description": "must not be blank",
      "incidentDate": "must be in the past"
    }
  }
}



11. System Errors
INTERNAL_ERROR
HTTP: 500

Message: "Something went wrong. Please try again."

Details: null (never expose the stack trace to the client)

When thrown: Any unhandled exception.

Note: Log the actual exception server-side with a request ID.

Frontend: Show generic message. Include the request ID if provided.

SERVICE_UNAVAILABLE
HTTP: 503

Message: "The service is temporarily unavailable. Please try again shortly."

When thrown: Database down, dependency failure, or maintenance.

Frontend: Show message. Optionally auto-retry.

RATE_LIMIT_EXCEEDED
HTTP: 429

Message: "Too many requests. Please slow down."

Details: { "retryAfter": 60 }

When thrown: Rate limit exceeded.

Frontend: Show message. Wait before retrying.

12. Error Code Index
Code	HTTP	Domain
INVALID_CREDENTIALS	401	Auth
ACCOUNT_LOCKED	403	Auth
ACCOUNT_INACTIVE	403	Auth
SESSION_EXPIRED	401	Auth
WEAK_PASSWORD	400	Auth
USER_NOT_FOUND	404	Users
USERNAME_TAKEN	409	Users
CANNOT_DEACTIVATE_SELF	422	Users
CASE_NOT_FOUND	404	Cases
NOT_YOUR_CASE	403	Cases
INVALID_CASE_STATUS_TRANSITION	422	Cases
CASE_ALREADY_CLOSED	409	Cases
EVIDENCE_NOT_FOUND	404	Evidence
NOT_YOUR_EVIDENCE	403	Evidence
FILE_TOO_LARGE	413	Evidence
UNSUPPORTED_FILE_TYPE	415	Evidence
EVIDENCE_ALREADY_SHARED	409	Evidence
REFERRAL_NOT_FOUND	404	Referrals
INVALID_REFERRAL_STATUS	422	Referrals
SERVICE_NOT_FOUND	404	Services
SERVICE_NOT_VERIFIED	422	Services
AI_SERVICE_UNAVAILABLE	503	AI
AI_CRISIS_DETECTED	200	AI
AI_INVALID_RESPONSE	500	AI
VALIDATION_FAILED	400	Validation
INTERNAL_ERROR	500	System
SERVICE_UNAVAILABLE	503	System
RATE_LIMIT_EXCEEDED	429	System
Total: 28 codes.

13. Backend Implementation
ErrorCode enum
java
package com.togethersafe.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Auth
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password."),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "Your account is locked. Try again in {minutes} minutes."),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN, "Your account has been deactivated. Contact an administrator."),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "Your session has expired. Please log in again."),
    WEAK_PASSWORD(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters and include a number."),

    // Users
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    USERNAME_TAKEN(HttpStatus.CONFLICT, "This username is already taken."),
    CANNOT_DEACTIVATE_SELF(HttpStatus.UNPROCESSABLE_ENTITY, "You cannot deactivate your own account."),

    // Cases
    CASE_NOT_FOUND(HttpStatus.NOT_FOUND, "Case not found."),
    NOT_YOUR_CASE(HttpStatus.FORBIDDEN, "You do not have access to this case."),
    INVALID_CASE_STATUS_TRANSITION(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot change status from {from} to {to}."),
    CASE_ALREADY_CLOSED(HttpStatus.CONFLICT, "This case is already closed."),

    // Evidence
    EVIDENCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Evidence not found."),
    NOT_YOUR_EVIDENCE(HttpStatus.FORBIDDEN, "You do not have permission to access this evidence."),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds the {max} MB limit."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only {types} files are supported."),
    EVIDENCE_ALREADY_SHARED(HttpStatus.CONFLICT, "This evidence is already shared with this user."),

    // Referrals
    REFERRAL_NOT_FOUND(HttpStatus.NOT_FOUND, "Referral not found."),
    INVALID_REFERRAL_STATUS(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot change referral status from {from} to {to}."),

    // Services
    SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "Service not found."),
    SERVICE_NOT_VERIFIED(HttpStatus.UNPROCESSABLE_ENTITY, "This service is not yet verified."),

    // AI
    AI_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "The AI assistant is temporarily unavailable. Please try again shortly."),
    AI_INVALID_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "The AI assistant returned an invalid response."),

    // Validation
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "One or more fields are invalid."),

    // System
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "The service is temporarily unavailable. Please try again shortly."),
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Too many requests. Please slow down.");

    private final HttpStatus status;
    private final String message;
}
Custom exceptions
java
public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public ResourceNotFoundException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ResourceNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }
    // getters
}
Global exception handler
java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return build(ex.getErrorCode(), ex.getDetails());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(ValidationException ex) {
        return build(ex.getErrorCode(), ex.getDetails());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handlePermission(PermissionDeniedException ex) {
        return build(ex.getErrorCode(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnknown(Exception ex) {
        log.error("Unhandled exception", ex);
        return build(ErrorCode.INTERNAL_ERROR, null);
    }

    private ResponseEntity<ApiResponse<?>> build(ErrorCode code, Map<String, Object> details) {
        return ResponseEntity.status(code.getStatus())
            .body(ApiResponse.error(code, details));
    }
}
14. Frontend Handling (JavaFX)
java
try {
    caseService.updateStatus(caseId, request);
} catch (ApiException ex) {
    switch (ex.getCode()) {
        case "NOT_YOUR_CASE" -> {
            Toast.error("You do not have access to this case.");
            SceneManager.switchTo("my-cases");
        }
        case "VALIDATION_FAILED" -> {
            // ex.getDetails() contains field-level errors
            showFieldErrors(ex.getDetails());
        }
        case "SESSION_EXPIRED" -> {
            Session.clear();
            SceneManager.switchTo("login");
        }
        default -> Toast.error(ex.getMessage());
    }
}
15. Logging Rules
Never log the error message sent to the client as your only log.

Always log the underlying exception with context.

Include: user ID, request ID, entity ID, action.

Use log.error for 500s, log.warn for 4xx that indicates misuse.

Never log evidence content, case narratives, or passwords.

java
log.warn("Permission denied: user {} tried to access case {}", userId, caseId);
log.error("Failed to save case {}: {}", caseId, ex.getMessage(), ex);
16. What NOT to Do
Don't return raw exception messages to the client.

Don't include stack traces in API responses.

Don't reveal whether a username exists (use INVALID_CREDENTIALS for both cases).

Don't invent error codes without adding them to this document.

Don't use the same code for different meanings.

Don't use HTTP 200 with an error body --- use the correct status code.

Don't use lowercase or camelCase codes --- always UPPER_SNAKE_CASE.

Don't skip the details field when it would help the user.

Don't leak database column names or internal IDs in messages.

Don't use exclamation marks.

Don't blame the user.

The One-Sentence Summary
ERROR_CODES.md is the complete catalog of every error in TogetherSafe --- code, HTTP status, message, details, and how to handle. Backend throws these codes. Frontend handles them by code, never by message. Nothing ships without a proper code.
