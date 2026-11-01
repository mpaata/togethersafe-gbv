from pydantic import BaseModel, Field
from typing import Optional

class CrisisCheckResponse(BaseModel):
    crisis: bool = Field(..., description="True if a crisis was detected.")
    type: Optional[str] = Field(None, description="The type of crisis detected.")
    urgency: Optional[str] = Field(None, description="CRITICAL, HIGH, or NONE.")
    action: Optional[str] = Field(None, description="Suggested action (e.g., ESCALATE_TO_HUMAN).")


class ChatResponse(BaseModel):
    reply: str = Field(..., description="The AI's supportive response.")
    crisisDetected: bool = Field(..., description="True if a crisis was detected.")
    suggestedResources: list[str] = Field(
        default_factory=list, 
        description="List of suggested resources (e.g., counselling)."
    )
    disclaimer: str = Field(
        default="I am an AI assistant. If you are in immediate danger, please call 999.",
        description="Standard safety disclaimer."
    )