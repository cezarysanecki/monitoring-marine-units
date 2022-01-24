import {Injectable} from '@angular/core';
import {ApiToken, LoggedUser} from "../model/login-credentials.type";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class JwtTokenService {

  private userTokensKey = 'userTokens';
  private jwtHelperService: JwtHelperService;

  constructor() {
    this.jwtHelperService = new JwtHelperService();
  }

  registerToken(apiToken: ApiToken): LoggedUser {
    localStorage.setItem(this.userTokensKey, JSON.stringify(apiToken));
    return this.decodeLoggedUser(apiToken);
  }

  invalidateToken() {
    localStorage.removeItem(this.userTokensKey);
  }

  getLoggedUser(): LoggedUser | null {
    const userTokensString = localStorage.getItem(this.userTokensKey);
    if (userTokensString) {
      const userTokens = JSON.parse(userTokensString) as ApiToken;
      return this.decodeLoggedUser(userTokens);
    }
    return null;
  }

  private decodeLoggedUser(apiToken: ApiToken): LoggedUser {
    const decodedToken = this.jwtHelperService.decodeToken(apiToken.token);
    return {
      email: decodedToken.sub,
      groups: decodedToken.groups,
      tokens: apiToken
    };
  }
}
