import { Controller, Get, Post, Param, Body, UseGuards, Req } from '@nestjs/common';
import { ClaimService } from './claim.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('v1/claims')
@UseGuards(JwtAuthGuard)
export class ClaimController {
  constructor(private claimService: ClaimService) {}

  @Post()
  async create(@Req() req: any, @Body() data: any) {
    return this.claimService.createClaim(req.user.id, data);
  }

  @Get()
  async getAll(@Req() req: any) {
    return this.claimService.getClaims(req.user.id);
  }

  @Get(':id')
  async getOne(@Param('id') id: string) {
    return this.claimService.getClaim(id);
  }

  @Post(':id/submit')
  async submit(@Param('id') id: string) {
    return this.claimService.submitClaim(id);
  }

  @Post(':id/approve')
  async approve(@Param('id') id: string) {
    return this.claimService.approveClaim(id);
  }

  @Post(':id/reject')
  async reject(@Param('id') id: string) {
    return this.claimService.rejectClaim(id);
  }
}
