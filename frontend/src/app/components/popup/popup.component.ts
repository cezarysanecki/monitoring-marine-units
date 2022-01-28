import {Component, Input} from '@angular/core';
import {AuthenticationService} from "../../auth/services/authentication.service";
import {LoggedUser} from "../../auth/model/login-credentials.type";
import * as moment from 'moment';
import {VesselService} from "../../vessels/services/vessel.service";
import {GroupMarker, SingleMarker} from "../map/type/map.type";
import {Router} from "@angular/router";
import {catchError, EMPTY, tap} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.scss']
})
export class PopupComponent {

  moment = moment;

  @Input()
  singleMarker: SingleMarker | null = null;

  @Input()
  groupMarker: GroupMarker | null = null;

  loggedUser: LoggedUser | null;

  constructor(private authenticationService: AuthenticationService,
              private vesselService: VesselService,
              private router: Router,
              private toastrService: ToastrService) {
    this.loggedUser = this.authenticationService.loggedUser;
  }

  trackVessel(singleMarker: SingleMarker) {
    this.vesselService.trackVessel({ mmsi: singleMarker.mmsi, name: singleMarker.name, shipType: singleMarker.shipType })
      .pipe(
        tap(
          () => this.toastrService.success(`Successfully tracked vessel: ${singleMarker.mmsi}`)
        ),
        catchError(err => {
          this.toastrService.error(err.error.message);
          return EMPTY;
        })
      )
      .subscribe();
  }
}
