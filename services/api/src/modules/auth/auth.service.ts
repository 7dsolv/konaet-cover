import { Injectable, BadRequestException, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { PrismaService } from '../../prisma.service';
import { CreateUserDto, LoginDto } from './auth.dto';

@Injectable()
export class AuthService {
  constructor(
    private prisma: PrismaService,
    private jwtService: JwtService,
  ) {}

  async register(createUserDto: CreateUserDto) {
    const existingUser = await this.prisma.user.findUnique({
      where: { email: createUserDto.email },
    });

    if (existingUser) {
      throw new BadRequestException('User already exists');
    }

    const user = await this.prisma.user.create({
      data: {
        email: createUserDto.email,
        status: 'active',
        locale: 'pt-BR',
        createdAt: new Date(),
      },
    });

    const accessToken = this.jwtService.sign(
      { sub: user.id, email: user.email },
      { expiresIn: '10m' },
    );

    const refreshToken = this.jwtService.sign(
      { sub: user.id, type: 'refresh' },
      { expiresIn: '30d' },
    );

    return {
      accessToken,
      refreshToken,
      expiresIn: 600,
      user: {
        id: user.id,
        email: user.email,
        status: user.status,
        locale: user.locale,
        createdAt: user.createdAt,
      },
    };
  }

  async login(loginDto: LoginDto) {
    const user = await this.prisma.user.findUnique({
      where: { email: loginDto.email },
    });

    if (!user) {
      throw new UnauthorizedException('Invalid credentials');
    }

    const accessToken = this.jwtService.sign(
      { sub: user.id, email: user.email },
      { expiresIn: '10m' },
    );

    const refreshToken = this.jwtService.sign(
      { sub: user.id, type: 'refresh' },
      { expiresIn: '30d' },
    );

    return {
      accessToken,
      refreshToken,
      expiresIn: 600,
      user: {
        id: user.id,
        email: user.email,
        status: user.status,
        locale: user.locale,
        createdAt: user.createdAt,
      },
    };
  }

  async refresh(token: string) {
    try {
      const payload = this.jwtService.verify(token);

      const user = await this.prisma.user.findUnique({
        where: { id: payload.sub },
      });

      if (!user) {
        throw new UnauthorizedException('User not found');
      }

      const accessToken = this.jwtService.sign(
        { sub: user.id, email: user.email },
        { expiresIn: '10m' },
      );

      return {
        accessToken,
        expiresIn: 600,
      };
    } catch (error) {
      throw new UnauthorizedException('Invalid token');
    }
  }

  async validateUser(userId: string) {
    const user = await this.prisma.user.findUnique({
      where: { id: userId },
    });

    if (!user || user.status === 'deleted') {
      throw new UnauthorizedException('User not found or deleted');
    }

    return user;
  }
}
