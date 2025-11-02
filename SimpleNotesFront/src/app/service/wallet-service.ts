import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';

export interface WalletResponse{
    id: number;
    name: string;
    description: string;
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
export class WalletService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  postWallet(walletData: { name: string; description: string }): Observable<WalletResponse> {
    return this.http.post<WalletResponse>(`${this.apiUrl}/wallet`, walletData);
  }

  getWallets(page: number, size: number): Observable<PageDTO<WalletResponse>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      ascending: 'true'
    };

    return this.http.get<PageDTO<WalletResponse>>(`${this.apiUrl}/wallet`, { params });
  }

  deleteWallet(id : number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/wallet/${id}`);
  }

  getWallet(id : number): Observable<WalletResponse> {
    return this.http.get<WalletResponse>(`${this.apiUrl}/wallet/${id}`);
  }
}
