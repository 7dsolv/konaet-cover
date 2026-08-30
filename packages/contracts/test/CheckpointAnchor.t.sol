// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "../src/CheckpointAnchor.sol";

contract CheckpointAnchorTest {
    function testAnchorAcceptsFirstCheckpoint() public {
        CheckpointAnchor anchor = new CheckpointAnchor();
        bytes32 merkleRoot = keccak256("test_merkle");
        bytes32 stateRoot = keccak256("test_state");
        bytes32 manifestDigest = keccak256("test_manifest");

        anchor.anchor(1, merkleRoot, stateRoot, manifestDigest);
        CheckpointAnchor.CheckpointRecord memory record = anchor.getCheckpoint(1);

        require(record.sequence == 1, "unexpected sequence");
        require(record.merkleRoot == merkleRoot, "unexpected merkle root");
        require(record.stateRoot == stateRoot, "unexpected state root");
        require(record.anchoredBy == address(this), "unexpected anchor author");
    }

    function testAnchorEnforcesMonotonicSequence() public {
        CheckpointAnchor anchor = new CheckpointAnchor();
        bytes32 merkleRoot = keccak256("test");
        bytes32 stateRoot = keccak256("state");
        bytes32 manifestDigest = keccak256("manifest");

        anchor.anchor(1, merkleRoot, stateRoot, manifestDigest);

        (bool success,) = address(anchor).call(
            abi.encodeWithSelector(
                anchor.anchor.selector,
                1,
                merkleRoot,
                stateRoot,
                manifestDigest
            )
        );
        require(!success, "expected monotonic sequence rejection");
    }

    function testAnchorRejectsZeroMerkleRoot() public {
        CheckpointAnchor anchor = new CheckpointAnchor();

        (bool success,) = address(anchor).call(
            abi.encodeWithSelector(
                anchor.anchor.selector,
                1,
                bytes32(0),
                keccak256("state"),
                keccak256("manifest")
            )
        );
        require(!success, "expected zero root rejection");
    }

    function testMissingCheckpointReverts() public {
        CheckpointAnchor anchor = new CheckpointAnchor();

        (bool success,) = address(anchor).call(
            abi.encodeWithSelector(anchor.getCheckpoint.selector, 99)
        );
        require(!success, "expected missing checkpoint rejection");
    }
}
