from app.services.rag_service import search_knowledge
from app.services.language_service import get_disclaimer

def generate_reply(message: str, crisis_detected: bool) -> str:
    if crisis_detected:
        return "Your safety is the priority. Please call 999 or a crisis line immediately."
    
    knowledge = search_knowledge(message)
    if knowledge:
        return f"{knowledge}\n\nI am an AI assistant. If you are in immediate danger, please call 999."
    
    message_lower = message.lower()
    
    if any(word in message_lower for word in ["help", "resource", "support", "shelter", "counsel", "police", "legal"]):
        return "I hear you. I have verified information on Police, Legal Aid, Medical support, Shelters, Counselling, and Government services. Which one would you like to know more about? I am an AI assistant, not a substitute for professional help."
    
    if any(word in message_lower for word in ["safe", "safety", "plan", "escape"]):
        return "Your safety matters. I can help you think through a safety plan. Would you like to start? I am an AI assistant, not a substitute for professional help."
    
    return "I hear you. Your safety is the most important thing. You are not alone. Would you like me to suggest some support resources near you? I am an AI assistant, not a substitute for professional help."