import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app.services.chatbot import generate_reply, DISCLAIMER

def test_crisis_reply():
    reply = generate_reply("I want to die", crisis_detected=True)
    assert "999" in reply
    assert "crisis" in reply.lower()
    assert "priority" in reply.lower()

def test_normal_help_request():
    reply = generate_reply("I need help finding a shelter", crisis_detected=False)
    assert "help" in reply.lower() or "resource" in reply.lower()

def test_default_reply():
    reply = generate_reply("Hello there", crisis_detected=False)
    assert "safety" in reply.lower() or "alone" in reply.lower()

def test_disclaimer_exists():
    assert DISCLAIMER is not None
    assert "AI" in DISCLAIMER
    assert "999" in DISCLAIMER