import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {LoggedUser} from "../../types/login-credentials.type";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent {

  @Output()
  panelShownEvent = new EventEmitter<boolean>();

  loggedUser: LoggedUser | null = null;
  isPanelShown = true;

  constructor(private authenticationService: AuthenticationService) {
    this.authenticationService.loggedUser$.subscribe(loggedUser => {
      this.loggedUser = loggedUser;
    });
  }

  togglePanel() {
    this.isPanelShown = !this.isPanelShown;
    this.panelShownEvent.emit(this.isPanelShown);
  }

  logout() {
    this.authenticationService.logout();
  }
}
