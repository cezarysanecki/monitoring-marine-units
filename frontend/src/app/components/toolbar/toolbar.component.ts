import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AuthenticationService} from "../../auth/services/authentication.service";
import {LoggedUser} from "../../auth/model/login-credentials.type";
import {Router} from "@angular/router";

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

  constructor(private authenticationService: AuthenticationService,
              private router: Router) {
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
    void this.router.navigate(['/']);
  }
}
