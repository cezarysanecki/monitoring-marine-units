import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MarkerService} from "./components/map/services/marker.service";
import {PopupService} from "./components/map/services/popup.service";
import {environment} from "../environments/environment";
import {ApiInterceptor} from "./auth/interceptors/api.interceptor";
import {AppRoutingModule} from "./app-routing-module";
import {MapComponent} from "./components/map/map.component";
import {ToolbarComponent} from "./components/toolbar/toolbar.component";
import {LoginPanelComponent} from "./components/panels/login-panel/login-panel.component";
import {MapPanelComponent} from "./components/panels/map-panel/map-panel.component";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    ToolbarComponent,
    LoginPanelComponent,
    MapPanelComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
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
