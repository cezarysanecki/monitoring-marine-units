import {Component, OnInit} from '@angular/core';
import {MapService} from "../../map/services/map.service";
import {AuthenticationService} from "../../../auth/services/authentication.service";
import {MapState} from "../../map/type/map.type";

@Component({
  selector: 'app-map-panel',
  templateUrl: './map-panel.component.html',
  styleUrls: ['./map-panel.component.scss']
})
export class MapPanelComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService,
              private mapService: MapService) {
  }

  ngOnInit() {
    this.authenticationService.loggedUser$.subscribe(loggedUser => {
      if (!loggedUser) {
        this.mapService.changeState(MapState.PublicMode);
      }
    })
  }
}
