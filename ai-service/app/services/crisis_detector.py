# GBV-Critical: This file must never be removed or simplified without review.

CRISIS_KEYWORDS = {
    "SUICIDAL_IDEATION": [
        "kill myself", "end my life", "don't want to live",
        "want to die", "suicide", "better off dead",
        "no reason to live", "end it all"
    ],
    "IMMEDIATE_DANGER": [
        "he will kill me", "coming for me", "has a weapon",
        "threatening to kill", "going to hurt me",
        "chasing me", "outside my house"
    ],
    "CHILD_AT_RISK": [
        "hurting the child", "touching my daughter",
        "touching my son", "beating the kids",
        "abusing my child"
    ]
}

def detect_crisis(text: str) -> dict:
    """
    Scans the text for crisis keywords.
    Returns a dict indicating whether a crisis was detected.
    """
    text_lower = text.lower()
    
    for crisis_type, keywords in CRISIS_KEYWORDS.items():
        for keyword in keywords:
            if keyword in text_lower:
                return {
                    "crisis": True,
                    "type": crisis_type,
                    "urgency": "CRITICAL",
                    "action": "ESCALATE_TO_HUMAN"
                }
    
    return {
        "crisis": False,
        "type": None,
        "urgency": None,
        "action": None
    }