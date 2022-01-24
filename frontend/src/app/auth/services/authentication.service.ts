import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map, Observable, of} from "rxjs";
import {LoggedUser, LoginCredentials, UserTokens} from "../model/login-credentials.type";
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
    return this.http.post<UserTokens>('barentswatch/authentication/login', credentials)
      .pipe(map(apiToken => {
        const loggedUser = this.jwtTokenService.registerToken(apiToken);
        this.loggedUserSubject.next(loggedUser);
        return loggedUser;
      }));
  }

  refreshToken(): Observable<LoggedUser | null> {
    if (this.loggedUser) {
      return this.http.post<UserTokens>('barentswatch/authentication/refreshtoken', this.loggedUser.tokens.refreshToken, {
        headers: {'Content-Type': 'application/json'}
      })
        .pipe(
          map(apiToken => {
            const loggedUser = this.jwtTokenService.registerToken(apiToken);
            this.loggedUserSubject.next(loggedUser);
            return loggedUser;
          }));
    }
    return of(null);
  }

  logout() {
    this.jwtTokenService.invalidateToken();
    this.loggedUserSubject.next(null);
  }
}
