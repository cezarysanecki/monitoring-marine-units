import {Injectable} from "@angular/core";
import * as L from 'leaflet';
import {CurrentMapParameters} from "../type/marker.type";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private mapMoveEndSubject!: BehaviorSubject<CurrentMapParameters>;
  public mapMoveEnd$!: Observable<CurrentMapParameters>;

  map!: L.Map;
  markers: L.CircleMarker[] = [];

  constructor() {
  }

  initMap(): boolean {
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

    this.mapMoveEndSubject = new BehaviorSubject<CurrentMapParameters>(this.getCurrentMapParameters());
    this.mapMoveEnd$ = this.mapMoveEndSubject.asObservable();

    this.map.on('moveend', () => {
      this.mapMoveEndSubject.next(this.getCurrentMapParameters())
    });

    return true;
  }

  clearMarkers() {
    this.markers.forEach(marker => this.map.removeLayer(marker));
    this.markers = [];
  }

  attachMarkersOnMap(markers: L.CircleMarker[]) {
    this.markers.forEach(marker => this.map.removeLayer(marker));
    this.markers = markers;

    this.markers.forEach(mapMarker => {
      mapMarker.addTo(this.map);
    });

    this.refreshMap();
  }

  refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
  }

  centerOn(latitude: number, longitude: number) {
    this.map.flyTo([latitude, longitude], 10);
  }

  private getCurrentMapParameters(): CurrentMapParameters {
    const bounds = this.map.getBounds();

    return {
      bounds: {
        northWestLongitude: bounds.getNorthWest().lng,
        northWestLatitude: bounds.getNorthWest().lat,
        southEastLongitude: bounds.getSouthEast().lng,
        southEastLatitude: bounds.getSouthEast().lng
      },
      zoom: this.map.getZoom()
    }
  }
}
