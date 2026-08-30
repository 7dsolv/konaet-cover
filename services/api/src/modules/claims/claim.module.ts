import { Module } from '@nestjs/common';
import { PrismaService } from '../../prisma.service';
import { ClaimService } from './claim.service';
import { ClaimController } from './claim.controller';
import { AuthModule } from '../auth/auth.module';

@Module({
  imports: [AuthModule],
  controllers: [ClaimController],
  providers: [ClaimService, PrismaService],
  exports: [ClaimService],
})
export class ClaimModule {}
