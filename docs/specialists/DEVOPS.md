# DEVOPS.md --- DevOps Specialist Document

## 1. Who This Is For
You are the **DevOps Engineer** on TogetherSafe.
You set up the server, deploy the system, keep it running, and back it up.

You work alongside:
- **Backend Lead** --- packages the JAR you deploy
- **JavaFX Developer** --- packages the `.msi`/`.dmg`/`.deb` you distribute
- **Database Engineer** --- you both share the deployment responsibility
- **Product + QA** --- coordinates the pilot deployment with you

You own the servers, deployment, backups, and monitoring. You do not edit backend code, JavaFX code, or the Python AI code.

## 2. What You Build in v1
- Ubuntu 24.04 LTS server setup
- PostgreSQL 16 installation and configuration
- Java 25 runtime installation
- Spring Boot app as a systemd service
- Keycloak for OAuth 2.0 / LDAP
- Python FastAPI AI service as a systemd service
- Cloudflare Tunnel for secure remote access
- GitHub Actions CI/CD pipeline
- Daily automated backup (pg_dump + cron)
- Monitoring (uptime, disk, logs)
- Deployment scripts and runbooks

**Not in v1:** Docker, Kubernetes, load balancers, multi-server. Keep it simple: one server, one database.

## 3. Server Architecture
```text
+-------------------------------------------------------------+
|  TOGETHERSAFE SERVER (Ubuntu 24.04 LTS)                    |
|                                                              |
|  +-----------------------------------------------------+    |
|  |  Spring Boot App (JAR)                              |    |
|  |  - systemd service: togetherafesafe-backend.service |    |
|  |  - Port 8080                                        |    |
|  |  - Heap: 2 GB                                       |    |
|  +-----------------------------------------------------+    |
|                                                              |
|  +-----------------------------------------------------+    |
|  |  Python AI Service (FastAPI)                        |    |
|  |  - systemd service: togetherafesafe-ai.service      |    |
|  |  - Port 8000                                        |    |
|  +-----------------------------------------------------+    |
|                                                              |
|  +-----------------------------------------------------+    |
|  |  Keycloak (OAuth 2.0 / LDAP)                        |    |
|  |  - systemd service: keycloak.service                |    |
|  |  - Port 8180                                        |    |
|  +-----------------------------------------------------+    |
|                                                              |
|  +-----------------------------------------------------+    |
|  |  PostgreSQL 16                                      |    |
|  |  - Port 5432 (LAN only)                             |    |
|  +-----------------------------------------------------+    |
|                                                              |
|  +-----------------------------------------------------+    |
|  |  Cloudflare Tunnel (cloudflared)                    |    |
|  |  - Exposes localhost:8080 as https://...            |    |
|  +-----------------------------------------------------+    |
|                                                              |
|  +-----------------------------------------------------+    |
|  |  Backup (cron) + Monitoring (cron)                  |    |
|  +-----------------------------------------------------+    |
+-------------------------------------------------------------+



One server. No containers. Simple, reliable, easy to support.

4. Folder Structure (on the server)
text
/opt/togethersafe/
|-- backend/
|   |-- togetherafesafe-backend.jar
|   +-- application-prod.yml
|
|-- ai-service/
|   |-- main.py
|   |-- requirements.txt
|   +-- venv/
|
|-- logs/
|   |-- backend.log
|   |-- ai.log
|   +-- backup.log
|
|-- backups/
|   +-- togetherafesafe_YYYYMMDD.sql.gz
|
|-- scripts/
|   |-- backup.sh
|   |-- restore.sh
|   |-- deploy.sh
|   +-- health-check.sh
|
+-- config/
    +-- backend.env
5. Server Setup (Fresh Ubuntu 24.04)
Step 1 --- Base system
bash
sudo apt update && sudo apt upgrade -y
sudo apt install -y curl wget git ufw fail2ban htop unzip
sudo timedatectl set-timezone Africa/Kampala
sudo useradd -m -s /bin/bash togetherafesafe
sudo usermod -aG sudo togetherafesafe
Step 2 --- Firewall
bash
sudo ufw allow OpenSSH
sudo ufw allow 8080/tcp   # LAN only
sudo ufw enable
Step 3 --- Java 25
bash
sudo apt install -y openjdk-25-jre-headless
java -version
Step 4 --- PostgreSQL 16
bash
sudo apt install -y postgresql-16 postgresql-contrib-16
sudo -u postgres psql <<EOF
CREATE USER togetherafesafe WITH PASSWORD 'STRONG_PASSWORD';
CREATE DATABASE togetherafesafe_db OWNER togetherafesafe;
GRANT ALL PRIVILEGES ON DATABASE togetherafesafe_db TO togetherafesafe;
EOF
Step 5 --- Python 3.12
bash
sudo apt install -y python3.12 python3.12-venv python3-pip
Step 6 --- App user and folders
bash
sudo mkdir -p /opt/togethersafe/{backend,ai-service,logs,backups,scripts,config}
sudo chown -R togetherafesafe:togethersafe /opt/togethersafe
6. Spring Boot Service (systemd)
Create /etc/systemd/system/togethersafe-backend.service:

ini
[Unit]
Description=TogetherSafe Backend
After=network.target postgresql.service
Requires=postgresql.service

[Service]
Type=simple
User=togethersafe
WorkingDirectory=/opt/togethersafe/backend
Environment="SPRING_PROFILES_ACTIVE=prod"
EnvironmentFile=/opt/togethersafe/config/backend.env
ExecStart=/usr/bin/java -Xms512m -Xmx2048m -jar /opt/togethersafe/backend/togetherafesafe-backend.jar
Restart=on-failure
RestartSec=10
StandardOutput=append:/opt/togethersafe/logs/backend.log
StandardError=append:/opt/togethersafe/logs/backend-error.log

[Install]
WantedBy=multi-user.target
Create /opt/togethersafe/config/backend.env:

bash
DATABASE_URL=jdbc:postgresql://localhost:5432/togethersafe_db
DATABASE_USER=togethersafe
DATABASE_PASSWORD=STRONG_PASSWORD
JWT_SECRET=<generate-a-256-bit-secret>
SERVER_PORT=8080
AI_SERVICE_URL=http://localhost:8000
Secure it:

bash
sudo chmod 600 /opt/togethersafe/config/backend.env
sudo chown togetherafesafe:togethersafe /opt/togethersafe/config/backend.env
sudo systemctl daemon-reload
sudo systemctl enable togetherafesafe-backend.service
sudo systemctl start togetherafesafe-backend.service
7. Python AI Service (systemd)
Create /etc/systemd/system/togethersafe-ai.service:

ini
[Unit]
Description=TogetherSafe AI Service
After=network.target

[Service]
Type=simple
User=togethersafe
WorkingDirectory=/opt/togethersafe/ai-service
ExecStart=/opt/togethersafe/ai-service/venv/bin/uvicorn main:app --host 0.0.0.0 --port 8000
Restart=on-failure
RestartSec=10
StandardOutput=append:/opt/togethersafe/logs/ai.log
StandardError=append:/opt/togethersafe/logs/ai-error.log

[Install]
WantedBy=multi-user.target
Setup:

bash
cd /opt/togethersafe/ai-service
python3.12 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
sudo systemctl enable togetherafesafe-ai.service
sudo systemctl start togetherafesafe-ai.service
8. Keycloak Setup
Download Keycloak (recommended: standalone distribution).

Configure LDAP User Federation (point to your LDAP server).

Create a Realm: togethersafe.

Create Clients: togethersafe-desktop (public, for JavaFX), togethersafe-backend (confidential, for Spring Boot).

Configure roles: ADMIN, CASEWORKER, SURVIVOR.

Run as systemd service similar to backend.

9. CI/CD with GitHub Actions
.github/workflows/ci.yml:

yaml
name: CI
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build-backend:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_DB: togetherafesafe_test
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
        ports: [5432:5432]
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
          cache: maven
      - name: Build backend
        run: cd backend && mvn clean verify -B
        env:
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/togethersafe_test
          SPRING_DATASOURCE_USERNAME: test
          SPRING_DATASOURCE_PASSWORD: test
          JWT_SECRET: test-secret-for-ci-only

  build-desktop:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
          cache: maven
      - name: Build desktop
        run: cd desktop && mvn clean verify -B

  build-ai:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: '3.12'
      - name: Install deps
        run: cd ai-service && pip install -r requirements.txt
      - name: Run tests
        run: cd ai-service && pytest
10. Backup Script
/opt/togethersafe/scripts/backup.sh:

bash
#!/bin/bash
set -e

DB_NAME="togethersafe_db"
DB_USER="togethersafe"
BACKUP_DIR="/opt/togethersafe/backups"
EXTERNAL_DIR="/mnt/backup/togethersafe"
RETENTION_DAYS=30
DATE=$(date +%Y%m%d_%H%M%S)

pg_dump -U "$DB_USER" -h localhost "$DB_NAME" | gzip > "$BACKUP_DIR/togethersafe_${DATE}.sql.gz"
cp "$BACKUP_DIR/togethersafe_${DATE}.sql.gz" "$EXTERNAL_DIR/"

find "$BACKUP_DIR" -name "togethersafe_*.sql.gz" -mtime +$RETENTION_DAYS -delete
find "$EXTERNAL_DIR" -name "togethersafe_*.sql.gz" -mtime +$RETENTION_DAYS -delete

echo "[$(date)] Backup complete: togethersafe_${DATE}.sql.gz"
Cron (runs daily at 2 AM):

bash
0 2 * * * /opt/togethersafe/scripts/backup.sh >> /opt/togethersafe/logs/backup.log 2>&1
11. Health Check Script
/opt/togethersafe/scripts/health-check.sh:

bash
#!/bin/bash

# Backend
if curl -sf http://localhost:8080/actuator/health > /dev/null; then
    echo "Backend: OK"
else
    echo "Backend: DOWN"
fi

# AI Service
if curl -sf http://localhost:8000/health > /dev/null; then
    echo "AI Service: OK"
else
    echo "AI Service: DOWN"
fi

# PostgreSQL
if pg_isready -h localhost -U togetherafesafe > /dev/null 2>&1; then
    echo "PostgreSQL: OK"
else
    echo "PostgreSQL: DOWN"
fi

# Disk
DISK_USAGE=$(df / | tail -1 | awk '{print $5}' | sed 's/%//')
if [ "$DISK_USAGE" -gt 85 ]; then
    echo "Disk: WARNING ($DISK_USAGE%)"
else
    echo "Disk: OK ($DISK_USAGE%)"
fi
Cron (every 5 minutes):

bash
*/5 * * * * /opt/togethersafe/scripts/health-check.sh >> /opt/togethersafe/logs/health.log 2>&1
12. Monitoring and Logs
Application logs: /opt/togethersafe/logs/

Rotate with logrotate (/etc/logrotate.d/togethersafe)

View live logs:

bash
sudo journalctl -u togetherafesafe-backend.service -f
sudo journalctl -u togetherafesafe-ai.service -f
sudo tail -f /opt/togethersafe/logs/backup.log
External uptime monitoring: UptimeRobot (pings /actuator/health).

13. Definition of Done (DevOps)
A DevOps task is done when:

□ Works on a fresh Ubuntu 24.04 server.
□ Documented in docs/07-operations/.
□ Tested end-to-end.
□ Backup verified (restore test passed).
□ Health check passes.
□ Logs are written and rotated.
□ Rollback procedure documented.
□ Runbook written.
□ PR reviewed and approved.
14. What NOT to Do
Don't use Docker in v1 --- adds complexity, no benefit for one server.

Don't expose PostgreSQL to the internet --- LAN only.

Don't open port 8080 to the internet --- use Cloudflare Tunnel.

Don't run as root --- use the togethersafe user.

Don't store passwords in plain text --- use environment files with chmod 600.

Don't skip backups --- test them monthly.

Don't deploy on a Friday --- deploy Monday--Thursday.

Don't edit the JAR on the server --- always deploy from source.

Don't hardcode the server URL in the desktop app --- make it configurable.

Don't skip the restore test --- a backup you can't restore is not a backup.