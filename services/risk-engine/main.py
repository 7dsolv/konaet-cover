"""Motor de risco reproduzível do KONAET COVER (modo demonstração)."""

from math import sqrt
from random import Random

from fastapi import FastAPI, Query
from pydantic import BaseModel, Field
import uvicorn

app = FastAPI(
    title="KONAET Risk Engine",
    description="Monte Carlo risk simulation and fraud detection",
    version="0.1.0"
)

class DeviceProfile(BaseModel):
    device_id: str = Field(min_length=1, max_length=128)
    make: str = Field(min_length=1, max_length=80)
    model: str = Field(min_length=1, max_length=120)
    age_days: int = Field(ge=0, le=3650)
    purchase_value_minor: int = Field(ge=0)
    coverage_days: int = Field(ge=1, le=1095)

class RiskAssessment(BaseModel):
    device_id: str
    risk_score: float  # 0-100
    risk_level: str    # LOW, MEDIUM, HIGH, CRITICAL
    fraud_probability: float
    recommended_premium_basis: int  # basis points (100 = 1%)
    factors: dict
    simulations_count: int
    confidence: float
    confidence_interval_95: dict[str, float]


def wilson_interval(successes: int, total: int, z_score: float = 1.96) -> tuple[float, float]:
    """Intervalo de Wilson para uma proporção binomial."""
    proportion = successes / total
    denominator = 1 + (z_score**2 / total)
    centre = proportion + (z_score**2 / (2 * total))
    margin = z_score * sqrt(
        (proportion * (1 - proportion) / total) + (z_score**2 / (4 * total**2))
    )
    return (
        max(0.0, (centre - margin) / denominator),
        min(1.0, (centre + margin) / denominator),
    )

def monte_carlo_simulation(
    device_profile: DeviceProfile,
    simulations: int = 10_000,
    seed: int | None = None,
) -> RiskAssessment:
    """
    Monte Carlo simulation for device loss probability.

    Factors considered:
    - Device make/model loss statistics
    - Age of device (newer = higher risk)
    - Time coverage (longer coverage = higher probability)
    - Regional factors
    """

    if simulations < 100:
        raise ValueError("simulations must be at least 100")

    rng = Random(seed)

    # Probabilidade-base demonstrativa por fabricante.
    make_risk_map = {
        "Apple": 0.015,
        "Samsung": 0.020,
        "Google": 0.012,
        "OnePlus": 0.018,
        "Xiaomi": 0.025,
    }

    normalized_make = device_profile.make.strip().casefold()
    base_loss_prob = next(
        (risk for make, risk in make_risk_map.items() if make.casefold() == normalized_make),
        0.020,
    )

    # Neste modelo de demonstração, aparelhos novos têm maior exposição a furto.
    age_factor = 1.20 - min(device_profile.age_days / 730.0, 1.0) * 0.35

    # Coverage factor: longer coverage = higher risk
    coverage_factor = 1.0 + (device_profile.coverage_days / 365.0) * 0.30

    # Regional factor (demo: fixed)
    regional_factor = 1.05

    # Adjusted probability
    adjusted_loss_prob = base_loss_prob * age_factor * coverage_factor * regional_factor
    adjusted_loss_prob = min(adjusted_loss_prob, 0.15)  # Cap at 15%

    # Fraud probability (typically 5-15% of legitimate claims)
    fraud_prob = adjusted_loss_prob * rng.uniform(0.05, 0.15)

    # Monte Carlo simulation
    positive_outcomes = 0
    for _ in range(simulations):
        if rng.random() < adjusted_loss_prob:
            positive_outcomes += 1

    simulated_loss_prob = positive_outcomes / simulations

    # Risk score (0-100)
    risk_score = simulated_loss_prob * 100

    # Risk level classification
    if risk_score < 2:
        risk_level = "LOW"
    elif risk_score < 5:
        risk_level = "MEDIUM"
    elif risk_score < 10:
        risk_level = "HIGH"
    else:
        risk_level = "CRITICAL"

    # Premium basis (basis points)
    # Higher risk = higher premium
    premium_basis = round(simulated_loss_prob * 10_000)
    interval_low, interval_high = wilson_interval(positive_outcomes, simulations)

    return RiskAssessment(
        device_id=device_profile.device_id,
        risk_score=round(risk_score, 2),
        risk_level=risk_level,
        fraud_probability=round(fraud_prob * 100, 2),
        recommended_premium_basis=premium_basis,
        factors={
            "base_loss_probability": base_loss_prob,
            "age_factor": round(age_factor, 3),
            "coverage_factor": round(coverage_factor, 3),
            "regional_factor": round(regional_factor, 3),
            "adjusted_loss_probability": round(adjusted_loss_prob, 4),
        },
        simulations_count=simulations,
        confidence=0.95,
        confidence_interval_95={
            "low": round(interval_low * 100, 3),
            "high": round(interval_high * 100, 3),
        },
    )

@app.get("/health")
async def health_check():
    """Health check endpoint."""
    return {"status": "ok", "service": "risk-engine"}

@app.post("/v1/assess-device", response_model=RiskAssessment)
async def assess_device(
    profile: DeviceProfile,
    simulations: int = Query(default=10_000, ge=100, le=100_000),
    seed: int | None = Query(default=None),
) -> RiskAssessment:
    """
    Assess device risk using Monte Carlo simulation.

    Returns risk score, level, and recommended premium basis.
    """
    return monte_carlo_simulation(profile, simulations=simulations, seed=seed)

@app.post("/v1/assess-batch")
async def assess_batch(
    profiles: list[DeviceProfile],
    simulations: int = Query(default=10_000, ge=100, le=100_000),
    seed: int | None = Query(default=None),
):
    """Batch assess multiple devices."""
    results = []
    for index, profile in enumerate(profiles):
        assessment = monte_carlo_simulation(
            profile,
            simulations=simulations,
            seed=None if seed is None else seed + index,
        )
        results.append(assessment)
    return {"assessments": results, "count": len(results)}

@app.get("/v1/factors")
async def get_risk_factors():
    """Get risk factor multipliers."""
    return {
        "make_risk_map": {
            "Apple": 0.015,
            "Samsung": 0.020,
            "Google": 0.012,
            "OnePlus": 0.018,
            "Xiaomi": 0.025,
        },
        "age_multiplier": "1.0 + (days / 365.0) * 0.5",
        "coverage_multiplier": "1.0 + (days / 365.0) * 0.3",
        "regional_multiplier": 1.05,
        "fraud_base": "5-15% of loss probability",
    }

if __name__ == "__main__":
    import os
    port = int(os.getenv("RISK_ENGINE_PORT", 8888))
    host = os.getenv("RISK_ENGINE_HOST", "0.0.0.0")

    uvicorn.run(
        app,
        host=host,
        port=port,
        log_level="info"
    )
