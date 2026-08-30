# Architectural Decision Records (ADR)

## ADR-001: Modular Monolith Backend Architecture

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** We need a scalable backend that can start simple and evolve without major rewrites.

**Decision:**

Use a modular monolith pattern with NestJS:
- Single deployment artifact initially
- Well-isolated domain modules
- Risk engine as separate Python service
- Clear domain boundaries for future microservices migration

**Rationale:**
- Faster development in MVP phase
- Lower operational complexity vs microservices
- Domain isolation prevents coupling
- Async jobs decouple real-time operations
- Easy to extract services later (Risk, Anchoring, etc)

**Consequences:**
- Single point of failure until horizontally scaled
- Database schema is "monolithic" (joined tables)
- Must maintain strict domain boundaries to enable future split
- Risk engine async calls add latency (mitigated by caching)

---

## ADR-002: Postgres as Source of Truth, Blockchain as Proof

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Blockchain offers immutability but cannot scale to transaction volume; we need both properties.

**Decision:**

- Postgres is authoritative (accounts, claims, decisions, evidence)
- Blockchain records cryptographic commitments only (checkpoint roots, rule hashes)
- Events are immutable via SHA3-512 hashing and causal linking
- Blockchain anchor is async and optional (claim stays valid if anchor fails)

**Rationale:**
- Postgres can handle regulatory queries and complex joins
- Blockchain is expensive and slow for high-frequency data
- Users don't interact with blockchain directly
- Proves immutability via cryptographic hashes, not consensus
- Clear separation of concerns

**Consequences:**
- Requires ETL to generate anchor proofs
- Proof Explorer must cross-reference DB + chain
- Can't rely on blockchain for business logic gates
- Must implement double-entry bookkeeping in DB (not on-chain)

---

## ADR-003: Event Sourcing via Causal Ledger

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Need to reconstruct state, audit decisions, and prove causality.

**Decision:**

- Every state change emits a CausalEvent (immutable)
- Events form a DAG via parent links (can have multiple parents)
- SHA3-512 digest of event payload for uniqueness
- Keccak-256 for EVM integration (bytes32)
- Events never delete; only status changes capture logical deletes

**Rationale:**
- Reconstructs any historical state from events
- Proves decision causality (why a claim was approved)
- Audit trail cannot be altered
- Enables "replay" for debugging
- Supports blockchain verification via merkle roots

**Consequences:**
- Events table grows unbounded (needs archival strategy)
- Projection materialization needed for fast queries
- Must handle event versioning carefully
- Duplicate detection via hash ensures idempotency

---

## ADR-004: Transactional Outbox Pattern

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Cannot lose events even if async job crashes.

**Decision:**

- State change + event emission in same Postgres transaction
- Background worker polls outbox table and processes events
- Outbox entries deleted only after successful processing
- Idempotency key ensures no duplicates if job retries

**Rationale:**
- Database crash = events still exist in outbox
- No need for distributed transactions
- Simple, battle-tested pattern
- Works with standard Postgres (no extra infrastructure)

**Consequences:**
- Small latency between state change and event processing
- Outbox table must be cleaned up periodically
- Workers must be idempotent (Idempotency-Key required)
- Ordering guaranteed per entity (shard key = subjectId)

---

## ADR-005: P-256 Device Key in Android Keystore

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Must sign claims from device; key cannot leave phone.

**Decision:**

- Generate P-256 (secp256r1) signing key in Android Keystore
- Mark as non-exportable when StrongBox available
- Public key registered with challenge/response auth
- Device signs claim submission; server verifies signature

**Rationale:**
- Hardware-backed when available (StrongBox)
- NIST-standard, widely supported
- No payload size limits (vs RSA)
- Server can detect device compromise via attestation
- Prevents claim submissions from compromised backend

**Consequences:**
- Requires API 23+ (we support API 28+)
- Attestation validation is optional but recommended
- Key rotation needs careful choreography
- Private key never leaves Keystore (can't backup/restore)

---

## ADR-006: No Proxy Pattern in MVP Contracts

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Contract upgrades add complexity; MVP doesn't need it.

**Decision:**

- No upgradeable proxies in v0.1.0
- Deploy v2 contract separately, register migration explicitly
- Events already store rule/contract versions
- Access control via immutable registry contracts

**Rationale:**
- Reduces attack surface for MVP
- Simpler security review
- Can add proxies later if needed
- Contracts are minimal anyway (checkpoint storage only)

**Consequences:**
- Contract deployments are irreversible
- Must manage versions in application layer
- Backward compatibility tests required for new versions
- Rollback = deploy new contract + update pointer

---

## ADR-007: Separation of Settlement (SIMULATED vs PARTNER)

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** App must never accidentally process real money without authorization.

**Decision:**

- `SETTLEMENT_MODE` env var gates all financial logic
- Default: `SIMULATED` (all transactions are demo)
- Only `PARTNER` mode calls external settlement adapter
- Mode change requires signed config + environment restart
- No silent fallback to real settlement

**Rationale:**
- Prevents accidental fund transfers
- Supports demo/testing use case
- Regulatory gate is explicit and auditable
- Adapter pattern allows different backends (future)

**Consequences:**
- Config changes are not hot-reloadable for settlement mode
- Integration tests must run in SIMULATED mode
- Production deployment requires explicit configuration review
- Cannot change mode without deployment

---

## ADR-008: Admin API Same Endpoints, Different Scopes

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Avoid API duplication; control access via authorization.

**Decision:**

- Admin and users hit same API endpoints
- Authorization layer checks JWT scope + resource ownership
- Some fields redacted based on scope (e.g., auditor can't see raw evidence by default)
- Admin endpoints prefixed `/v1/admin/` for convenience

**Rationale:**
- Single source of business logic
- Easier to test (one set of tests)
- Consistent API contracts
- Scope system extensible for future roles

**Consequences:**
- Authorization logic must be air-tight
- Easier to accidentally expose data (need thorough testing)
- Query performance may differ between scopes
- Admin scope cannot be granted to regular users (env-based only)

---

## ADR-009: Android Target SDK 36 from Day One

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Google Play will require API 36 after 2026-08-31.

**Decision:**

- `compileSdk = 36` and `targetSdk = 36` in first commit
- No support for API < 28 (baseline modern Android 9)
- Test on API 28/31/33/34/35/36
- Monitor future requirements (rumored 2027 memory limits)

**Rationale:**
- Avoids "target before Play" delays post-release
- APIs 28-36 are stable and supported
- No legacy baggage from older Android versions
- Future-proofs for announced 2027 changes

**Consequences:**
- Cannot use deprecated Android APIs
- Must handle modern permission model throughout
- Breaks on devices < API 28 (< 1% of Play base)
- More aggressive runtime feature detection needed

---

## ADR-010: Risk Simulation Default to SIMULATED Mode

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Risk Lab shows pool health; must not imply real insolvency risk.

**Decision:**

- All risk metrics labeled "simulated" in UI
- Monte Carlo uses demo parameters by default
- Real pool data hidden until regulated operation approved
- Scenarios are "educational" not "projected"

**Rationale:**
- Users should not make decisions based on demo data
- Protects company from liability claims
- Compliance gate ensures honesty before real operation
- Still allows meaningful product testing

**Consequences:**
- Risk Lab cannot show actual pool viability
- Parameters must be clearly visible (transparency)
- Must communicate "not actual predictions" in store listing
- Requires legal review before changing mode

---

## ADR-011: Soft Delete for User Accounts

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** LGPD requires account deletion; also need audit history.

**Decision:**

- User record marked `deleted_at` (soft delete)
- Account access denied immediately (hard logout)
- Deletion job runs asynchronously per retention matrix
- Events keep pseudonymous subject refs (no PII removed)
- Regulatory retention (fraud, money laundering) uses separate PII table

**Rationale:**
- Events remain verifiable and immutable
- Deletion process is async and restartable
- Audit trail survives (without PII)
- Supports compliance investigations

**Consequences:**
- Application must check `deleted_at` on every query
- Separate PII storage for compliance (complex)
- Deletion job can take hours (not instantaneous)
- Must document retention policy clearly

---

## ADR-012: UDF (Unidirectional Data Flow) in Android UI

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** Claims UI has complex states; need reproducibility.

**Decision:**

- ViewModel holds state via Flow<ClaimUiState>
- UI is pure function of state (no side effects)
- User actions emitted as sealed events (ClaimAction)
- ViewModel processes actions → emits new state
- No global singletons or event buses in UI layer

**Rationale:**
- UI state is testable without Compose preview
- Actions are recordable for replay/debugging
- Easier to add undo/redo in future
- Consistent with MVVM + MVI hybrid patterns

**Consequences:**
- More boilerplate than direct DB queries in composables
- State must be immutable (data classes)
- Requires discipline to avoid mutations
- Easier to reason about but slower development initially

---

## ADR-013: No AI-Assisted Claim Decisions (MVP)

**Date:** 2026-08-29

**Status:** ACCEPTED

**Context:** ML models can bias or hallucinate; must not make binding decisions alone.

**Decision:**

- `AI_ASSIST_ENABLED` feature flag defaults to `false`
- AI can provide context/signals (fraud score, similar claims)
- Reviewer must explicitly approve, with reason code
- All AI inputs logged for audit
- Rules engine stays deterministic and explainable

**Rationale:**
- Regulatory and legal liability
- Maintains user trust
- Can add ML safely in future
- Signals can improve over time without changing decision logic

**Consequences:**
- Reviewers must review every claim manually (scalability concern)
- Can experiment with ML offline (batch analysis)
- Future version can use ML with explicit governance
- Community expectations around "AI" not met in v0.1

---

**Legend:**
- ✅ ACCEPTED - Approved as an architectural direction; implementation may still be pending
- 🔄 PROPOSED - Under discussion
- ❌ REJECTED - Decided against
- ⏸️ DEFERRED - Future decision

Implementation progress is tracked separately in [ROADMAP.md](ROADMAP.md). An accepted ADR describes the intended design and does not, by itself, claim that the feature is complete.
