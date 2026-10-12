# RUNBOOK.md --- Operations Runbook

**Step-by-step procedures for operating TogetherSafe in production.**

## 1. Purpose

When something breaks at 2 AM, no one has time to read a 50-page manual. This runbook is the short, actionable document you reach for.

Every procedure here has been:
- Tested in a staging environment.
- Reviewed by at least two team members.
- Updated after every production incident.

**If a procedure here is wrong, fix it immediately after the incident.** Outdated runbooks are worse than no runbooks.

## 2. How to Use This Runbook

1. Find the procedure you need (see the index below).
2. Follow the steps **exactly**.
3. If a step fails, **stop** and escalate (see §13).
4. After the incident, update this file with what you learned.

**Never improvise during an incident.** Improvisation is how small problems become catastrophes.

## 3. Index of Procedures

1. [Deploy a New Version](#4-deploy-a-new-version)
2. [Roll Back a Bad Deployment](#5-roll-back-a-bad-deployment)
3. [Restore from Backup](#6-restore-from-backup)
4. [Handle a Database Outage](#7-handle-a-database-outage)
5. [Handle an AI Service Outage](#8-handle-an-ai-service-outage)
6. [Handle a Keycloak Outage](#9-handle-a-keycloak-outage)
7. [Rotate Secrets](#10-rotate-secrets)
8. [Add a New Organization](#11-add-a-new-organization)
9. [Respond to a Security Incident](#12-respond-to-a-security-incident)
10. [Escalation Contacts](#13-escalation-contacts)

## 4. Deploy a New Version

### Prerequisites
- You have SSH access to the production server.
- The new JAR has passed CI on the `main` branch.
- A backup was taken in the last 24 hours.

### Steps

1. **Verify the backup exists:**
   ```bash
   ls -lh /mnt/backup/togethersafe/ | tail -5


   If no backup from the last 24 hours exists, STOP. Take a backup first:

bash
/opt/togethersafe/scripts/backup.sh
Copy the new JAR to the server:

bash
scp backend/target/togetherafesafe-backend-1.0.0.jar \
    togetherafesafe@server:/tmp/
Deploy using the deploy script:

bash
ssh togetherafesafe@server "sudo /opt/togethersafe/scripts/deploy.sh /tmp/togetherafesafe-backend-1.0.0.jar"
Verify the health check passes:

bash
curl -sf http://localhost:8080/actuator/health
Expected: {"status":"UP"}

Check the logs for errors:

bash
sudo journalctl -u togetherafesafe-backend.service -n 50
Announce the deployment in the team channel.

If something fails
The deploy script automatically rolls back if the health check fails.

If the health check passes but the app misbehaves, see §5.

5. Roll Back a Bad Deployment
Use this when a new deployment is causing problems.

Steps
Stop the service:

bash
sudo systemctl stop togetherafesafe-backend.service
Restore the previous JAR:

bash
sudo cp /opt/togethersafe/backend/togetherafesafe-backend.jar.bak \
        /opt/togethersafe/backend/togetherafesafe-backend.jar
Start the service:

bash
sudo systemctl start togetherafesafe-backend.service
Verify:

bash
curl -sf http://localhost:8080/actuator/health
Notify the team of the rollback and open a postmortem issue.

If no backup JAR exists
The deploy script creates .jar.bak before replacing. If missing, get the previous version from GitHub Releases.

6. Restore from Backup
Use this when data has been lost or corrupted.

⚠️ WARNING
Restoring will overwrite the current database. Take a fresh backup first.

Steps
Take a fresh backup of the current (broken) database:

bash
/opt/togethersafe/scripts/backup.sh
Choose the backup to restore:

bash
ls -lh /mnt/backup/togethersafe/
Run the restore script:

bash
sudo /opt/togethersafe/scripts/restore.sh /mnt/backup/togethersafe/togethersafe_YYYYMMDD_HHMMSS.sql.gz
Verify row counts match expectations:

bash
psql -U togetherafesafe -h localhost -d togetherafesafe_db -c "SELECT COUNT(*) FROM cases;"
psql -U togetherafesafe -h localhost -d togetherafesafe_db -c "SELECT COUNT(*) FROM users;"
Verify the app is working:

bash
curl -sf http://localhost:8080/actuator/health
Notify the team.

If the restore fails
Do not repeatedly retry. Escalate (§13) with the error message.

7. Handle a Database Outage
Symptoms
actuator/health shows {"status":"DOWN"} for db.

Users cannot log in or see data.

Steps
Check PostgreSQL status:

bash
sudo systemctl status postgresql
If PostgreSQL is stopped, start it:

bash
sudo systemctl start postgresql
Check for disk space:

bash
df -h /var/lib/postgresql
If disk is >90% full, clear old logs immediately:

bash
sudo journalctl --vacuum-time=3d
Check for lock contention:

bash
sudo -u postgres psql -c "SELECT pid, state, query FROM pg_stat_activity WHERE state != 'idle';"
If issues persist, restart PostgreSQL:

bash
sudo systemctl restart postgresql
This disconnects all users. Notify them first if possible.

Verify the app reconnects:

bash
curl -sf http://localhost:8080/actuator/health
8. Handle an AI Service Outage
Symptoms
AI chat returns errors.

curl http://localhost:8000/health fails.

Steps
Check the service status:

bash
sudo systemctl status togetherafesafe-ai.service
Restart if stopped:

bash
sudo systemctl restart togetherafesafe-ai.service
Check the logs:

bash
sudo journalctl -u togetherafesafe-ai.service -n 100
Check disk space and memory:

bash
df -h /
free -h
If the service keeps crashing, disable it temporarily:

bash
sudo systemctl stop togetherafesafe-ai.service
The backend should gracefully degrade (see BACKEND.md): AI endpoints return 503 SERVICE_UNAVAILABLE, but the rest of the app still works.

Notify the team and investigate.

Graceful degradation check
Users can still report cases.

Users can still view cases.

Users can still upload evidence.

Users see a friendly message when AI is unavailable.

9. Handle a Keycloak Outage
Symptoms
Users cannot log in.

JWT validation fails.

Steps
Check the Keycloak service:

bash
sudo systemctl status keycloak
Restart if stopped:

bash
sudo systemctl restart keycloak
Check the logs:

bash
sudo journalctl -u keycloak -n 100
Check LDAP connectivity:

bash
ldapsearch -x -H ldap://ldap.example.org -b "dc=example,dc=org"
If LDAP is down but Keycloak is up:

Users with cached tokens can still work.

New logins will fail.

Escalate to the LDAP administrator.

10. Rotate Secrets
Rotate secrets quarterly and immediately after any suspected leak.

Steps
Generate a new secret:

bash
openssl rand -base64 32
Update the environment file:

bash
sudo nano /opt/togethersafe/config/backend.env
Change JWT_SECRET to the new value.

Restart the backend:

bash
sudo systemctl restart togetherafesafe-backend.service
Verify:

bash
curl -sf http://localhost:8080/actuator/health
Notify users they may need to log in again.

Record the rotation in the audit log.

Also rotate:
Database password (DATABASE_PASSWORD).

Keycloak client secret.

Any API keys used by the AI service.

11. Add a New Organization
Note: v1 supports one organization per deployment. This procedure is for v2+ when multi-tenancy is enabled.

Steps
Create the organization in the database:

sql
INSERT INTO organizations (name, contact_email, contact_phone)
VALUES ('New NGO', 'contact@ngo.org', '+256700000000')
RETURNING id;
Note the returned id (e.g., 2).

Create the first admin user via the API:

bash
curl -X POST http://localhost:8080/api/v1/admin/users \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "username": "admin@neongo.org",
      "password": "temp-password",
      "fullName": "Admin User",
      "email": "admin@neongo.org",
      "role": "ADMIN"
    }'
Notify the new org admin to log in and change the password.

Verify the org's data is isolated (see PRODUCT_QA.md § Permission Tests).

12. Respond to a Security Incident
See SECURITY.md § Incident Response for the full process.

Immediate Steps
Classify severity (P0-P3).

Assemble the incident team (see §13).

Contain the incident:

Data breach: isolate affected systems, revoke sessions.

Credential leak: rotate secrets immediately.

Evidence tampering: freeze the case, compare checksums.

Preserve logs for forensics.

Notify:

P0: Notify the Incident Commander immediately (24/7).

P1: Within 4 hours.

P2: Within 24 hours.

P3: Within 72 hours.

Follow the regulatory timeline (72 hours for GDPR and Uganda DPPA).

Publish a postmortem within 7 days.

13. Escalation Contacts
Fill these in before going to production. A runbook with blank contacts is useless.

Role	Name	Contact	Backup
Incident Commander	[TBD]	[TBD]	[TBD]
Technical Lead	[TBD]	[TBD]	[TBD]
Database Admin	[TBD]	[TBD]	[TBD]
DevOps Engineer	[TBD]	[TBD]	[TBD]
Security Lead	[TBD]	[TBD]	[TBD]
Communications Lead	[TBD]	[TBD]	[TBD]
Hosting Provider Support	[TBD]	[TBD]	[TBD]
Keycloak / LDAP Support	[TBD]	[TBD]	[TBD]
When to Escalate
Any P0 or P1 incident — immediately.

Any incident you cannot resolve within 30 minutes.

Any suspected data breach.

Any physical security concern.

Any communication from a regulator or law enforcement.

14. Common Health Checks
Check all services at once
bash
sudo /opt/togethersafe/scripts/health-check.sh
Check backend
bash
curl -sf http://localhost:8080/actuator/health
Check AI service
bash
curl -sf http://localhost:8000/health
Check PostgreSQL
bash
pg_isready -h localhost -U togetherafesafe
Check disk space
bash
df -h
Check memory
bash
free -h
Check recent logs
bash
sudo journalctl -u togetherafesafe-backend.service -n 100
sudo journalctl -u togetherafesafe-ai.service -n 100
15. Maintenance Windows
Preferred: Monday through Thursday, 22:00–23:00 local time.

Avoid: Fridays, weekends, and holidays.

Notify users at least 48 hours in advance via the app announcement feature.

Keep downtime under 15 minutes where possible.

16. Post-Incident Checklist
After every incident, complete within 7 days:

□ Timeline of events documented.
□ Root cause identified.
□ What went well recorded.
□ What did not go well recorded.
□ Detection gap analysis (how could we have caught this faster?).
□ Action items assigned with owners and deadlines.
□ This runbook updated with new learnings.
□ Team debriefed (blameless).
□ Postmortem published internally.
17. What NOT to Do
Don't improvise during an incident.

Don't skip the fresh backup before restoring.

Don't restart PostgreSQL without notifying users (if possible).

Don't deploy on a Friday.

Don't skip the post-incident review.

Don't blame individuals — focus on systems.

Don't leave the escalation contacts blank.

Don't let this runbook go stale — update it after every incident.

Don't test disaster recovery procedures for the first time during an actual disaster.

The One-Sentence Summary
RUNBOOK.md is the step-by-step operations manual for TogetherSafe — deploy, roll back, restore, respond to incidents, rotate secrets. Every procedure is tested, every incident updates this file, and every step is followed exactly during a crisis.