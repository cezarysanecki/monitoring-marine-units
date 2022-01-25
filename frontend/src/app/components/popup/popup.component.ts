import {Component, Input} from '@angular/core';
import {AuthenticationService} from "../../auth/services/authentication.service";
import {VesselRegistry} from "../../vessels/model/vessel.type";
import {LoggedUser} from "../../auth/model/login-credentials.type";
import * as moment from 'moment';
import {VesselService} from "../../vessels/services/vessel.service";

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.scss']
})
export class PopupComponent {

  moment = moment;

  @Input()
  registries: VesselRegistry[] = [];

  loggedUser: LoggedUser | null;

  constructor(private authenticationService: AuthenticationService,
              private vesselService: VesselService) {
    this.loggedUser = this.authenticationService.loggedUser;
  }

  trackVessel(mmsi: number) {
    this.vesselService.trackVessel(mmsi)
      .subscribe();
  }
}
