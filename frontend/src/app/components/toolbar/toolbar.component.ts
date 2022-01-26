import {Component, EventEmitter, Output} from '@angular/core';
import {AuthenticationService} from "../../auth/services/authentication.service";
import {LoggedUser} from "../../auth/model/login-credentials.type";
import {MapService} from "../map/services/map.service";

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
              private mapService: MapService) {
    this.authenticationService.loggedUser$.subscribe(loggedUser => {
      this.loggedUser = loggedUser;
    });
  }

  togglePanel() {
    this.isPanelShown = !this.isPanelShown;
    this.panelShownEvent.emit(this.isPanelShown);
  }

  showPanel() {
    this.isPanelShown = true;
    this.panelShownEvent.emit(this.isPanelShown);
  }

  logout() {
    this.authenticationService.logout();
  }

  centerMap() {
    this.mapService.centerOnInitialPlace();
  }
}
