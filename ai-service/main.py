from fastapi import FastAPI

app = FastAPI(
    title="TogetherSafe AI Service",
    description="Microservice for crisis detection and AI support.",
    version="1.0.0"
)

@app.get("/health")
def health_check():
    return {"status": "ok", "service": "ai-microservice"}