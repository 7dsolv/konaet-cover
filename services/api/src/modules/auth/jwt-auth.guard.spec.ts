import { ExecutionContext } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { JwtAuthGuard } from './jwt-auth.guard';

describe('JwtAuthGuard', () => {
  const createContext = (authorization?: string) => {
    const request: { headers: { authorization?: string }; user?: unknown } = {
      headers: { authorization },
    };

    const context = {
      switchToHttp: () => ({ getRequest: () => request }),
    } as ExecutionContext;

    return { context, request };
  };

  it('rejects requests without a bearer token', () => {
    const jwtService = { verify: jest.fn() } as unknown as JwtService;
    const guard = new JwtAuthGuard(jwtService);
    const { context } = createContext();

    expect(guard.canActivate(context)).toBe(false);
  });

  it('attaches the normalized user id for a valid token', () => {
    const jwtService = {
      verify: jest.fn().mockReturnValue({ sub: 'user-123', email: 'demo@konaet.local' }),
    } as unknown as JwtService;
    const guard = new JwtAuthGuard(jwtService);
    const { context, request } = createContext('Bearer valid-token');

    expect(guard.canActivate(context)).toBe(true);
    expect(request.user).toEqual({
      sub: 'user-123',
      id: 'user-123',
      email: 'demo@konaet.local',
    });
  });

  it('rejects an invalid token', () => {
    const jwtService = {
      verify: jest.fn().mockImplementation(() => {
        throw new Error('invalid');
      }),
    } as unknown as JwtService;
    const guard = new JwtAuthGuard(jwtService);
    const { context } = createContext('Bearer invalid-token');

    expect(guard.canActivate(context)).toBe(false);
  });
});
