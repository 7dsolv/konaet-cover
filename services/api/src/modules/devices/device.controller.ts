import { Controller, Get, Post, Patch, Delete, Param, Body, UseGuards, Req } from '@nestjs/common';
import { DeviceService } from './device.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('v1/devices')
@UseGuards(JwtAuthGuard)
export class DeviceController {
  constructor(private deviceService: DeviceService) {}

  @Post()
  async create(@Req() req: any, @Body() data: any) {
    return this.deviceService.createDevice(req.user.id, data);
  }

  @Get()
  async getAll(@Req() req: any) {
    return this.deviceService.getDevices(req.user.id);
  }

  @Get(':id')
  async getOne(@Param('id') id: string) {
    return this.deviceService.getDevice(id);
  }

  @Patch(':id')
  async update(@Param('id') id: string, @Body() data: any) {
    return this.deviceService.updateDevice(id, data);
  }

  @Delete(':id')
  async delete(@Param('id') id: string) {
    return this.deviceService.deleteDevice(id);
  }
}
