import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

interface RegisterData {
  name: string;
  email: string;
  password: string;
}

interface LoginData {
  email: string;
  password: string;
}

interface ResetPasswordData {
  newPassword: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  register(data: RegisterData) {
    console.log('Registering user with data:', data);
    return this.http.post(`${this.apiUrl}/auth/register`, data);
  }

  login(data: LoginData) {
    return this.http.post(`${this.apiUrl}/auth/login`, data);
  }

  confirmEmail(token: string) {
    return this.http.patch(`${this.apiUrl}/auth/confirm-email?token=${token}`, null);
  }

  forgotPassword(email: string) {
    return this.http.post(`${this.apiUrl}/auth/forgot-password?email=${email}`, null);
  }

  resetPassword(token: string, newPassword: ResetPasswordData) {
    return this.http.patch(`${this.apiUrl}/auth/reset-password?token=${token}`, newPassword);
  }

}
