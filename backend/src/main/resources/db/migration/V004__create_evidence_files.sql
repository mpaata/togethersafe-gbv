CREATE TABLE evidence_files (
    id BIGSERIAL PRIMARY KEY,
    org_id BIGINT NOT NULL REFERENCES organizations(id),
    case_id BIGINT NOT NULL REFERENCES cases(id),
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    storage_reference VARCHAR(500) NOT NULL,
    checksum_sha256 VARCHAR(64) NOT NULL,
    uploaded_by BIGINT NOT NULL REFERENCES users(id),
    uploaded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    visibility VARCHAR(30) NOT NULL DEFAULT 'PRIVATE' CHECK (visibility IN ('PRIVATE', 'SHARED', 'PUBLIC_WITHIN_CASE')),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_evidence_files_org_id ON evidence_files(org_id);
CREATE INDEX idx_evidence_files_case_id ON evidence_files(case_id);
CREATE INDEX idx_evidence_files_uploaded_by ON evidence_files(uploaded_by);