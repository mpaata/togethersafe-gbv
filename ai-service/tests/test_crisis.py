import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app.services.crisis_detector import detect_crisis

def test_detect_suicidal_ideation():
    result = detect_crisis("I don't want to live anymore.")
    assert result["crisis"] is True
    assert result["type"] == "SUICIDAL_IDEATION"
    assert result["urgency"] == "CRITICAL"
    assert result["action"] == "ESCALATE_TO_HUMAN"

def test_detect_immediate_danger():
    result = detect_crisis("He will kill me, he has a weapon.")
    assert result["crisis"] is True
    assert result["type"] == "IMMEDIATE_DANGER"

def test_detect_child_at_risk():
    result = detect_crisis("He is touching my daughter.")
    assert result["crisis"] is True
    assert result["type"] == "CHILD_AT_RISK"

def test_no_false_positive():
    result = detect_crisis("I had a good day today.")
    assert result["crisis"] is False
    assert result["type"] is None

def test_detect_soft_crisis_signal():
    result = detect_crisis("I cant do this anymore")
    assert result["crisis"] is True
    assert result["type"] == "SUICIDAL_IDEATION"

def test_detect_soft_crisis_with_apostrophe():
    result = detect_crisis("I can't do this anymore")
    assert result["crisis"] is True
    assert result["type"] == "SUICIDAL_IDEATION"