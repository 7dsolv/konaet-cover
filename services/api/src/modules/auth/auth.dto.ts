export class CreateUserDto {
  email!: string;
  password?: string;
}

export class LoginDto {
  email!: string;
  password!: string;
}

export class AuthResponseDto {
  accessToken!: string;
  refreshToken!: string;
  expiresIn!: number;
  user!: {
    id: string;
    email: string;
    status: string;
    locale: string;
    createdAt: Date;
  };
}
