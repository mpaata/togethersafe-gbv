from pydantic import BaseModel, Field
from typing import Optional

class CrisisCheckResponse(BaseModel):
    crisis: bool = Field(..., description="True if a crisis was detected.")
    type: Optional[str] = Field(None, description="The type of crisis detected.")
    urgency: Optional[str] = Field(None, description="CRITICAL, HIGH, or NONE.")
    action: Optional[str] = Field(None, description="Suggested action (e.g., ESCALATE_TO_HUMAN).")