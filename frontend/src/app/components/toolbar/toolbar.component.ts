import {Component, EventEmitter, Output} from '@angular/core';
import {AuthenticationService} from "../../auth/services/authentication.service";
import {LoggedUser} from "../../auth/model/login-credentials.type";
import {MapService} from "../map/services/map.service";
import {ToolbarService} from "./services/toolbar.service";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent {

  private readonly FILTER_ON_KEY = 'filterOn';
  filterOn;

  @Output()
  panelShownEvent = new EventEmitter<boolean>();

  loggedUser: LoggedUser | null = null;
  isPanelShown = true;

  constructor(private authenticationService: AuthenticationService,
              private mapService: MapService,
              private toolbarService: ToolbarService) {
    if (!localStorage.getItem(this.FILTER_ON_KEY)) {
      localStorage.setItem(this.FILTER_ON_KEY, FilterOn.OFF);
      this.filterOn = FilterOn.OFF;
    } else {
      this.filterOn = localStorage.getItem(this.FILTER_ON_KEY);
    }
    this.toolbarService.changeFilterOn(this.filterOn === FilterOn.ON);

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

  toggleFilter() {
    this.filterOn = this.filterOn === FilterOn.OFF ? FilterOn.ON : FilterOn.OFF;
    localStorage.setItem(this.FILTER_ON_KEY, this.filterOn);
    this.toolbarService.changeFilterOn(this.filterOn === FilterOn.ON);
  }
}

enum FilterOn {
  ON = 'yes',
  OFF = 'no'
}
