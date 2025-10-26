import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

interface FriendRequestCreateData {
  senderId: number;
  receiverId: number;
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
}
