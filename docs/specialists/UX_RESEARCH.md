# UX_RESEARCH.md --- User Experience Research Specialist Document

## 1. Who This Is For
You are the **UX Researcher / Designer** on TogetherSafe.
You ensure every screen is designed around real survivor needs, not assumptions.

You work alongside:
- **Product + QA** --- you share user insights and validation
- **JavaFX Developer** --- you design the flows they build
- **Backend Lead** --- you define the data needed for each screen
- **AI Service Developer** --- you design the AI chat interactions

You own the UX research, personas, user journeys, and design principles. You do not write production code.

## 2. Why This Document Exists

**TogetherSafe is not a normal app.** Our primary user is a person in or recovering from a traumatic situation, often in danger. Every design decision has safety implications.

Getting UX wrong is not "just a bad interface" — it can:
- Put a survivor in physical danger (visible app, wrong notification).
- Revictimize a survivor (judgmental language, unnecessary questions).
- Prevent a survivor from getting help (confusing flow, too many steps).
- Break trust permanently (data leak, unexpected behavior).

**This document is the source of truth for who we build for and why.**

## 3. Design Principles (Non-Negotiable)

### 3.1 Safety First
- **Every screen must be usable in under 30 seconds.**
- **Every action must be reversible** (except SOS).
- **No screen should reveal sensitive information** if someone else looks over the shoulder.
- **The app must work on a shared device** without leaving traces.

### 3.2 Survivor-Centred
- **The survivor controls their data.** Nothing is shared without explicit consent.
- **The survivor controls the pace.** No mandatory fields, no forced flows.
- **The survivor is never blamed.** Language is neutral, supportive, non-judgmental.

### 3.3 Trauma-Informed
- **Predictable.** No surprise modals, no sudden changes.
- **Calm.** Soft colors, generous whitespace, no alarming reds except SOS/errors.
- **Empowering.** Show options, not commands. "You can..." not "You must..."
- **Non-triggering.** Avoid graphic imagery, avoid detailed descriptions, allow users to skip anything.

### 3.4 Privacy by Default
- **Anonymous reporting works without any account.**
- **No tracking without consent.**
- **No notifications that reveal case details.**
- **The app can be disguised, hidden, or wiped.**

### 3.5 Accessible to All
- **WCAG 2.1 AA minimum.**
- **Works on low-end devices and slow networks.**
- **Supports screen readers, keyboard-only navigation, high contrast.**
- **Language is plain, translated where needed.**

## 4. Personas

Personas are composite characters based on real research. They are not real people.

### 4.1 Primary Persona: Amina (Survivor, Urban)

- **Age:** 26
- **Location:** Kampala, Uganda
- **Situation:** Recently left an abusive partner. Has one child (age 4).
- **Device:** Basic Android phone (2 GB RAM), shared with her sister.
- **Tech confidence:** Medium. Uses WhatsApp, mobile money.
- **Language:** Luganda (primary), English (functional).
- **Needs:** Report safely, track case, access counselling, store evidence.
- **Fears:** Partner finding out, being judged, losing her child.
- **Motivations:** Protect her child, get justice, rebuild her life.

**Key insights:**
- She may only have **5 minutes** of private time per day.
- She may be **watched** by her partner or family.
- She may **not trust** apps after previous bad experiences.
- She needs to know **who sees her data** and **how to hide the app**.

### 4.2 Secondary Persona: Grace (Survivor, Rural)

- **Age:** 34
- **Location:** Rural Uganda
- **Situation:** Married, experiencing economic and emotional abuse. Has 4 children.
- **Device:** Shared family phone. Rarely has data.
- **Tech confidence:** Low. Uses voice notes more than text.
- **Language:** Runyankole (primary), limited English.
- **Needs:** Information, safe contact with a counsellor, community support.
- **Fears:** Being disowned, losing her children, community shame.
- **Motivations:** Her children's future.

**Key insights:**
- She may **never have private device access**.
- She may need **voice-based interactions** (v2+).
- She may need **community-level trusted contacts**.
- She needs **offline support** (v2+).

### 4.3 Tertiary Persona: Sarah (Caseworker)

- **Age:** 32
- **Location:** Kampala
- **Role:** Social worker at an NGO, handles 40+ active cases.
- **Device:** Windows laptop at the office.
- **Tech confidence:** High.
- **Language:** English.
- **Needs:** Quickly triage cases, see urgent flags, update statuses, refer to services.
- **Fears:** Missing a critical case, mishandling evidence, data breach.
- **Motivations:** Help survivors rebuild, work efficiently.

**Key insights:**
- She has **limited time per case** (5-10 minutes for triage).
- She needs **urgent cases surfaced immediately**.
- She needs **keyboard efficiency** (no mouse).
- She must **never see cases she is not assigned to**.

### 4.4 Quaternary Persona: Daniel (Administrator)

- **Age:** 45
- **Location:** Kampala
- **Role:** Director of an NGO with 15 caseworkers.
- **Device:** Windows laptop.
- **Tech confidence:** Medium.
- **Needs:** Overview of all cases, user management, audit logs, reports.
- **Fears:** Regulatory non-compliance, data leak, staff misuse.
- **Motivations:** Program impact, donor reporting, staff accountability.

**Key insights:**
- He needs **high-level summaries**, not individual cases.
- He needs **audit trails** for everything.
- He needs **export and reporting** for donors.

## 5. User Journeys

### 5.1 Journey 1: Anonymous Report (Amina)

**Context:** Amina has 5 minutes alone. She just experienced violence last night.

1. **Opens the app** (already installed, disguised as "System Update").
2. **Taps "Report a Case"** on the home screen (no login required).
3. **Guided form:** Case type, brief description, optional location, optional date.
4. **Submits anonymously.**
5. **Receives a tracking reference** (e.g., `TS-4K7P-9M2Q`) — copied to clipboard.
6. **Taps "Save my reference"** — copies it somewhere safe, then closes the app.

**What must work:**
- Total time: under 3 minutes.
- No account required.
- No revealing questions.
- Reference generated and clearly saved.

**What must never happen:**
- The app asking for her name or phone.
- The app showing her previous reports on a shared device.
- A notification appearing while her partner is nearby.

### 5.2 Journey 2: Tracking a Case (Amina, 3 days later)

**Context:** Amina wants to know if anyone has reviewed her report.

1. **Opens the app.**
2. **Taps "Track a Case."**
3. **Pastes her tracking reference.**
4. **Sees a simple status:** "Your report has been received. A caseworker is reviewing it."
5. **Closes the app.**

**What must work:**
- No login required.
- Status is written in plain language (not "IN_PROGRESS").
- No personal data shown.

**What must never happen:**
- The status page showing her case details.
- The status page suggesting she contact anyone (unless she asks).
- The tracking reference being guessable.

### 5.3 Journey 3: Caseworker Triage (Sarah)

**Context:** Sarah arrives at work. She has 40 active cases and needs to triage.

1. **Logs in** (Keycloak, JWT stored in session).
2. **Opens Admin Dashboard.**
3. **Sees summary cards:** Open (12), Urgent (3), Resolved this month (8).
4. **Clicks "Urgent Cases"** — sorted by priority (CRITICAL first).
5. **Opens a CRITICAL case.** Reads the summary.
6. **Adds a case note.** Updates status to IN_PROGRESS.
7. **Assigns to herself.** Refers to counselling service.

**What must work:**
- Keyboard-only navigation from login to triage.
- Urgent cases surfaced immediately.
- Adding a note is fast (no modal chains).
- Referral creation is 2 clicks.

**What must never happen:**
- Any delay >1 second on any action.
- Any case appearing that is not assigned to her.
- Any loss of a note or status change.

### 5.4 Journey 4: Evidence Upload (Amina)

**Context:** Amina has a photo of bruising on her arm. She wants to save it as evidence.

1. **Opens the app.**
2. **Logs in** (she created an account after her first report).
3. **Opens her case.**
4. **Taps "Add Evidence."**
5. **Selects a photo from her gallery.**
6. **Confirms upload.** Sees the file listed with a checksum.
7. **Taps "Keep private"** (default).
8. **Closes the app.**

**What must work:**
- File upload is fast even on slow networks.
- Photo's EXIF metadata (GPS, timestamp) is stripped before storage.
- File is private by default.
- She can share explicitly later.

**What must never happen:**
- The app asking her to share with anyone by default.
- The photo staying on the device in a readable cache.
- The upload failing silently.

### 5.5 Journey 5: Crisis Detection (Amina)

**Context:** Amina is having a very dark moment. She opens the AI chat.

1. **Opens the app.**
2. **Taps "AI Support."**
3. **Types:** "I don't want to live anymore."
4. **AI detects crisis.**
5. **Response appears immediately:** crisis hotline numbers, encouragement to call.
6. **A caseworker is alerted** (backend flags the case).

**What must work:**
- Crisis detection is fast and reliable.
- Emergency numbers are prominent.
- No "Are you sure?" dialogs.
- Human is notified.

**What must never happen:**
- The AI trying to counsel her itself.
- The AI giving medical advice.
- The response being generic or dismissive.
- Any delay in surfacing crisis resources.

## 6. Jobs to Be Done (JTBD)

| When... | I want to... | So I can... |
|---|---|---|
| I've just experienced violence | Report it quickly and safely | Get help without putting myself at risk |
| I'm worried about my case | Check its status anonymously | Feel some control |
| I have evidence | Store it securely | Use it later if I decide to pursue justice |
| I'm scared | Talk to someone | Feel less alone |
| I'm in crisis | Get emergency numbers fast | Get immediate help |
| I'm a caseworker | Triage and follow up efficiently | Help more survivors |
| I'm an admin | See the whole picture | Lead my team and report to donors |

## 7. Usability Testing Plan

### 7.1 Before Every Major Release
- **5 task-based tests** with real users (survivors or caseworkers).
- **Moderated** — a researcher watches and asks questions.
- **Recorded** (with consent) — for review.
- **Synthetic data only** — never real survivor data.

### 7.2 Critical Tasks to Test
1. Report a case anonymously (under 3 minutes).
2. Track a case by reference.
3. Upload evidence and confirm it is private.
4. Use the AI chat when scared.
5. As a caseworker, triage 5 cases in 15 minutes.

### 7.3 Success Metrics
- **Task completion rate** > 90%.
- **Time on task** meets or beats targets.
- **Error rate** < 5%.
- **Subjective confidence** (SUS score) > 75.

### 7.4 What to Watch For
- **Moments of hesitation** — where users pause.
- **Signs of stress** — sighing, frowning, racing.
- **Workarounds** — users doing something unexpected.
- **Misunderstanding** — users interpreting labels wrongly.
- **Abandonment** — users quitting a task.

### 7.5 Never
- **Never test with real survivor data.**
- **Never test with a survivor without a support plan** (counsellor on standby).
- **Never dismiss subjective feedback** as "they didn't understand."
- **Never assume** a task is "obvious."

## 8. Content Design

### 8.1 Tone
- **Calm, supportive, respectful.**
- **Neutral, non-judgmental.**
- **Plain English, short sentences.**
- **Second person ("You can..." not "The user can...").**

### 8.2 Rules
- Use **"survivor"**, never "victim" (see `GLOSSARY.md`).
- **No ALL CAPS** except SOS.
- **No exclamation marks** in error messages.
- **No technical jargon** in user-facing text.
- **No graphic descriptions** in help text.
- **Always offer choices** — never command.

### 8.3 Examples

| Bad | Good |
|---|---|
| "Submit report" | "Send your report" |
| "You must upload evidence" | "You can add evidence now, or later" |
| "The case is IN_PROGRESS" | "A caseworker is reviewing your report" |
| "An error occurred" | "Something went wrong. Please try again." |
| "Are you sure you want to delete?" | "Delete this? You can't undo this." |
| "Victim name" | "Survivor name" or "Your name" |

## 9. Accessibility Requirements

See `DESIGN_SYSTEM.md` § Accessibility for the full list. Key rules:

- **WCAG 2.1 AA** minimum.
- **Keyboard-only** navigation works everywhere.
- **Screen reader** compatible (labeled inputs, ARIA where needed).
- **High contrast** support.
- **Large text** support.
- **Reduced motion** support.
- **No color-only** information.

## 10. What NOT to Do

- **Never assume** a user has a private device.
- **Never assume** a user has fast internet.
- **Never assume** a user reads English well.
- **Never assume** a user wants to report.
- **Never make** a user feel judged.
- **Never make** a user feel rushed.
- **Never make** a user feel trapped in a flow.
- **Never show** a survivor's data on a screen they did not ask for.
- **Never send** a notification that reveals case details.
- **Never use** alarming colors except SOS/errors.
- **Never test** with real survivor data.
- **Never design** for Western, well-resourced users only.
- **Never skip** usability testing on a critical flow.

## 11. References

- WHO: *Clinical management of rape and intimate partner violence in emergencies* (survivor-centred care).
- WHO: *Responding to intimate partner violence and sexual violence against women* (clinical handbook).
- UNFPA: *Minimum Standards for Prevention and Response to Gender-Based Violence in Emergencies*.
- NNEDV: *Technology Safety & Privacy Toolkit*.
- W3C: *Web Content Accessibility Guidelines (WCAG) 2.1*.
- NN/g: *Usability Testing 101*.

## 12. Definition of Done (UX Task)

A UX task is done when:
- [ ] Persona needs are documented and addressed.
- [ ] Journey maps are updated if user flow changed.
- [ ] Content (labels, messages) is reviewed against this document.
- [ ] Accessibility is verified (WCAG 2.1 AA).
- [ ] Tone is calm, supportive, survivor-centred.
- [ ] Usability tested with 5 users (for major changes).
- [ ] No real survivor data was used in testing.
- [ ] Reviewed by Product + QA.
- [ ] PR includes screenshots or Figma links.

## The One-Sentence Summary

**UX_RESEARCH.md defines who we build for (survivors, caseworkers, admins), how they use TogetherSafe, and the trauma-informed, survivor-centred, accessible design principles that guide every screen. Every design decision is tested, not assumed.**