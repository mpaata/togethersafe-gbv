from fastapi import APIRouter
from app.models.request import ChatRequest
from app.models.response import ChatResponse
from app.services.crisis_detector import detect_crisis
from app.services.chatbot import generate_reply, get_disclaimer
from app.services.language_service import get_crisis_message

router = APIRouter()

@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest) -> ChatResponse:
    crisis_result = detect_crisis(request.message)

    if crisis_result["crisis"]:
        reply = get_crisis_message(request.language)
        return ChatResponse(
            reply=reply,
            crisisDetected=True,
            suggestedResources=["crisis-line", "counselling", "safety-planning"],
            disclaimer=get_disclaimer(request.language)
        )

    reply = generate_reply(request.message, crisis_detected=False)
    resources = []
    if any(word in request.message.lower() for word in ["help", "resource", "support"]):
        resources = ["counselling", "legal-aid"]

    return ChatResponse(
        reply=reply,
        crisisDetected=False,
        suggestedResources=resources,
        disclaimer=get_disclaimer(request.language)
    )