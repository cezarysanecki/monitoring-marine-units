import {Inject, Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {AuthenticationService} from "../services/authentication.service";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  private isRefreshing = false;

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const loggedUser = this.authenticationService.loggedUser;

    if (loggedUser && request.url.startsWith('barentswatch/user')) {
      const tokenReq = request.clone({
        setHeaders: {
          Authorization: `Bearer ${loggedUser.tokens.token}`
        }
      });

      return next.handle(tokenReq).pipe(catchError(error => {
        if (error.status === 401) {
          if (loggedUser && !this.isRefreshing) {
            this.isRefreshing = true;

            this.authenticationService.refreshToken();

            const tokenReq = request.clone({
              setHeaders: {
                Authorization: `Bearer ${loggedUser.tokens.token}`
              }
            });
            this.isRefreshing = false;

            return next.handle(tokenReq);
          }
        }

        return throwError(error);
      }));
    }

    return next.handle(request);
  }
}
