import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app.services.chatbot import generate_reply
from app.services.language_service import get_disclaimer

def test_crisis_reply():
    reply = generate_reply("I want to die", crisis_detected=True)
    assert "999" in reply
    assert "crisis" in reply.lower()
    assert "priority" in reply.lower()

def test_normal_help_request_returns_verified_info():
    reply = generate_reply("I need help finding a shelter", crisis_detected=False)
    assert "shelter" in reply.lower() or "help" in reply.lower()
    assert "999" in reply

def test_default_reply():
    reply = generate_reply("Hello there", crisis_detected=False)
    assert "safety" in reply.lower() or "alone" in reply.lower()

def test_disclaimer_exists_in_english():
    disclaimer = get_disclaimer("en")
    assert disclaimer is not None
    assert "AI" in disclaimer
    assert "999" in disclaimer

def test_disclaimer_exists_in_luganda():
    disclaimer = get_disclaimer("lg")
    assert disclaimer is not None
    assert "999" in disclaimer

def test_disclaimer_exists_in_swahili():
    disclaimer = get_disclaimer("sw")
    assert disclaimer is not None
    assert "999" in disclaimer