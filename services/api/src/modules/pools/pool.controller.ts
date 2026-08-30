import { Controller, Get, Post, Param, UseGuards, Req } from '@nestjs/common';
import { PoolService } from './pool.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('v1/pools')
@UseGuards(JwtAuthGuard)
export class PoolController {
  constructor(private poolService: PoolService) {}

  @Get()
  async getAll() {
    return this.poolService.getPools();
  }

  @Get(':id')
  async getOne(@Param('id') id: string) {
    return this.poolService.getPool(id);
  }

  @Post(':id/join')
  async join(@Req() req: any, @Param('id') poolId: string) {
    return this.poolService.joinPool(req.user.id, poolId);
  }

  @Get(':id/members')
  async getMembers(@Param('id') id: string) {
    return this.poolService.getPoolMembers(id);
  }
}
