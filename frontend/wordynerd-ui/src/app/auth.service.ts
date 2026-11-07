import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface LoginResponse {
  userId: number;
  email: string;
  role: string;
  accessToken: string;
  refreshToken: string;
  message: string;
}

export interface SignupResponse {
  userId: number;
  email: string;
  role: string;
  message: string;
}

export interface UserProfile {
  id: number;
  email: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  healthCheck(): Observable<string> {
    return this.http.get('/health-check', { responseType: 'text' });
  }

  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/login', credentials);
  }

  refresh(refreshToken: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/refresh', { refreshToken });
  }

  signup(credentials: { email: string; password: string }): Observable<SignupResponse> {
    return this.http.post<SignupResponse>('/signup', credentials);
  }

  getCurrentUser(accessToken: string): Observable<UserProfile> {
    return this.http.get<UserProfile>('/user/me', {
      headers: new HttpHeaders({ Authorization: `Bearer ${accessToken}` }),
    });
  }

  logout(accessToken: string): Observable<string> {
    return this.http.post('/logout', {}, {
      headers: new HttpHeaders({ Authorization: `Bearer ${accessToken}` }),
      responseType: 'text',
    });
  }
}
