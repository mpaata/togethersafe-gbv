-- Development seed data (synthetic only, no real survivor data)

INSERT INTO organizations (name, contact_email, contact_phone)
VALUES ('Safe Haven NGO', 'contact@safehaven.org', '+256700123456');

INSERT INTO users (org_id, username, password_hash, full_name, role)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', 'ADMIN');

INSERT INTO users (org_id, username, password_hash, full_name, role)
VALUES (1, 'caseworker1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Achieng', 'CASEWORKER');
