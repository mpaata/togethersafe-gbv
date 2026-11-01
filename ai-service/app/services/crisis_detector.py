# GBV-Critical: This file must never be removed or simplified without review.

CRISIS_KEYWORDS = {
    "SUICIDAL_IDEATION": [
        "kill myself", "end my life", "dont want to live", "don't want to live",
        "want to die", "suicide", "better off dead",
        "no reason to live", "end it all",
        "cant do this anymore", "can't do this anymore",
        "tired of living", "tired of life",
        "no point anymore", "nothing left",
        "cant go on", "can't go on",
        "wish i was dead", "want it to end"
    ],
    "IMMEDIATE_DANGER": [
        "he will kill me", "coming for me", "has a weapon",
        "threatening to kill", "going to hurt me",
        "chasing me", "outside my house", "waiting for me",
        "he found me", "he knows where i am"
    ],
    "CHILD_AT_RISK": [
        "hurting the child", "touching my daughter",
        "touching my son", "beating the kids",
        "abusing my child", "harming my baby",
        "he hurts the children", "touching the children"
    ]
}

def detect_crisis(text: str) -> dict:
    """
    Scans the text for crisis keywords.
    Handles both apostrophe and no-apostrophe variations.
    """
    text_lower = text.lower().replace("'", "").replace("’", "")
    
    for crisis_type, keywords in CRISIS_KEYWORDS.items():
        for keyword in keywords:
            normalized_keyword = keyword.lower().replace("'", "").replace("’", "")
            if normalized_keyword in text_lower:
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