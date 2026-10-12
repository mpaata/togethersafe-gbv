# AI_SERVICE.md --- Python AI Microservice Specialist Document

## 1. Who This Is For
You are the **AI Service Developer** on TogetherSafe.
You build the Python FastAPI microservice that provides the AI chatbot, risk scoring, and natural language processing.

You work alongside:
- **Backend Lead** --- calls your REST API from Spring Boot
- **JavaFX Developer** --- the desktop app indirectly uses your service via the backend
- **Product + QA** --- tests your AI outputs for safety and accuracy

You own the Python AI service. You do not edit Java code, FXML, or SQL.

## 2. What You Build in v1
- FastAPI microservice (Python 3.12)
- AI chatbot for first-line support and resource signposting
- Risk scoring model (flag high-risk cases based on case text)
- Named Entity Recognition (NER) to extract locations/dates from case text
- Crisis detection (flag cases with suicidal ideation, threats to life)
- Bounded response system (no diagnosis, no legal advice, no medical advice)
- Health check endpoint
- Tests for all critical paths

**Not in v1:** Fine-tuning a custom model, voice recognition, image analysis. Those are v2+.

## 3. CRITICAL Safety Rules (GBV Context)
This is a **GBV application**. The AI must never:
- Provide medical, legal, or psychological diagnosis.
- Replace emergency services or human caseworkers.
- Make promises about outcomes.
- Suggest the survivor confront the perpetrator.
- Provide information that could put the survivor at risk.
- Store or log raw survivor narratives outside the secure server.
- Give legal advice (only general information + signpost to legal aid).

The AI **must**:
- Include a crisis disclaimer on every chatbot response.
- Escalate to human help if suicidal ideation or immediate danger is detected.
- Use survivor-centered language (non-judgmental, empowering).
- Be transparent that it is an AI, not a human.
- Respect the survivor's autonomy (provide options, not commands).

## 4. Technology Stack
| Component | Technology |
|---|---|
| Language | Python 3.12 |
| Framework | FastAPI |
| ASGI Server | Uvicorn |
| NLP | spaCy or Hugging Face Transformers |
| LLM (optional) | Local model via Ollama (privacy-first) or an API |
| Validation | Pydantic |
| Testing | pytest |
| Deployment | systemd (see DEVOPS.md) |

## 5. Package Structure
```text
ai-service/
|
|-- main.py                  <- FastAPI app entry point
|-- requirements.txt
|-- .env.example
|-- README.md
|
|-- app/
|   |-- __init__.py
|   |-- config.py            <- settings via pydantic-settings
|   |
|   |-- api/
|   |   |-- __init__.py
|   |   |-- chat.py          <- POST /chat
|   |   |-- risk.py          <- POST /risk-score
|   |   |-- crisis.py        <- POST /detect-crisis
|   |   +-- health.py        <- GET /health
|   |
|   |-- models/
|   |   |-- __init__.py
|   |   |-- request.py       <- Pydantic request models
|   |   +-- response.py      <- Pydantic response models
|   |
|   |-- services/
|   |   |-- __init__.py
|   |   |-- chatbot.py       <- Chatbot logic (bounded prompts)
|   |   |-- risk_scorer.py   <- Risk scoring model
|   |   |-- crisis_detector.py <- Crisis keyword + NLP detection
|   |   +-- entity_extractor.py <- NER for dates, locations
|   |
|   +-- prompts/
|       |-- system_prompt.txt
|       +-- crisis_responses.json
|
+-- tests/
    |-- test_chat.py
    |-- test_risk.py
    +-- test_crisis.py



    6. API Endpoints (v1)
GET /health
Simple health check.

json
{ "status": "ok", "version": "1.0.0" }
POST /chat
Chat with the AI assistant.
Request:

json
{
  "sessionId": "abc-123",
  "message": "I need help but I'm scared to report.",
  "language": "en"
}
Response:

json
{
  "reply": "I hear you. Your safety comes first. You don't have to report right now...",
  "crisisDetected": false,
  "suggestedResources": ["counselling", "safety-planning"],
  "disclaimer": "I am an AI assistant. If you are in immediate danger, please call 999."
}
POST /risk-score
Score a case for urgency.
Request:

json
{
  "caseDescription": "...",
  "caseType": "PHYSICAL_ASSAULT",
  "hasChildren": true
}
Response:

json
{
  "riskLevel": "HIGH",
  "confidence": 0.82,
  "factors": ["threat to life", "repeat incident", "children involved"]
}
POST /detect-crisis
Detect crisis keywords.
Request:

json
{ "text": "I don't want to live anymore." }
Response:

json
{
  "crisis": true,
  "type": "SUICIDAL_IDEATION",
  "urgency": "CRITICAL",
  "action": "ESCALATE_TO_HUMAN"
}
7. Code Conventions
FastAPI Endpoints
python
from fastapi import APIRouter, HTTPException
from app.models.request import ChatRequest
from app.models.response import ChatResponse
from app.services.chatbot import ChatbotService

router = APIRouter()
chatbot = ChatbotService()

@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest) -> ChatResponse:
    try:
        reply = await chatbot.respond(request.message, request.sessionId)
        return ChatResponse(
            reply=reply.text,
            crisisDetected=reply.crisis_detected,
            suggestedResources=reply.resources,
            disclaimer="I am an AI assistant. If you are in immediate danger, please call 999."
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail="AI service error")
Pydantic Models
python
from pydantic import BaseModel, Field

class ChatRequest(BaseModel):
    sessionId: str = Field(..., min_length=1, max_length=100)
    message: str = Field(..., min_length=1, max_length=2000)
    language: str = Field(default="en", pattern="^(en|sw|lg|fr)$")

class ChatResponse(BaseModel):
    reply: str
    crisisDetected: bool
    suggestedResources: list[str]
    disclaimer: str
Crisis Detection
python
CRISIS_KEYWORDS = {
    "SUICIDAL_IDEATION": ["kill myself", "end my life", "don't want to live"],
    "IMMEDIATE_DANGER": ["he will kill me", "coming for me", "has a weapon"],
    "CHILD_AT_RISK": ["hurting the child", "touching my daughter"]
}

def detect_crisis(text: str) -> dict:
    text_lower = text.lower()
    for crisis_type, keywords in CRISIS_KEYWORDS.items():
        if any(kw in text_lower for kw in keywords):
            return {
                "crisis": True,
                "type": crisis_type,
                "urgency": "CRITICAL",
                "action": "ESCALATE_TO_HUMAN"
            }
    return {"crisis": False}
Logging Rules
Never log the raw case narrative or chat message.

Log only: session id, action, timestamp, crisis flag (true/false).

Use structured logging (JSON format).

8. Bounded Prompts (System Prompt)
The AI chatbot uses a strict system prompt. Example:

text
You are TogetherSafe AI, a supportive assistant for survivors of gender-based violence.

RULES:
- You are NOT a doctor, lawyer, or therapist.
- Never diagnose, prescribe, or give legal advice.
- Never suggest confronting the perpetrator.
- Always prioritize the survivor's safety.
- If the user mentions suicide or immediate danger, respond with:
  "Your safety is the priority. Please call 999 or a crisis line immediately. You are not alone."
- Provide options, not commands.
- Use survivor-centered, non-judgmental language.
- On every response, end with: "I am an AI assistant, not a substitute for professional help."
9. Testing
pytest for unit tests.

Test every crisis keyword.

Test bounded prompt (AI must not give legal advice).

Test that raw narratives are never logged.

Aim for 80%+ coverage on services.

Example:

python
def test_detect_suicidal_ideation():
    result = detect_crisis("I don't want to live anymore.")
    assert result["crisis"] is True
    assert result["type"] == "SUICIDAL_IDEATION"

def test_no_false_positive():
    result = detect_crisis("I had a good day today.")
    assert result["crisis"] is False
10. Privacy and Data Handling
Never persist user messages to disk by default.

If logging is required for improvement, obtain explicit user consent and anonymize.

Use local models (Ollama) when possible to avoid sending data to third-party APIs.

If using an external LLM API, ensure a Data Processing Agreement (DPA) is in place.

Encrypt any cached data.

11. Definition of Done (AI Service)
An AI task is done when:

□ FastAPI endpoint implemented and tested.
□ Pydantic models validate inputs and outputs.
□ Crisis detection tested with real keywords.
□ Bounded prompt tested (AI refuses to give legal/medical advice).
□ No raw survivor text is logged.
□ Response includes the AI disclaimer.
□ pytest coverage ≥80% for services.
□ README updated with new endpoints.
□ PR reviewed and approved.
12. What NOT to Do
Don't log raw chat messages or case narratives.

Don't provide medical, legal, or psychological diagnosis.

Don't suggest confronting the perpetrator.

Don't use an external LLM without a Data Processing Agreement.

Don't skip the AI disclaimer in responses.

Don't ignore crisis keywords --- always escalate.

Don't store conversation history beyond the session without consent.

Don't trust user input --- validate with Pydantic.

Don't hardcode secrets --- use environment variables.

Don't return stack traces to the client.