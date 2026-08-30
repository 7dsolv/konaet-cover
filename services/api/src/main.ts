import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Logger } from '@nestjs/common';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.enableCors({
    origin: ['http://localhost:3000', 'http://127.0.0.1:8080', 'http://localhost:8080'],
    credentials: true,
  });

  const port = process.env.API_PORT || 8080;
  const host = process.env.API_HOST || '0.0.0.0';

  await app.listen(port, host, () => {
    Logger.log(`🚀 Server running on http://${host}:${port}`, 'Bootstrap');
  });
}

bootstrap().catch((err) => {
  Logger.error('Failed to start application', err);
  process.exit(1);
});
