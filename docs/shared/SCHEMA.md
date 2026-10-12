# SCHEMA.md --- Shared Document

**Every table, every column, every relationship. The source of truth for the database.**

## 1. How to Use This Document
- **Database engineer:** writes Flyway migrations from this.
- **Backend lead:** writes JPA entities matching this.
- **Frontend devs:** understand what fields exist.
- **QA:** verifies data integrity.
- **Never change the schema without updating this file.**
- **Never change this file without team agreement.**
- If the code and this document disagree, this document wins --- until someone updates both.

## 2. Conventions

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
Naming
Thing	Convention	Example
Table	snake_case, plural	cases, evidence_files
Column	snake_case	case_type
Foreign key	<table_singular>_id	case_id
Index	idx_<table>_<column>	idx_cases_status
Unique	uk_<table>_<column>	uk_users_username
FK constraint	fk_<table>_<column>	fk_cases_reporter_id
Check constraint	ck_<table>_<rule>	ck_cases_status
Migration	V<n>__<description>.sql	V010__create_evidence.sql
Data types
Purpose	Type
ID	BIGSERIAL
Foreign key	BIGINT
Short text	VARCHAR(n)
Long text	TEXT
Decimal (lat/lng)	DECIMAL(10,7)
Boolean	BOOLEAN
Date only	DATE
Timestamp	TIMESTAMP
Enum-like	VARCHAR(30) with CHECK
JSON	JSONB
File checksum	VARCHAR(64) (SHA-256)
3. Table Overview
#	Table	Purpose	Org-scoped	Soft delete
1	organizations	The NGO/agency itself	No	No
2	users	All system users	Yes	Yes
3	cases	Every reported case	Yes	Yes
4	evidence_files	Evidence metadata	Yes	Yes
5	evidence_shares	Explicit evidence sharing	Yes	No
6	referrals	Referrals to service providers	Yes	No
7	services	Service directory	Yes	Yes
8	case_notes	Case notes by caseworkers	Yes	No
9	audit_events	State-change audit log	Yes	No
4. Full Table Definitions
1. organizations
The organization running the deployment (e.g., NGO, government agency). For v1, only one row.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
name	VARCHAR(200)	No	---	
contact_email	VARCHAR(100)	Yes	NULL	
contact_phone	VARCHAR(50)	Yes	NULL	
logo_url	VARCHAR(500)	Yes	NULL	
created_at	TIMESTAMP	No	NOW()	
updated_at	TIMESTAMP	Yes	NULL	
Constraints:

PK: id

2. users
All system users.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
username	VARCHAR(100)	No	---	Unique per org
password_hash	VARCHAR(255)	No	---	BCrypt
full_name	VARCHAR(200)	No	---	
email	VARCHAR(100)	Yes	NULL	
phone	VARCHAR(50)	Yes	NULL	
role	VARCHAR(20)	No	---	ADMIN, CASEWORKER, SURVIVOR
is_active	BOOLEAN	No	TRUE	
failed_login_attempts	INT	No	0	
locked_until	TIMESTAMP	Yes	NULL	
last_login_at	TIMESTAMP	Yes	NULL	
created_at	TIMESTAMP	No	NOW()	
updated_at	TIMESTAMP	Yes	NULL	
Constraints:

PK: id

FK: fk_users_org_id → organizations(id)

Unique: uk_users_org_username (org_id, username)

Check: ck_users_role (role IN ('ADMIN', 'CASEWORKER', 'SURVIVOR'))

Indexes:

idx_users_org_id

idx_users_username

idx_users_role

idx_users_active on (org_id, is_active) WHERE is_active = TRUE

3. cases
Every reported case.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
case_reference	VARCHAR(50)	No	---	e.g., CASE-2026-0001
tracking_reference	VARCHAR(50)	Yes	NULL	SHA-256 hash for anonymous lookup
reporter_id	BIGINT	Yes	NULL	FK → users (null if anonymous)
assigned_caseworker_id	BIGINT	Yes	NULL	FK → users
case_type	VARCHAR(30)	No	---	See enum below
description	TEXT	No	---	
incident_date	DATE	Yes	NULL	
location_summary	VARCHAR(200)	Yes	NULL	
status	VARCHAR(20)	No	'OPEN'	
priority	VARCHAR(20)	No	'MEDIUM'	
is_active	BOOLEAN	No	TRUE	Soft delete
created_at	TIMESTAMP	No	NOW()	
updated_at	TIMESTAMP	Yes	NULL	
Enums:

case_type: PHYSICAL_ASSAULT, SEXUAL_ASSAULT, EMOTIONAL_ABUSE, ECONOMIC_ABUSE, CHILD_MARRIAGE, FORCED_MARRIAGE, ONLINE_HARASSMENT, MISSING_CHILD, FAMILY_SUPPORT, HARASSMENT, OTHER

status: OPEN, IN_PROGRESS, RESOLVED, CLOSED

priority: LOW, MEDIUM, HIGH, CRITICAL

Constraints:

PK: id

FK: fk_cases_org_id → organizations(id)

FK: fk_cases_reporter_id → users(id)

FK: fk_cases_assigned_caseworker_id → users(id)

Unique: uk_cases_org_reference (org_id, case_reference)

Unique: uk_cases_tracking_reference (tracking_reference) WHERE tracking_reference IS NOT NULL

Check: ck_cases_status (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'))

Check: ck_cases_priority (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'))

Indexes:

idx_cases_org_id

idx_cases_reporter_id

idx_cases_assigned_caseworker_id

idx_cases_status

idx_cases_priority

idx_cases_created_at (DESC)

4. evidence_files
Evidence metadata. Files are stored on disk, not in the database.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
case_id	BIGINT	No	---	FK → cases
file_name	VARCHAR(255)	No	---	Original name
file_type	VARCHAR(50)	No	---	MIME type
file_size_bytes	BIGINT	No	---	
storage_reference	VARCHAR(500)	No	---	Relative path on disk
checksum_sha256	VARCHAR(64)	No	---	Integrity verification
uploaded_by	BIGINT	No	---	FK → users
uploaded_at	TIMESTAMP	No	NOW()	
visibility	VARCHAR(30)	No	'PRIVATE'	PRIVATE, SHARED, PUBLIC_WITHIN_CASE
is_active	BOOLEAN	No	TRUE	Soft delete
Constraints:

PK: id

FK: fk_evidence_files_org_id → organizations(id)

FK: fk_evidence_files_case_id → cases(id)

FK: fk_evidence_files_uploaded_by → users(id)

Check: ck_evidence_files_visibility (visibility IN ('PRIVATE', 'SHARED', 'PUBLIC_WITHIN_CASE'))

Check: ck_evidence_files_size (file_size_bytes > 0)

Indexes:

idx_evidence_files_org_id

idx_evidence_files_case_id

idx_evidence_files_uploaded_by

5. evidence_shares
Explicit sharing of evidence with a specific user. Requires survivor consent.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
evidence_id	BIGINT	No	---	FK → evidence_files
shared_with_user_id	BIGINT	No	---	FK → users
shared_by_user_id	BIGINT	No	---	FK → users (must be owner or caseworker)
shared_at	TIMESTAMP	No	NOW()	
revoked_at	TIMESTAMP	Yes	NULL	
notes	TEXT	Yes	NULL	
Constraints:

PK: id

FK: fk_evidence_shares_org_id → organizations(id)

FK: fk_evidence_shares_evidence_id → evidence_files(id)

FK: fk_evidence_shares_shared_with_user_id → users(id)

FK: fk_evidence_shares_shared_by_user_id → users(id)

Unique: uk_evidence_shares (evidence_id, shared_with_user_id) WHERE revoked_at IS NULL

Indexes:

idx_evidence_shares_org_id

idx_evidence_shares_evidence_id

idx_evidence_shares_shared_with_user_id

6. referrals
Referrals to service providers.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
case_id	BIGINT	No	---	FK → cases
service_id	BIGINT	No	---	FK → services
requested_by	BIGINT	No	---	FK → users
status	VARCHAR(20)	No	'PENDING'	
requested_at	TIMESTAMP	No	NOW()	
responded_at	TIMESTAMP	Yes	NULL	
notes	TEXT	Yes	NULL	
response_notes	TEXT	Yes	NULL	
Enums:

status: PENDING, ACCEPTED, DECLINED, COMPLETED

Constraints:

PK: id

FK: fk_referrals_org_id → organizations(id)

FK: fk_referrals_case_id → cases(id)

FK: fk_referrals_service_id → services(id)

FK: fk_referrals_requested_by → users(id)

Check: ck_referrals_status (status IN ('PENDING', 'ACCEPTED', 'DECLINED', 'COMPLETED'))

Indexes:

idx_referrals_org_id

idx_referrals_case_id

idx_referrals_service_id

idx_referrals_status

7. services
The service directory (counselling, police, medical, shelters, legal aid).

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
name	VARCHAR(200)	No	---	
category	VARCHAR(50)	No	---	
description	TEXT	Yes	NULL	
location	VARCHAR(200)	Yes	NULL	
latitude	DECIMAL(10,7)	Yes	NULL	For map
longitude	DECIMAL(10,7)	Yes	NULL	For map
contact_phone	VARCHAR(50)	Yes	NULL	
contact_email	VARCHAR(100)	Yes	NULL	
is_verified	BOOLEAN	No	FALSE	
verified_at	TIMESTAMP	Yes	NULL	
is_active	BOOLEAN	No	TRUE	Soft delete
created_at	TIMESTAMP	No	NOW()	
updated_at	TIMESTAMP	Yes	NULL	
Enums:

category: COUNSELLING, POLICE, MEDICAL, SHELTER, LEGAL, OTHER

Constraints:

PK: id

FK: fk_services_org_id → organizations(id)

Check: ck_services_category (category IN ('COUNSELLING', 'POLICE', 'MEDICAL', 'SHELTER', 'LEGAL', 'OTHER'))

Indexes:

idx_services_org_id

idx_services_category

idx_services_active on (org_id, is_active) WHERE is_active = TRUE

8. case_notes
Notes added to a case by caseworkers.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
case_id	BIGINT	No	---	FK → cases
author_id	BIGINT	No	---	FK → users
note_type	VARCHAR(30)	No	---	
content	TEXT	No	---	
created_at	TIMESTAMP	No	NOW()	
updated_at	TIMESTAMP	Yes	NULL	
Enums:

note_type: INTAKE, FOLLOW_UP, INTERNAL, SURVIVOR_CONTACT

Constraints:

PK: id

FK: fk_case_notes_org_id → organizations(id)

FK: fk_case_notes_case_id → cases(id)

FK: fk_case_notes_author_id → users(id)

Check: ck_case_notes_type (note_type IN ('INTAKE', 'FOLLOW_UP', 'INTERNAL', 'SURVIVOR_CONTACT'))

Indexes:

idx_case_notes_org_id

idx_case_notes_case_id

idx_case_notes_author_id

9. audit_events
Every state-changing action.

Column	Type	Null	Default	Notes
id	BIGSERIAL	No	---	PK
org_id	BIGINT	No	---	FK → organizations
actor_id	BIGINT	Yes	NULL	FK → users (null = anonymous/system)
action	VARCHAR(50)	No	---	e.g., CASE_CREATED, EVIDENCE_SHARED
target_type	VARCHAR(50)	No	---	e.g., Case, Evidence
target_id	BIGINT	Yes	NULL	
details	JSONB	Yes	NULL	Non-sensitive metadata only
ip_address	VARCHAR(50)	Yes	NULL	
created_at	TIMESTAMP	No	NOW()	
CRITICAL: Never log sensitive content (evidence contents, full case narrative) to this table.

Constraints:

PK: id

FK: fk_audit_events_org_id → organizations(id)

FK: fk_audit_events_actor_id → users(id)

Indexes:

idx_audit_events_org_id

idx_audit_events_actor_id

idx_audit_events_action

idx_audit_events_created_at (DESC)

5. Relationships Summary
text
organizations
├── users
│   ├── cases (reporter)
│   ├── cases (assigned_caseworker)
│   ├── evidence_files (uploaded_by)
│   ├── evidence_shares (shared_with, shared_by)
│   ├── referrals (requested_by)
│   ├── case_notes (author)
│   └── audit_events (actor)
│
├── cases
│   ├── evidence_files
│   │   └── evidence_shares
│   ├── referrals
│   │   └── services
│   └── case_notes
│
└── services
    └── referrals
6. Common Queries
Find a survivor's open cases
sql
SELECT c.id, c.case_reference, c.case_type, c.status, c.priority, c.created_at
FROM cases c
WHERE c.reporter_id = ?
  AND c.is_active = TRUE
ORDER BY c.created_at DESC;
List all evidence shared with a caseworker
sql
SELECT ef.id, ef.file_name, ef.file_type, es.shared_at
FROM evidence_files ef
JOIN evidence_shares es ON es.evidence_id = ef.id
WHERE es.shared_with_user_id = ?
  AND es.revoked_at IS NULL
ORDER BY es.shared_at DESC;
Count open cases per caseworker
sql
SELECT u.id, u.full_name, COUNT(c.id) AS open_cases
FROM users u
LEFT JOIN cases c ON c.assigned_caseworker_id = u.id
  AND c.status IN ('OPEN', 'IN_PROGRESS')
  AND c.is_active = TRUE
WHERE u.role = 'CASEWORKER' AND u.is_active = TRUE
GROUP BY u.id, u.full_name
ORDER BY open_cases DESC;
7. Seed Data (Development Only)
For local development, create V099__test_data.sql with:

1 organization

3 users (1 admin, 2 caseworkers)

1 survivor user

5 cases in various statuses

10 services (counselling, police, medical, shelters, legal aid)

3 evidence files (metadata only)

2 referrals

2 evidence shares

NEVER seed real survivor data. Always synthetic.

8. Table Count
9 tables in v1.

Why not 45 like a full enterprise version?

No AI tables (AI service is stateless in v1)

No analytics tables (v2)

No notification tables (v2)

No messaging tables (v2)

No offline sync tables (v2)

No multi-org management tables (v3)

v1 is a subset. That is the point.

The One-Sentence Summary
SCHEMA.md defines all 9 tables for v1 --- every column, every constraint, every index, every relationship. Every table has org_id (multi-tenancy from day one). Cases and evidence are never hard-deleted. The database engineer writes Flyway migrations from this. The backend lead writes JPA entities matching this.