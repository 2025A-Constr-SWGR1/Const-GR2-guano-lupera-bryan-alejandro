import { Injectable } from '@nestjs/common';

@Injectable()
export class AppService {
  getHello(): string {
    console.error('error');
    console.warn('warn!');
    return 'Hello World!!';
  }
}
