CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    org_id BIGINT NOT NULL REFERENCES organizations(id),
    name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('COUNSELLING', 'POLICE', 'MEDICAL', 'SHELTER', 'LEGAL', 'OTHER')),
    description TEXT,
    location VARCHAR(200),
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    contact_phone VARCHAR(50),
    contact_email VARCHAR(100),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verified_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE INDEX idx_services_org_id ON services(org_id);
CREATE INDEX idx_services_category ON services(category);