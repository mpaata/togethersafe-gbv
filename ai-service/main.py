from fastapi import FastAPI
from app.api import crisis

app = FastAPI(
    title="TogetherSafe AI Service",
    description="Microservice for crisis detection and AI support.",
    version="1.0.0"
)

# Register routers
app.include_router(crisis.router, prefix="/ai", tags=["Crisis Detection"])

@app.get("/health")
def health_check():
    return {"status": "ok", "service": "ai-microservice"}