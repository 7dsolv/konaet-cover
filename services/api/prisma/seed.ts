import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

async function main() {
  console.log('🌱 Seeding database...');

  // Clean database
  await prisma.user.deleteMany();
  await prisma.pool.deleteMany();

  // Create demo user
  const demoUser = await prisma.user.create({
    data: {
      email: 'demo@konaet.local',
      status: 'active',
      locale: 'pt-BR',
      createdAt: new Date(),
    },
  });

  console.log(`✓ Created demo user: ${demoUser.email}`);

  // Create demo reviewer
  const reviewer = await prisma.user.create({
    data: {
      email: 'reviewer@konaet.local',
      status: 'active',
      locale: 'pt-BR',
      createdAt: new Date(),
    },
  });

  console.log(`✓ Created reviewer: ${reviewer.email}`);

  // Create demo pools
  const pools = [];
  const riskLevels = ['LOW', 'MEDIUM', 'HIGH'];

  for (let i = 0; i < 3; i++) {
    const pool = await prisma.pool.create({
      data: {
        code: `DEMO_${riskLevels[i]}_${Date.now()}`,
        name: `${riskLevels[i]} Risk Pool`,
        mode: 'DEMO',
        ruleVersionId: `v1.0.0`,
        capacityMinor: BigInt(1000000000 * (i + 1)),
        status: 'active',
        createdAt: new Date(),
      },
    });
    pools.push(pool);
    console.log(`✓ Created ${pool.name}`);
  }

  // Create demo device
  const device = await prisma.protectedDevice.create({
    data: {
      userId: demoUser.id,
      nickname: 'My Phone',
      make: 'Samsung',
      model: 'Galaxy S24',
      purchaseValueMinor: 300000, // R$ 3.000
      currency: 'BRL',
      status: 'active',
      createdAt: new Date(),
    },
  });

  console.log(`✓ Created device: ${device.nickname}`);

  // Join pools
  for (const pool of pools) {
    const membership = await prisma.membership.create({
      data: {
        poolId: pool.id,
        userId: demoUser.id,
        joinedAt: new Date(),
        status: 'active',
      },
    });

    const coverage = await prisma.coverage.create({
      data: {
        membershipId: membership.id,
        deviceId: device.id,
        poolId: pool.id,
        userId: demoUser.id,
        ruleVersionId: 'v1.0.0',
        startsAt: new Date(),
        endsAt: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000), // 90 days
        status: 'active',
        createdAt: new Date(),
      },
    });

    console.log(`✓ Created coverage in ${pool.name}`);
  }

  // Create demo claim
  const claim = await prisma.claim.create({
    data: {
      userId: demoUser.id,
      coverageId: (
        await prisma.coverage.findFirst({ where: { userId: demoUser.id } })
      )!.id,
      deviceId: device.id,
      type: 'LOSS',
      state: 'APPROVED',
      version: 1,
      occurredAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000), // 7 days ago
      submittedAt: new Date(Date.now() - 6 * 24 * 60 * 60 * 1000),
      createdAt: new Date(),
    },
  });

  console.log(`✓ Created demo claim`);

  // Create causal event
  const event = await prisma.causalEvent.create({
    data: {
      eventType: 'CLAIM_APPROVED.v1',
      subjectType: 'claim',
      subjectId: claim.id,
      logicalClock: BigInt(1),
      payloadSha3_512: 'demo_hash_sha3_512',
      payloadKeccak256: '0xdeadbeef',
      actorRef: `reviewer_${reviewer.id}`,
      createdAt: new Date(),
    },
  });

  console.log(`✓ Created causal event`);

  console.log('\n✅ Database seeded successfully!');
}

main()
  .catch((e) => {
    console.error('❌ Seeding failed:', e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
