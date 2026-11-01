from pydantic import BaseModel, Field

class CrisisCheckRequest(BaseModel):
    text: str = Field(
        ...,
        min_length=1,
        max_length=2000,
        description="The survivor's message to check for crisis indicators."
    )

class ChatRequest(BaseModel):
    sessionId: str = Field(
        ...,
        min_length=1,
        max_length=100,
        description="A unique session ID for the conversation."
    )
    message: str = Field(
        ...,
        min_length=1,
        max_length=2000,
        description="The survivor's message."
    )
    language: str = Field(
        default="en",
        pattern="^(en|sw|lg|fr)$",
        description="ISO language code (en, sw, lg, fr)."
    )