import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpStatusCode} from "@angular/common/http";
import {catchError, Observable, switchMap, throwError} from "rxjs";
import {AuthenticationService} from "../services/authentication.service";
import {LoggedUser} from "../model/login-credentials.type";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  private static URL_WITH_TOKEN = 'barentswatch/monitoring';

  private isRefreshing = false;

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.startsWith(TokenInterceptor.URL_WITH_TOKEN)) {
      const apiRequest = this.updateHeaders(request, this.authenticationService.loggedUser);

      return next.handle(apiRequest).pipe(catchError(error => {
        if (!this.isRefreshing && error.status === HttpStatusCode.Unauthorized) {
          this.isRefreshing = true;

          return this.authenticationService.refreshToken()
            .pipe(
              switchMap(loggedUser => {
                const apiRequest = this.updateHeaders(request, loggedUser);
                this.isRefreshing = false;

                return next.handle(apiRequest);
              }),
              catchError(error => {
                this.isRefreshing = false;
                this.authenticationService.logout();

                return throwError(error);
              })
            );
        }

        return throwError(error);
      }));
    }

    return next.handle(request);
  }

  private updateHeaders(request: HttpRequest<any>, loggedUser: LoggedUser | null): HttpRequest<any> {
    if (loggedUser) {
      return request.clone({
        setHeaders: {
          Authorization: `Bearer ${loggedUser.tokens.apiToken}`
        }
      });
    }
    return request;
  }
}
