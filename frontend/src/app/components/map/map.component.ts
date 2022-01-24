import {AfterViewInit, Component, ElementRef, Input} from '@angular/core';
import * as L from 'leaflet';
import {Subject} from "rxjs";
import {MarkerService} from "./services/marker.service";
import {AuthenticationService} from "../../auth/services/authentication.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements AfterViewInit {

  @Input()
  resizeMap!: Subject<void>;

  private map!: L.Map;

  constructor(private markerService: MarkerService,
              private authenticationService: AuthenticationService,
              private elementRef: ElementRef) {
  }

  ngAfterViewInit() {
    this.initMap();
    this.markerService.makeVesselsMarkers(this.map, this.elementRef);

    this.resizeMap.subscribe(() => {
      this.refreshMap();
    });

    this.authenticationService.loggedUser$.subscribe(() => {
      this.map.closePopup();
      this.markerService.makeVesselsMarkers(this.map, this.elementRef);
    });
  }

  private initMap() {
    this.map = L.map('map', {
      center: [60, 10.5],
      zoom: 4,
      maxBounds: [[85, -50], [20, 85]]
    });

    this.map.on('moveend', () => {
      this.markerService.makeVesselsMarkers(this.map, this.elementRef);
      this.refreshMap();
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }

  private refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
  }
}
