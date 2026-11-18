import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = 'https://prathmesh-resume-backend.hostless.app/api/chat';

  constructor(private http: HttpClient) {}

  sendMessage(message: string): Observable<string> {
    return this.http.get(this.apiUrl, {
      params: {
        message: message
      },
      responseType: 'text'
    });
  }
}