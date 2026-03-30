import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/enviroments';
import { tap, of, delay, throwError } from 'rxjs';

// @Injectable({
//   providedIn: 'root',
// })
// export class AuthService {
//   private baseUrl = environment.apiUrl;

//   constructor(private http: HttpClient) {}

//   login(data: { username: string; password: string }) {
//     return this.http.post<any>(`${this.baseUrl}/auth/login`, data).pipe(
//       tap((res) => {
//         localStorage.setItem('token', res.token);
//       }),
//     );
//   }

//   register(data: any) {
//     return this.http.post(`${this.baseUrl}/auth/register`, data);
//   }

//   logout() {
//     localStorage.removeItem('token');
//   }

//   isAuthenticated(): boolean {
//     return !!localStorage.getItem('token');
//   }
// }

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private fakeToken = 'mock-jwt-token-123456';

  login(data: { username: string; password: string }) {
    if (data.username === 'admin' && data.password === '123456') {
      return of({ token: this.fakeToken }).pipe(delay(500));
    }

    return throwError(() => new Error('Invalid credentials'));
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  logout() {
    localStorage.removeItem('token');
  }
  // 🔥 endpoint protegido simulado
  testProtected() {
    const token = this.getToken();

    if (token === this.fakeToken) {
      return of({ message: 'Authorized (mock)' }).pipe(delay(300));
    }

    return of({ message: 'Unauthorized' }).pipe(delay(300));
  }
}
