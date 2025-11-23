import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {NoteResponse} from './note-service';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';

export interface PageDTO<T> {
  data: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ListWalletUser {
  id: number;
  username: string;
  permission: Permission;
}

export interface WalletUserRequest {
  walletId: number;
  userId: number;
  permission: Permission;
}

export interface Permission {
  canCreate: boolean;
  canUpdate: boolean;
  canDelete: boolean;
}

export interface WalletUserResponse {
  id: number;
  userId: number;
  walletId: number;
  permission: Permission;
}

@Injectable({
  providedIn: 'root'
})
export class WalletUserService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  create(data: WalletUserRequest) {
    return this.http.post<WalletUserResponse>(`${this.apiUrl}/wallet-user`, data);
  }

  get(page: number, size: number, walletId: number) {
    const params: any = {
      page: page,
      size: size,
      ascending: 'true',
    };

    return this.http.get<PageDTO<ListWalletUser>>(`${this.apiUrl}/wallet-user/${walletId}`, { params });
  }

  delete(walletUserId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/wallet-user/${walletUserId}`);
  }

  update(walletUserId: number, data: WalletUserRequest) {
    return this.http.put<WalletUserResponse>(`${this.apiUrl}/wallet-user/${walletUserId}`, data);
  }

}
