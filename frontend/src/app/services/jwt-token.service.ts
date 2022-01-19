import {Injectable} from '@angular/core';
import {ApiToken, LoggedUser} from "../types/login-credentials.type";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class JwtTokenService {

  private jwtTokenKey = 'token';
  private jwtHelperService: JwtHelperService;

  constructor() {
    this.jwtHelperService = new JwtHelperService();
  }

  registerToken(apiToken: ApiToken): LoggedUser {
    localStorage.setItem(this.jwtTokenKey, apiToken.token);
    return this.decodeLoggedUser(apiToken.token);
  }

  invalidateToken() {
    localStorage.removeItem(this.jwtTokenKey);
  }

  getLoggedUser(): LoggedUser | null {
    if (localStorage.getItem(this.jwtTokenKey)) {
      return this.decodeLoggedUser(localStorage.getItem(this.jwtTokenKey) as string);
    }
    return null;
  }

  private decodeLoggedUser(token: string): LoggedUser {
    const decodedToken = this.jwtHelperService.decodeToken(token);
    return {
      email: decodedToken.sub,
      groups: decodedToken.groups
    };
  }
}
