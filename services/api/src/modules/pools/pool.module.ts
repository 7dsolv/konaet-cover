import { Module } from '@nestjs/common';
import { PrismaService } from '../../prisma.service';
import { PoolService } from './pool.service';
import { PoolController } from './pool.controller';
import { AuthModule } from '../auth/auth.module';

@Module({
  imports: [AuthModule],
  controllers: [PoolController],
  providers: [PoolService, PrismaService],
  exports: [PoolService],
})
export class PoolModule {}
