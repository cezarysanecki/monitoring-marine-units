import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MarkerService} from "./components/map/services/marker.service";
import {environment} from "../environments/environment";
import {ApiInterceptor} from "./auth/interceptors/api.interceptor";
import {AppRoutingModule} from "./app-routing-module";
import {MapComponent} from "./components/map/map.component";
import {ToolbarComponent} from "./components/toolbar/toolbar.component";
import {LoginPanelComponent} from "./components/panels/login-panel/login-panel.component";
import {InfoPanelComponent} from "./components/panels/info-panel/info-panel.component";
import {FormsModule} from "@angular/forms";
import {AppPanelComponent} from "./components/panels/app-panel/app-panel.component";
import {TokenInterceptor} from "./auth/interceptors/token.interceptor";
import {PopupComponent} from './components/popup/popup.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    ToolbarComponent,
    LoginPanelComponent,
    InfoPanelComponent,
    AppPanelComponent,
    PopupComponent,
  ],
  entryComponents: [
    PopupComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot()
  ],
  providers: [
    {
      provide: "BASE_API_URL",
      useValue: environment.apiUrl,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiInterceptor,
      multi: true
    },
    MarkerService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
