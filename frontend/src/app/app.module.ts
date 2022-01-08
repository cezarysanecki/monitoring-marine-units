import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {MapComponent} from './map/map.component';
import {HttpClientModule} from "@angular/common/http";
import {MarkerService} from "./marker.service";
import {PopupService} from "./popup.service";
import {ShapeService} from "./shape.service";
import {environment} from "../environments/environment";

@NgModule({
  declarations: [
    AppComponent,
    MapComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [
    MarkerService,
    PopupService,
    ShapeService,
    { provide: "BASE_API_URL", useValue: environment.apiUrl }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
