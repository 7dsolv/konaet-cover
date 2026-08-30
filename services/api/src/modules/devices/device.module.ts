import { Module } from '@nestjs/common';
import { PrismaService } from '../../prisma.service';
import { DeviceService } from './device.service';
import { DeviceController } from './device.controller';
import { AuthModule } from '../auth/auth.module';

@Module({
  imports: [AuthModule],
  controllers: [DeviceController],
  providers: [DeviceService, PrismaService],
  exports: [DeviceService],
})
export class DeviceModule {}
