import os

KNOWLEDGE_BASE_DIR = os.path.join(os.path.dirname(__file__), "..", "..", "knowledge_base")
SERVICES_FILE = os.path.join(KNOWLEDGE_BASE_DIR, "services.txt")

def load_knowledge_base() -> str:
    """Loads the verified knowledge base into memory."""
    if not os.path.exists(SERVICES_FILE):
        return ""
    with open(SERVICES_FILE, "r", encoding="utf-8") as f:
        return f.read()

def search_knowledge(query: str) -> str:
    """
    Searches the verified knowledge base for relevant information.
    Returns a paragraph, or an empty string if nothing matches.
    """
    kb_content = load_knowledge_base()
    if not kb_content:
        return ""

    query_lower = query.lower()

    # Map user keywords to categories in our services.txt
    category_map = {
        "police": ["police", "criminal", "report", "arrest", "cfpu"],
        "legal": ["legal", "lawyer", "court", "fida", "justice", "protection order", "rights"],
        "medical": ["medical", "hospital", "doctor", "pep", "health", "clinic"],
        "shelter": ["shelter", "safe house", "housing", "accommodation", "set her free", "somero"],
        "counselling": ["counsel", "therapist", "mifumi", "mental health", "emotional support"],
        "government": ["ministry", "government", "mglsd", "policy", "national"],
        "prevention": ["raising voices", "prevention", "advocacy"],
        "helpline": ["helpline", "hotline", "call", "child helpline"]
    }

    for category, keywords in category_map.items():
        if any(word in query_lower for word in keywords):
            # Find the paragraph in the knowledge base that contains this category
            for block in kb_content.split("\n\n"):
                if category.lower() in block.lower():
                    return block.strip()

    # If no specific category is matched, return a general intro
    if any(word in query_lower for word in ["help", "support", "resource", "service"]):
        return "I have verified information on Police, Legal Aid, Medical support, Shelters, Counselling, and Government services. Which one would you like to know more about?"

    return ""