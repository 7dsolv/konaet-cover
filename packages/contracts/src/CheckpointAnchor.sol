// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @title ICheckpointAnchor
 * @notice Minimal interface for anchoring causal checkpoints on-chain
 */
interface ICheckpointAnchor {
    event CheckpointAnchored(
        uint64 indexed sequence,
        bytes32 indexed merkleRoot,
        bytes32 stateRoot,
        bytes32 manifestDigest
    );

    function anchor(
        uint64 sequence,
        bytes32 merkleRoot,
        bytes32 stateRoot,
        bytes32 manifestDigest
    ) external;
}

/**
 * @title CheckpointAnchor
 * @notice Stores checkpoint roots and proofs for verification
 */
contract CheckpointAnchor is ICheckpointAnchor {

    struct CheckpointRecord {
        uint64 sequence;
        bytes32 merkleRoot;
        bytes32 stateRoot;
        bytes32 manifestDigest;
        uint256 timestamp;
        address anchoredBy;
    }

    mapping(uint64 => CheckpointRecord) public checkpoints;
    uint64 public lastSequence;

    constructor() {
        lastSequence = 0;
    }

    /**
     * @notice Record a new checkpoint anchor
     * @dev Must be called in order by sequence
     */
    function anchor(
        uint64 sequence,
        bytes32 merkleRoot,
        bytes32 stateRoot,
        bytes32 manifestDigest
    ) external override {
        require(sequence > lastSequence, "sequence must be monotonic");
        require(merkleRoot != bytes32(0), "merkleRoot required");

        checkpoints[sequence] = CheckpointRecord({
            sequence: sequence,
            merkleRoot: merkleRoot,
            stateRoot: stateRoot,
            manifestDigest: manifestDigest,
            timestamp: block.timestamp,
            anchoredBy: msg.sender
        });

        lastSequence = sequence;

        emit CheckpointAnchored(sequence, merkleRoot, stateRoot, manifestDigest);
    }

    /**
     * @notice Retrieve a checkpoint record
     */
    function getCheckpoint(uint64 sequence)
        external
        view
        returns (CheckpointRecord memory)
    {
        require(checkpoints[sequence].timestamp != 0, "checkpoint not found");
        return checkpoints[sequence];
    }
}
