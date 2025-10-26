import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from './user-service';

interface FriendRequestCreateData {
  senderId: number;
  receiverId: number;
}

export interface UserResponse {
  id: number;
  email: string;
  name: string;
}

export interface FriendRequestListResponse {
  id: number;
  userResponse: UserResponse;
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
export class FriendRequestService {

  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) {}

  createFriendRequest(data: FriendRequestCreateData): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/friend-request`, data)
  }

  findAllFriends(page: number, size: number): Observable<PageDTO<FriendRequestListResponse>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      ascending: 'true'
    };

    return this.http.get<PageDTO<FriendRequestListResponse>>(`${this.apiUrl}/friend-request/friends`, { params });
  }

  findAllPendingFriendRequests(page: number, size: number): Observable<PageDTO<FriendRequestListResponse>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      ascending: 'true'
    };

    return this.http.get<PageDTO<FriendRequestListResponse>>(`${this.apiUrl}/friend-request/pendings`, { params });
  }

  delete(id: number): Observable<any>{
    return this.http.delete<any>(`${this.apiUrl}/friend-request/${id}`);
  }

  addFriend(id: number, status: string): Observable<any>{
    const params = {
      status: status,
    };
    return this.http.patch(`${this.apiUrl}/friend-request/${id}`, params)
  }
}
