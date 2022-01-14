import {AfterViewInit, Component, Input} from '@angular/core';
import * as L from 'leaflet';
import {MarkerService} from "../marker.service";
import {Subject} from "rxjs";

const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements AfterViewInit {

  private map: any;
  @Input() resizeMap!: Subject<boolean>;

  constructor(private markerService: MarkerService) { }

  private initMap() {
    this.map = L.map('map', {
      center: [ 60, 10.5 ],
      zoom: 4,
      maxBounds: [[ 85, -50 ], [ 20, 85 ]]
    });

    this.map.on('zoomend', () => {
      this.markerService.makeVesselsMarkers(this.map);
      this.refreshMap();
    });

    this.map.on('moveend', () => {
      this.markerService.makeVesselsMarkers(this.map);
      this.refreshMap();
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }

  ngAfterViewInit() {
    this.initMap();
    this.markerService.makeVesselsMarkers(this.map);

    this.resizeMap.subscribe(value => {
      if (!value) {
        this.refreshMap();
      }
    });
  }

  private refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
  }
}
