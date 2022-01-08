import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {MapComponent} from './map/map.component';
import {HttpClientModule} from "@angular/common/http";
import {MarkerService} from "./marker.service";
import {PopupService} from "./popup.service";
import {ShapeService} from "./shape.service";
import {environment} from "../environments/environment";
import {NavbarComponent} from "./navbar/navbar.component";
import {ToolbarComponent} from "./toolbar/toolbar.component";

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    NavbarComponent,
    ToolbarComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
  ],
  providers: [
    {provide: "BASE_API_URL", useValue: environment.apiUrl},
    MarkerService,
    PopupService,
    ShapeService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
