import {Injectable} from '@angular/core';
import * as L from 'leaflet';
import {VesselPositionService} from "./services/vessel-position.service";
import {VesselRegistry} from "./types/vessel-position.type";
import {PopupService} from "./popup.service";

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  markers: L.CircleMarker[] = [];

  constructor(private vesselPositionService: VesselPositionService, private popupService: PopupService) { }

  static scaledRadius(val: number, maxVal: number): number {
    return 20 * (val / maxVal);
  }

  makeVesselsMarkers(map: L.Map) {
    this.vesselPositionService.fetchVesselsPositions(map)
      .subscribe((registries: VesselRegistry[]) => {
        this.markers.forEach(marker => map.removeLayer(marker));
        this.markers = [];

        registries.forEach(registry => {
          const lon = registry.point.x;
          const lat = registry.point.y;
          const marker = L.circleMarker([lat, lon], {radius: 3});

          marker.bindPopup(this.popupService.makeVesselPopup(registry))
          marker.addTo(map);

          this.markers.push(marker);
        });
      });
  }
}
