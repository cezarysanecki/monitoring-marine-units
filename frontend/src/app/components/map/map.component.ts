import {AfterViewInit, Component, Input} from '@angular/core';
import {Subject} from "rxjs";
import {MarkerService} from "./services/marker.service";
import {AuthenticationService} from "../../auth/services/authentication.service";
import {MapService} from "./services/map.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements AfterViewInit {

  @Input()
  resizeMapSubject!: Subject<void>;

  constructor(private mapService: MapService,
              private markerService: MarkerService,
              private authenticationService: AuthenticationService) {
  }

  ngAfterViewInit() {
    this.mapService.initMap();

    this.mapService.map.on('moveend', () => {
      this.markerService.makeVesselsMarkers(this.mapService.map);
      this.mapService.refreshMap();
    });

    this.resizeMapSubject.subscribe(() => {
      this.mapService.refreshMap();
    });

    this.authenticationService.loggedUser$.subscribe(() => {
      this.mapService.map.closePopup();
      this.markerService.makeVesselsMarkers(this.mapService.map);
    });
  }
}
