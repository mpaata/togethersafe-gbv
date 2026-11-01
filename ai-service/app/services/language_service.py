import json
import os

KNOWLEDGE_BASE_DIR = os.path.join(os.path.dirname(__file__), "..", "..", "knowledge_base")
CRISIS_FILE = os.path.join(KNOWLEDGE_BASE_DIR, "crisis_responses.json")

def load_crisis_responses() -> dict:
    """Loads the multi-lingual crisis responses."""
    if not os.path.exists(CRISIS_FILE):
        return {}
    with open(CRISIS_FILE, "r", encoding="utf-8") as f:
        return json.load(f)

def get_crisis_message(language: str) -> str:
    """Returns the crisis message in the requested language (fallback: English)."""
    responses = load_crisis_responses()
    lang_data = responses.get(language, responses.get("en", {}))
    return lang_data.get("crisis_message", "Please call 999 immediately.")

def get_disclaimer(language: str) -> str:
    """Returns the disclaimer in the requested language (fallback: English)."""
    responses = load_crisis_responses()
    lang_data = responses.get(language, responses.get("en", {}))
    return lang_data.get("disclaimer", "I am an AI assistant. Please call 999 in an emergency.")