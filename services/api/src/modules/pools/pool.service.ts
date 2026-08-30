import { Injectable } from '@nestjs/common';
import { PrismaService } from '../../prisma.service';

@Injectable()
export class PoolService {
  constructor(private prisma: PrismaService) {}

  async getPools() {
    return this.prisma.pool.findMany({
      where: { status: 'active' },
    });
  }

  async getPool(id: string) {
    return this.prisma.pool.findUnique({
      where: { id },
    });
  }

  async joinPool(userId: string, poolId: string) {
    const pool = await this.prisma.pool.findUnique({
      where: { id: poolId },
    });

    if (!pool) {
      throw new Error('Pool not found');
    }

    const membership = await this.prisma.membership.create({
      data: {
        userId,
        poolId,
        status: 'active',
        joinedAt: new Date(),
      },
    });

    // Emit causal event
    await this.prisma.causalEvent.create({
      data: {
        eventType: 'POOL_JOINED.v1',
        subjectType: 'pool',
        subjectId: poolId,
        logicalClock: BigInt(1),
        payloadSha3_512: `joined_${userId}`,
        payloadKeccak256: `0x${userId.slice(0, 8)}`,
        actorRef: userId,
        createdAt: new Date(),
      },
    });

    return membership;
  }

  async getPoolMembers(poolId: string) {
    return this.prisma.membership.findMany({
      where: { poolId, status: 'active' },
      include: { user: true },
    });
  }
}
