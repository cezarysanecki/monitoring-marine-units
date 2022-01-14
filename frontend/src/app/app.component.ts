import {Component, LOCALE_ID} from '@angular/core';
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  panelShown: boolean = true;

  constructor() {
    registerLocaleData(localePl, LOCALE_ID);
  }

  togglePanel() {
    this.panelShown = !this.panelShown;
  }
}
