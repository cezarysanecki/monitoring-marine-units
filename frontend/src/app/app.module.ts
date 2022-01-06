import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {
  DataLabelService,
  LegendService,
  MapsComponent,
  MapsModule,
  MapsTooltipService
} from "@syncfusion/ej2-angular-maps";

@NgModule({
  imports: [
    BrowserModule, MapsModule
  ],
  declarations: [
    AppComponent
  ],
  bootstrap: [
    AppComponent
  ],
  providers: [
    MapsComponent,
    LegendService,
    DataLabelService,
    MapsTooltipService
  ]
})
export class AppModule { }
