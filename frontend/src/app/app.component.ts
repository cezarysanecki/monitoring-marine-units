import {Component, LOCALE_ID} from '@angular/core';
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {Subject} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  isPanelShown: boolean = true;
  isPanelShownSubject: Subject<boolean> = new Subject();

  constructor() {
    registerLocaleData(localePl, LOCALE_ID);
  }

  togglePanel() {
    this.isPanelShown = !this.isPanelShown;
    this.isPanelShownSubject.next(this.isPanelShown);
  }
}
