from fastapi import APIRouter
from app.models.request import CrisisCheckRequest
from app.models.response import CrisisCheckResponse
from app.services.crisis_detector import detect_crisis

router = APIRouter()

@router.post("/detect-crisis", response_model=CrisisCheckResponse)
async def check_crisis(request: CrisisCheckRequest) -> CrisisCheckResponse:
    """
    Detects crisis indicators in a survivor's message.
    Always escalates to human support if a crisis is detected.
    """
    result = detect_crisis(request.text)
    return CrisisCheckResponse(**result)