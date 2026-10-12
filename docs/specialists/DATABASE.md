# DATABASE.md --- Database Specialist Document

## 1. Who This Is For
You are the **Database Engineer** on TogetherSafe.
You own the PostgreSQL schema, Flyway migrations, indexes, and data integrity.

You work alongside:
- **Backend Lead** --- writes JPA entities that match your schema
- **DevOps Engineer** --- you both share the deployment responsibility
- **Product + QA** --- verifies data integrity and privacy

You own the schema. You do not edit Java code or FXML files.

## 2. What You Build in v1
- PostgreSQL 16 database schema
- Flyway migration scripts (versioned SQL)
- Indexes for performance
- Constraints for data integrity
- Soft-delete strategy (no hard deletes of cases/evidence)
- Seed data for local development
- Backup and restore verification
- Multi-tenancy enforcement (`school_id`/`org_id` on every table)

**Not in v1:** Analytics tables, notification tables, offline sync tables. Those are v2+.

## 3. Database Choice
- **Engine:** PostgreSQL 16
- **Why not SQLite?** TogetherSafe is a multi-user, multi-org platform. PostgreSQL provides concurrent access, transactions, strong constraints, and server-based deployment.
- **Migrations:** Flyway
- **Timezone:** All timestamps stored in UTC.

## 4. Naming Conventions
| Thing | Convention | Example |
|---|---|---|
| Table | snake_case, plural | `cases`, `evidence_files` |
| Column | snake_case | `case_type` |
| Foreign key | `<table_singular>_id` | `case_id` |
| Index | `idx_<table>_<column>` | `idx_cases_status` |
| Unique | `uk_<table>_<column>` | `uk_users_username` |
| FK constraint | `fk_<table>_<column>` | `fk_cases_reporter_id` |
| Check constraint | `ck_<table>_<rule>` | `ck_cases_status` |
| Migration | `V<n>__<description>.sql` | `V010__create_evidence.sql` |

## 5. Standard Columns
Every table has:
```sql
id BIGSERIAL PRIMARY KEY,
created_at TIMESTAMP NOT NULL DEFAULT NOW(),
updated_at TIMESTAMP


Every organization-scoped table also has:

sql
org_id BIGINT NOT NULL REFERENCES organizations(id)
Every soft-deletable table also has:

sql
is_active BOOLEAN NOT NULL DEFAULT TRUE
6. Data Types
Purpose	Type
ID	BIGSERIAL
Foreign key	BIGINT
Short text	VARCHAR(n)
Long text	TEXT
Boolean	BOOLEAN
Date only	DATE
Timestamp	TIMESTAMP
Enum-like	VARCHAR(30) with CHECK
JSON	JSONB
File checksum	VARCHAR(64) (SHA-256)
7. Core Tables for v1
1. organizations
The organization running the deployment (e.g., NGO, government agency). For v1, only one row.

id, name, contact_email, contact_phone, logo_url, created_at, updated_at

2. users
All system users.

id, org_id, username (unique per org), password_hash (BCrypt), full_name, email, phone, role (ADMIN, CASEWORKER, SURVIVOR), is_active, failed_login_attempts, locked_until, last_login_at, created_at, updated_at

CHECK: role IN ('ADMIN', 'CASEWORKER', 'SURVIVOR')

3. cases
Every reported case.

id, org_id, reporter_id (FK users), assigned_caseworker_id (FK users, nullable), case_type, description (TEXT), incident_date (DATE), location_summary (VARCHAR 200), status (OPEN, IN_PROGRESS, RESOLVED, CLOSED), priority (LOW, MEDIUM, HIGH, CRITICAL), created_at, updated_at

CHECK: status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')

CHECK: priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')

4. evidence_files
Metadata for evidence (files stored on disk).

id, org_id, case_id (FK cases), file_name (VARCHAR 255), file_type (VARCHAR 50), file_size_bytes (BIGINT), storage_reference (VARCHAR 500), checksum_sha256 (VARCHAR 64), uploaded_by (FK users), uploaded_at (TIMESTAMP), visibility (PRIVATE, SHARED, PUBLIC_WITHIN_CASE)

CHECK: visibility IN ('PRIVATE', 'SHARED', 'PUBLIC_WITHIN_CASE')

5. evidence_shares
Explicit sharing of evidence with a specific user.

id, org_id, evidence_id (FK evidence_files), shared_with_user_id (FK users), shared_by_user_id (FK users), shared_at (TIMESTAMP), revoked_at (TIMESTAMP, nullable), notes (TEXT)

6. referrals
Referrals to a service provider.

id, org_id, case_id (FK cases), service_id (FK services), requested_by (FK users), status (PENDING, ACCEPTED, DECLINED, COMPLETED), requested_at, responded_at, notes (TEXT)

CHECK: status IN ('PENDING', 'ACCEPTED', 'DECLINED', 'COMPLETED')

7. services
The service directory (counselling, police, medical, shelters, legal aid).

id, org_id, name (VARCHAR 200), category (VARCHAR 50), description (TEXT), location (VARCHAR 200), latitude (DECIMAL 10,7), longitude (DECIMAL 10,7), contact_phone, contact_email, is_verified (BOOLEAN), verified_at (TIMESTAMP), is_active (BOOLEAN)

8. case_notes
Notes added to a case by caseworkers.

id, org_id, case_id (FK cases), author_id (FK users), note_type (VARCHAR 30), content (TEXT), created_at, updated_at

CHECK: note_type IN ('INTAKE', 'FOLLOW_UP', 'INTERNAL', 'SURVIVOR_CONTACT')

9. audit_events
Every state-changing action.

id, org_id, actor_id (FK users, nullable), action (VARCHAR 50), target_type (VARCHAR 50), target_id (BIGINT, nullable), details (JSONB, nullable), ip_address (VARCHAR 50), created_at (TIMESTAMP)

IMPORTANT: Never log sensitive content (evidence contents, full case narrative) here.

8. Indexes (Minimum Required)
users: idx_users_org_id, idx_users_username, idx_users_role

cases: idx_cases_org_id, idx_cases_reporter_id, idx_cases_assigned_caseworker_id, idx_cases_status, idx_cases_priority

evidence_files: idx_evidence_files_case_id, idx_evidence_files_uploaded_by

evidence_shares: idx_evidence_shares_evidence_id, idx_evidence_shares_shared_with_user_id

referrals: idx_referrals_case_id, idx_referrals_service_id, idx_referrals_status

audit_events: idx_audit_events_actor_id, idx_audit_events_action, idx_audit_events_created_at (DESC)

9. Constraints (Critical)
Unique: uk_users_org_username (org_id, username)

Foreign keys always enforced

CHECK constraints on all enum-like columns

created_at is NOT NULL DEFAULT NOW()

updated_at is updated by application logic (or by a trigger)

10. Soft Deletes (Never Hard-Delete)
Survivors, cases, evidence, and notes are never hard-deleted.

Use is_active = FALSE for soft deletes.

Audit logs are append-only and cannot be modified or deleted.

11. Multi-Tenancy Rule
Every table (except organizations) has org_id.

Every query must filter by org_id derived from the authenticated user's session.

Never trust org_id from request body --- always derive it server-side.

12. Migrations (Flyway)
Located at: backend/src/main/resources/db/migration/

Naming: V001__create_organizations.sql, V002__create_users.sql, etc.

Rules:

Never modify a migration that has been applied. Create a new one.

Each migration is idempotent where possible.

Test migrations against a fresh database before committing.

Include index creation in the same migration as the table.

13. Seed Data (Development Only)
For local development, create V099__test_data.sql with:

1 organization

3 users (1 admin, 2 caseworkers)

1 survivor user

5 cases in various statuses

10 services (counselling, police, medical, shelters, legal aid)

3 evidence files (metadata only)

2 referrals

NEVER seed real survivor data. Always synthetic.

14. Backup and Restore
Daily pg_dump at 2 AM (handled by DevOps).

Retention: 30 days local, 90 days external.

Test restore monthly. A backup that cannot be restored is not a backup.

Encryption at rest for the backup drive.

15. Definition of Done (Database)
A database task is done when:

□ Migration written and versioned correctly.
□ Tested on a fresh PostgreSQL database.
□ Indexes created for foreign keys and search columns.
□ CHECK constraints applied to enum-like columns.
□ org_id present on every table (except organizations).
□ Foreign keys enforced.
□ SCHEMA.md updated with the new table/column.
□ Rollback strategy documented (if destructive change).
□ PR reviewed and approved.
16. What NOT to Do
Don't hard-delete survivors, cases, or evidence.

Don't modify an already-applied migration. Create a new one.

Don't skip org_id on any table.

Don't use ENUM types in PostgreSQL --- use VARCHAR(n) with CHECK.

Don't store large binary files in the database. Use disk storage with metadata.

Don't forget indexes on foreign keys.

Don't store timestamps in local timezone --- always UTC.

Don't log sensitive data (case narratives, evidence content) to audit tables.

Don't trust org_id from the request body.

Don't use triggers for business logic --- keep it in the application.

