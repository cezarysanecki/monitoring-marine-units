import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LoginCredentials, LoginToken} from "../types/login-credentials.type";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginHttpService {

  constructor(private http: HttpClient) { }

  login(credentials: LoginCredentials): Observable<LoginToken> {
    return this.http.post<LoginToken>('barentswatch/client/token', credentials);
  }
}
