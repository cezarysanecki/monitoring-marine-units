import {Component, OnDestroy, OnInit} from '@angular/core';
import {MapService} from "../../map/services/map.service";
import {MapState} from "../../map/type/map.type";
import {AuthenticationService} from "../../../auth/services/authentication.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-info-panel',
  templateUrl: './info-panel.component.html',
  styleUrls: ['./info-panel.component.scss']
})
export class InfoPanelComponent implements OnInit, OnDestroy {

  private loggedUserSubscription: Subscription;

  constructor(private mapService: MapService,
              private authenticationService: AuthenticationService) {
    this.loggedUserSubscription = this.authenticationService.loggedUser$.subscribe(
      loggedUser => {
        if (!loggedUser) {
          this.mapService.refreshMap();
        }
      }
    );
  }

  ngOnInit() {
    this.mapService.changeState(MapState.PublicMode);
  }

  ngOnDestroy() {
    this.loggedUserSubscription.unsubscribe();
  }
}
