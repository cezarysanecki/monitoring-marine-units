import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map, Observable} from "rxjs";
import {ApiToken, LoggedUser, LoginCredentials} from "../model/login-credentials.type";
import {JwtTokenService} from "./jwt-token.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private loggedUserSubject: BehaviorSubject<LoggedUser | null>;
  public loggedUser$: Observable<LoggedUser | null>;

  constructor(private http: HttpClient,
              private jwtTokenService: JwtTokenService) {
    this.loggedUserSubject = new BehaviorSubject<LoggedUser | null>(this.jwtTokenService.getLoggedUser());
    this.loggedUser$ = this.loggedUserSubject.asObservable();
  }

  public get loggedUser(): LoggedUser | null {
    return this.loggedUserSubject.value;
  }

  login(credentials: LoginCredentials): Observable<LoggedUser> {
    return this.http.post<ApiToken>('barentswatch/client/token', credentials)
      .pipe(map(apiToken => {
        const loggedUser = this.jwtTokenService.registerToken(apiToken);
        this.loggedUserSubject.next(loggedUser);
        return loggedUser;
      }));
  }

  refreshToken() {
    if (this.loggedUser) {
      this.http.post<ApiToken>('barentswatch/client/token/refresh', this.loggedUser.tokens.refreshToken)
        .subscribe(apiToken => {
          const loggedUser = this.jwtTokenService.registerToken(apiToken);
          this.loggedUserSubject.next(loggedUser);
        });
    }
  }

  logout() {
    this.jwtTokenService.invalidateToken();
    this.loggedUserSubject.next(null);
  }
}
