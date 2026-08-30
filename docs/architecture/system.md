# KONAET COVER - Architecture

## System Overview

KONAET COVER is an experimental device-protection platform. The repository currently contains:
- an **Android prototype** for onboarding, authentication and home flows;
- a **backend API prototype** for identity, devices, pools and claims;
- a runnable **risk engine** for reproducible Monte Carlo simulations;
- a minimal **smart contract** for checkpoint anchoring.

The reviewer web interface and several integrations described below are planned architecture, not completed features. See [ROADMAP.md](../ROADMAP.md) for the current implementation status.

## Core Architecture Decision

The system is built as a **modular monolith**:
- Single backend service with well-isolated domains
- Separate risk engine (Python) for computational tasks
- Separate smart contracts and a future admin web as isolated artifacts
- Allows future vertical scaling

## Key Principles

1. **Postgres is source of truth** - blockchain only stores proofs and anchors
2. **Transactional outbox** - state changes and events in same transaction
3. **Idempotent operations** - every mutation is retryable
4. **Event sourcing** - causal ledger reconstructs any state
5. **No PII on-chain** - only cryptographic proofs

## Domain Boundaries

```
┌─────────────────────────────────────────────────────────┐
│                    API Gateway / BFF                     │
├──────────┬──────────────┬──────────────┬────────────────┤
│ Identity │ Device +     │ Claims &     │ Causal Audit & │
│ & Auth   │ Pools        │ Evidence     │ Blockchain     │
└──────────┴──────────────┴──────────────┴────────────────┘
           │                           │
           └─────────────┬─────────────┘
                         │
           ┌─────────────┴─────────────┐
           │                           │
        ┌──▼──┐                    ┌───▼───┐
        │  DB │                    │ Cache │
        └──┬──┘                    └───┬───┘
           │                           │
           └──────────────┬────────────┘
                          │
    ┌─────────────────────┼──────────────────────┐
    │                     │                      │
 ┌──▼──┐            ┌─────▼────┐         ┌──────▼──┐
 │ S3  │            │Risk Eng  │         │Blockchain│
 │Jobs │            │(Python)  │         │ (Anvil)  │
 └─────┘            └──────────┘         └──────────┘
```

## Data Flow

1. **Mutation Request** → API validates & authorizes
2. **Domain Logic** → Processes and generates events
3. **Transactional Outbox** → Persists state + events atomically
4. **Event Workers** → Async processing (anchoring, notifications, etc)
5. **Causal Ledger** → Append-only, immutable event stream

## Key Components

### Android App (Kotlin + Compose)
- Single Activity + Navigation
- UDF (Unidirectional Data Flow)
- ViewModels + Repositories
- Jetpack Compose for UI
- P-256 key in Android Keystore

### API Backend (TypeScript + NestJS)
- NestJS framework
- Prisma ORM
- PostgreSQL database
- Redis for jobs & caching
- OpenAPI documentation is planned

### Risk Service (Python + FastAPI)
- Separate process for computational tasks
- NumPy/SciPy for Monte Carlo simulations
- Configurable random seeds for reproducibility
- Accepts HTTP requests from main API

### Contracts (Solidity + Foundry)
- CheckpointAnchor: store merkle/state roots
- RuleVersionRegistry: rule bundle hashes (planned)
- ValidatorRegistry: reviewer keys (planned)
- No upgradeable proxies (no proxy pattern in MVP)

### Admin Web (planned)
- Review queue management
- Claim visualization
- Event audit trail
- Feature flag management

## Integration Points

The integration points in this section describe the intended boundaries. Only the locally testable subset listed in the project README is currently implemented.

### Android ↔ API
- REST + JWT auth
- Device signature on mutations
- Passkey for sensitive actions
- Play Integrity token (prod)

### API ↔ Risk Engine
- HTTP requests (Uvicorn)
- Async job dispatch via Redis

### API ↔ Blockchain
- Worker writes checkpoints
- Reads via Web3.js/ethers

### Admin ↔ API
- Same API endpoints with different scopes
- Admin-specific query endpoints

## Deployment Patterns

### Development
- Docker Compose local stack
- All services running locally
- Hot reload enabled

### Staging (planned)
- Multi-container deployment
- Managed database (optional)
- Testnet blockchain
- Play Integrity validation (beta)

### Production (future, subject to legal and security review)
- Only when regulated
- Settlement adapter enabled
- Mainnet or partner chain
- Hardware security module for keys

## Security Model

The entries below are design requirements. They are not a security certification and several controls remain on the roadmap.

- **Authentication:** Passkey + OTP + device challenge
- **Authorization:** Scope-based (user, reviewer, ops, auditor)
- **Transport:** HTTPS only (prod) + TLS pinning (future)
- **Storage:** Secrets in KMS/environment, not in git
- **Audit:** Every decision logged + signed

## Observability

- **Traces:** OpenTelemetry end-to-end
- **Metrics:** Prometheus-compatible
- **Logs:** JSON structured, PII redacted
- **Alerts:** Health checks + business metric anomalies
