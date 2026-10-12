# SECURITY.md --- Security Policy for TogetherSafe

## 1. Our Commitment
TogetherSafe handles **highly sensitive survivor data**. Security is not a feature — it is the foundation of the entire system. This document explains how we protect survivors and how to report vulnerabilities.

## 2. Supported Versions
| Version | Supported |
|---------|-----------|
| 1.x.x | Yes (active development) |
| < 1.0.0 | No |

## 3. Threat Model
TogetherSafe is a high-risk target. We design against: abusive partners, stalkers, hostile state actors, insider threats, cybercriminals, and malicious researchers.

## 4. Anonymity & De-anonymization
- Anonymous reports do NOT store IP address or User-Agent.
- Tracking references are one-way hashes.
- Never link anonymous reports to logged-in accounts.
- Strip EXIF metadata from uploaded images.
- Rate-limit the tracking reference endpoint.

## 5. Physical Device Security
- Disguised Mode hides the app from the taskbar.
- No offline cache of case data by default.
- PIN required to reopen the app.
- We cannot defend against rooted devices or physical coercion.

## 6. Data Classification
- **Public:** Service directory, public resources
- **Internal:** Code, architecture docs
- **Confidential:** Case metadata, user records
- **Restricted:** Case narratives, evidence files, crisis flags

## 7. Cryptographic Standards
- Password hashing: BCrypt (cost 10+)
- JWT signing: HS256 (256-bit secret)
- File integrity: SHA-256
- Data at rest: AES-256-GCM
- Transport: TLS 1.3
- Never roll your own cryptography.

## 8. Third-Party Data Flows
| Service | Data Sent | Risk |
|---------|-----------|------|
| Keycloak | Username, password | High |
| Map API | Map tile requests only | Low |
| AI Service | Sanitized text (no PII) | High |
| External LLM | Only with DPA | Critical |

## 9. Reporting a Vulnerability
**DO NOT open a public GitHub issue.**

Preferred: Use GitHub Private Vulnerability Reporting (Security tab → "Report a vulnerability").

Fallback: security@togethersafe.org (placeholder).

Never include real survivor data in your report.

## 10. Response Timeline
| Stage | Target |
|-------|--------|
| Acknowledgment | 24 hours |
| Triage | 48 hours |
| Critical fix | 7 days |
| High fix | 14 days |
| Medium fix | 30 days |

## 11. Severity Classification
- **Critical:** Data breach; auth bypass; evidence tampering
- **High:** Privilege escalation; evidence share bypass; crisis detection bypass
- **Medium:** Info disclosure; weak cryptography
- **Low:** Minor information leak

**GBV rules:** Any vulnerability that could identify a survivor is High. Any that could link a survivor to a case is Critical. Any that could access evidence without consent is Critical.

## 12. Contributor Requirements
- All auth via Keycloak (OAuth 2.0 / LDAP)
- RBAC enforced server-side
- Never trust orgId from request body
- Evidence: files on disk, metadata in DB, SHA-256 checksums
- Every state change logged to audit_events
- Never log case narratives or evidence content
- Passwords: BCrypt, never plaintext
- Never commit secrets
- CI blocks on new hardcoded secrets and critical CVEs

## 13. Incident Response
| Severity | Definition | Response |
|----------|------------|----------|
| P0 | Active data breach | Immediate, 24/7 |
| P1 | Exploit in the wild | 4 hours |
| P2 | High-risk, no exploit | 24 hours |
| P3 | Medium-risk | 72 hours |

**Containment:**
- Data breach: isolate systems, revoke sessions, rotate secrets, notify.
- Credential leak: revoke, rotate, audit.
- Evidence tampering: freeze case, compare checksums, preserve original.

## 14. Legal & Regulatory Compliance
- **GDPR:** 72-hour breach notification; data subject rights.
- **Uganda DPPA (2019):** Register with NITA-U; 72-hour breach notification.
- **WHO GBV Guidelines:** Survivor-centred care, informed consent.

## 15. Responsible Disclosure
We support good-faith research. Researchers who follow this policy are protected under safe harbor. We commit to acknowledging within 24 hours and not pursuing legal action.

## 16. Security Checklist for Every Release
- [ ] Auth paths tested
- [ ] Permission enforcement verified
- [ ] Evidence sharing requires explicit consent
- [ ] Audit logging verified
- [ ] No secrets in Git
- [ ] No real survivor data in dev/test
- [ ] Dependency scan passed
- [ ] SAST scan passed
- [ ] Backup verified
- [ ] Disguised Mode tested
- [ ] Crisis detection tested
- [ ] AI refuses legal/medical advice
- [ ] EXIF stripped from images
- [ ] Anonymous reports don't store IP/User-Agent

## 17. What NOT to Do
- Never commit `.env` files or secrets.
- Never log evidence content or case narratives.
- Never trust orgId from request body.
- Never skip permission checks.
- Never store evidence in the database.
- Never disable a security gate.
- Never use real survivor data in testing.
- Never share evidence without consent.
- Never publicly disclose a vulnerability before a fix.
- Never roll your own cryptography.
- Never store IP/User-Agent for anonymous reports.

## 18. Contact
- Security reports: GitHub Private Vulnerability Reporting (preferred)
- Fallback: security@togethersafe.org (placeholder)

## The One-Sentence Summary
**SECURITY.md defines how TogetherSafe protects survivor data — through a documented threat model, anonymity guarantees, physical device security, third-party data flow controls, cryptographic standards, server-side RBAC, CI/CD security gates, GDPR and Uganda DPPA compliance, and coordinated vulnerability disclosure with safe harbor.**


