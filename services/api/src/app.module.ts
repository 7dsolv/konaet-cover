import { Module } from '@nestjs/common';
import { AuthModule } from './modules/auth/auth.module';
import { DeviceModule } from './modules/devices/device.module';
import { PoolModule } from './modules/pools/pool.module';
import { ClaimModule } from './modules/claims/claim.module';

@Module({
  imports: [AuthModule, DeviceModule, PoolModule, ClaimModule],
})
export class AppModule {}
