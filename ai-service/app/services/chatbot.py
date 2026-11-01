# GBV-Critical: The AI must never give medical, legal, or psychological advice.
# It must always prioritize survivor safety and escalate crises.

DISCLAIMER = "I am an AI assistant. If you are in immediate danger, please call 999."

SAFE_REPLIES = {
    "default": "I hear you. Your safety is the most important thing. You are not alone. Would you like me to suggest some support resources near you?",
    "resource_request": "I can help with that. We have counselling, legal aid, medical support, and safe shelters. Would you like me to find a service near you?",
    "safety_planning": "Your safety matters. I can help you think through a safety plan. Would you like to start?",
}

def generate_reply(message: str, crisis_detected: bool) -> str:
    """
    Generates a bounded, supportive reply.
    Never gives medical or legal advice.
    """
    if crisis_detected:
        return "Your safety is the priority. Please call 999 or a crisis line immediately. You are not alone."
    
    message_lower = message.lower()
    
    if any(word in message_lower for word in ["help", "resource", "support", "shelter", "counsel"]):
        return SAFE_REPLIES["resource_request"]
    
    if any(word in message_lower for word in ["safe", "safety", "plan", "escape"]):
        return SAFE_REPLIES["safety_planning"]
    
    return SAFE_REPLIES["default"]