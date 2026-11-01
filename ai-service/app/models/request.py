from pydantic import BaseModel, Field

class CrisisCheckRequest(BaseModel):
    text: str = Field(
        ..., 
        min_length=1, 
        max_length=2000,
        description="The survivor's message to check for crisis indicators."
    )