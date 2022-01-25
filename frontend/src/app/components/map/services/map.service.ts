import {Injectable} from "@angular/core";
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  map!: L.Map;

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
  }

  refreshMap() {
    setTimeout(() => {
      this.map.invalidateSize(true)
    }, 200);
  }
}
