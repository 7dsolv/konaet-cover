import { Injectable, BadRequestException } from '@nestjs/common';
import { PrismaService } from '../../prisma.service';

@Injectable()
export class ClaimService {
  constructor(private prisma: PrismaService) {}

  async createClaim(userId: string, data: any) {
    const coverage = await this.prisma.coverage.findFirst({
      where: { userId, deviceId: data.deviceId, status: 'active' },
    });

    if (!coverage) {
      throw new BadRequestException('No active coverage for this device');
    }

    const claim = await this.prisma.claim.create({
      data: {
        userId,
        deviceId: data.deviceId,
        coverageId: coverage.id,
        type: data.type || 'LOSS',
        state: 'DRAFT',
        version: 1,
        occurredAt: new Date(data.occurredAt),
        createdAt: new Date(),
      },
    });

    // Emit event
    await this.prisma.causalEvent.create({
      data: {
        eventType: 'CLAIM_CREATED.v1',
        subjectType: 'claim',
        subjectId: claim.id,
        logicalClock: BigInt(1),
        payloadSha3_512: `claim_${claim.id}`,
        payloadKeccak256: `0x${claim.id.slice(0, 8)}`,
        actorRef: userId,
        createdAt: new Date(),
      },
    });

    return claim;
  }

  async getClaims(userId: string) {
    return this.prisma.claim.findMany({
      where: { userId },
      include: { evidence: true },
    });
  }

  async getClaim(id: string) {
    return this.prisma.claim.findUnique({
      where: { id },
      include: { evidence: true },
    });
  }

  async updateClaimState(id: string, state: string) {
    const claim = await this.prisma.claim.update({
      where: { id },
      data: { state, version: { increment: 1 } },
    });

    // Emit state change event
    await this.prisma.causalEvent.create({
      data: {
        eventType: `CLAIM_${state}.v1`,
        subjectType: 'claim',
        subjectId: id,
        logicalClock: BigInt(claim.version),
        payloadSha3_512: `state_${state}`,
        payloadKeccak256: `0x${state.slice(0, 8)}`,
        actorRef: 'system',
        createdAt: new Date(),
      },
    });

    return claim;
  }

  async submitClaim(id: string) {
    return this.updateClaimState(id, 'SUBMITTED');
  }

  async approveClaim(id: string) {
    return this.updateClaimState(id, 'APPROVED');
  }

  async rejectClaim(id: string) {
    return this.updateClaimState(id, 'REJECTED');
  }
}
