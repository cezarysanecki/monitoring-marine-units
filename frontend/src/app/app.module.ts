import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {MapComponent} from './map/map.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MarkerService} from "./marker.service";
import {PopupService} from "./popup.service";
import {environment} from "../environments/environment";
import {NavbarComponent} from "./navbar/navbar.component";
import {ToolbarComponent} from "./toolbar/toolbar.component";
import {ApiInterceptor} from "./auth/api.interceptor";
import {AppRoutingModule} from "./app-routing-module";
import { LoginComponent } from './panels/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    NavbarComponent,
    ToolbarComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  providers: [
    {
      provide: "BASE_API_URL",
      useValue: environment.apiUrl,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiInterceptor,
      multi: true
    },
    MarkerService,
    PopupService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
