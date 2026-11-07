CREATE TABLE cases (
    id BIGSERIAL PRIMARY KEY,
    org_id BIGINT NOT NULL REFERENCES organizations(id),
    case_reference VARCHAR(50) NOT NULL,
    tracking_reference VARCHAR(50),
    reporter_id BIGINT REFERENCES users(id),
    assigned_caseworker_id BIGINT REFERENCES users(id),
    case_type VARCHAR(30) NOT NULL,
    description TEXT NOT NULL,
    incident_date DATE,
    location_summary VARCHAR(200),
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT uk_cases_org_reference UNIQUE (org_id, case_reference)
);

CREATE INDEX idx_cases_org_id ON cases(org_id);
CREATE INDEX idx_cases_reporter_id ON cases(reporter_id);
CREATE INDEX idx_cases_assigned_caseworker_id ON cases(assigned_caseworker_id);
CREATE INDEX idx_cases_status ON cases(status);
CREATE INDEX idx_cases_priority ON cases(priority);