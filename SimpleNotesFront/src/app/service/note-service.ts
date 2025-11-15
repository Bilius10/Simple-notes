import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

interface NoteCreate {
  title: string;
  content: string;
  walletId: number;
}

interface NoteUpdate {
  title: string;
  content: string;
}

export interface PageDTO<T> {
  data: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface NoteResponse {
  id: number;
  title: string;
  content: string;
}

@Injectable({
  providedIn: 'root'
})
export class NoteService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  create(data: NoteCreate) {
    return this.http.post(`${this.apiUrl}/note`, data);
  }

  get(page: number, size: number, walletId: number): Observable<PageDTO<NoteResponse>> {
    const params: any = {
      page: page,
      size: size,
      ascending: 'true',
    };

    return this.http.get<PageDTO<NoteResponse>>(`${this.apiUrl}/note/${walletId}`, { params });
  }

  delete(noteId: number, walletId: number): Observable<void> {
    const params = { walletId: walletId.toString() };
    return this.http.delete<void>(`${this.apiUrl}/note/${noteId}`, { params });
  }

  update(noteId: number, walletId: number, data: NoteUpdate): Observable<NoteResponse> {
    const params = { walletId: walletId.toString() };
    return this.http.put<NoteResponse>(`${this.apiUrl}/note/${noteId}`, data, { params });
  }
}
