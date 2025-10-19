import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';

export interface Notification {
  id: number;
  message: string;
  createdAt: string;
  isRead: boolean;
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
export class NotificationService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  getNotifications(): Observable<Notification[]> {
    return this.http.get<PageDTO<Notification>>(`${this.apiUrl}/notification`).pipe(
      map(response => response.data)
    );
  }

  countUnreadNotifications(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/notification/unread`);
  }

  markAsRead(notificationId: string | null): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/notification/mark-as-read/${notificationId}`, {});
  }

  markAllAsRead(): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/notification/mark-all-as-read`, {});
  }
}
