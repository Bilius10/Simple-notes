import {Injectable, WritableSignal} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
}

export interface PageDTO<T> {
  data: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  getUsers(page: number, size: number): Observable<PageDTO<User>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      ascending: 'true'
    };

    return this.http.get<PageDTO<User>>(`${this.apiUrl}/user`, { params });
  }

  getUserById(id: string | null): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user/${id}`);
  }

  updateUser(userData: { name: string; email: string }): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/user}`, userData);
  }
}
