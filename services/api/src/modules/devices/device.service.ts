import { Injectable } from '@nestjs/common';
import { PrismaService } from '../../prisma.service';

@Injectable()
export class DeviceService {
  constructor(private prisma: PrismaService) {}

  async createDevice(userId: string, data: any) {
    return this.prisma.protectedDevice.create({
      data: {
        userId,
        nickname: data.nickname,
        make: data.make,
        model: data.model,
        purchaseValueMinor: data.purchaseValueMinor,
        currency: data.currency || 'BRL',
        status: 'active',
      },
    });
  }

  async getDevices(userId: string) {
    return this.prisma.protectedDevice.findMany({
      where: { userId, status: 'active' },
    });
  }

  async getDevice(id: string) {
    return this.prisma.protectedDevice.findUnique({
      where: { id },
    });
  }

  async updateDevice(id: string, data: any) {
    return this.prisma.protectedDevice.update({
      where: { id },
      data,
    });
  }

  async deleteDevice(id: string) {
    return this.prisma.protectedDevice.update({
      where: { id },
      data: { status: 'deleted' },
    });
  }
}
