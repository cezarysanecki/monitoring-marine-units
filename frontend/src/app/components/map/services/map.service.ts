import {Injectable} from "@angular/core";
import * as L from 'leaflet';
import {MarkerService} from "./marker.service";
import {MarkerPreparerService} from "./marker-preparer.service";
import {Bounds} from "../type/marker.type";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  map!: L.Map;
  markers: L.CircleMarker[] = [];

  constructor(private markerPreparerService: MarkerPreparerService,
              private markerService: MarkerService) {
  }

  initMap() {
    this.map = L.map('map', {
      center: [60, 10.5],
      zoom: 4,
      maxBounds: [[85, -50], [20, 85]]
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    this.map.on('moveend', () => {
      this.markVesselsOnMap();
    });
  }

  markVesselsOnMap() {
    this.markerPreparerService.prepareVesselsMarkers(this.getBounds(), this.map.getZoom())
      .pipe(
        map(markers => {
          return this.markerService.convertToMapCircleMarkers(markers)
        })
      )
      .subscribe(markers => {
        this.markers.forEach(marker => this.map.removeLayer(marker));
        this.markers = markers;

        this.markers.forEach(mapMarker => {
          mapMarker.addTo(this.map);
        })
      });
    this.refreshMap();
  }

  refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
  }

  private getBounds(): Bounds {
    const bounds = this.map.getBounds();
    return {
      northWestLongitude: bounds.getNorthWest().lng,
      northWestLatitude: bounds.getNorthWest().lat,
      southEastLongitude: bounds.getSouthEast().lng,
      southEastLatitude: bounds.getSouthEast().lng
    }
  }
}
