from fastapi import APIRouter
from app.models.request import ChatRequest
from app.models.response import ChatResponse
from app.services.crisis_detector import detect_crisis
from app.services.chatbot import generate_reply, DISCLAIMER

router = APIRouter()

@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest) -> ChatResponse:
    # 1. Check for crisis first
    crisis_result = detect_crisis(request.message)

    # 2. Generate bounded reply
    reply = generate_reply(request.message, crisis_result["crisis"])

    # 3. Suggest resources if crisis or resource request
    resources = []
    if crisis_result["crisis"]:
        resources = ["crisis-line", "counselling", "safety-planning"]
    elif "help" in request.message.lower():
        resources = ["counselling", "legal-aid"]

    return ChatResponse(
        reply=reply,
        crisisDetected=crisis_result["crisis"],
        suggestedResources=resources,
        disclaimer=DISCLAIMER
    )