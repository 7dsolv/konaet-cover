import pytest
from fastapi.testclient import TestClient
from pydantic import ValidationError

from main import DeviceProfile, app, monte_carlo_simulation, wilson_interval


client = TestClient(app)


@pytest.fixture
def profile() -> DeviceProfile:
    return DeviceProfile(
        device_id="device-demo-001",
        make="Samsung",
        model="Demo Phone",
        age_days=120,
        purchase_value_minor=350_000,
        coverage_days=365,
    )


def test_health_check() -> None:
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok", "service": "risk-engine"}


def test_simulation_is_reproducible_with_seed(profile: DeviceProfile) -> None:
    first = monte_carlo_simulation(profile, simulations=2_000, seed=7)
    second = monte_carlo_simulation(profile, simulations=2_000, seed=7)
    assert first == second


def test_simulation_returns_bounded_values(profile: DeviceProfile) -> None:
    result = monte_carlo_simulation(profile, simulations=2_000, seed=42)
    assert 0 <= result.risk_score <= 100
    assert 0 <= result.fraud_probability <= 100
    assert result.risk_level in {"LOW", "MEDIUM", "HIGH", "CRITICAL"}
    assert result.confidence_interval_95["low"] <= result.risk_score
    assert result.risk_score <= result.confidence_interval_95["high"]


def test_profile_rejects_invalid_ranges() -> None:
    with pytest.raises(ValidationError):
        DeviceProfile(
            device_id="",
            make="Test",
            model="Test",
            age_days=-1,
            purchase_value_minor=-1,
            coverage_days=0,
        )


def test_wilson_interval_contains_observed_proportion() -> None:
    low, high = wilson_interval(20, 1_000)
    assert low <= 0.02 <= high
