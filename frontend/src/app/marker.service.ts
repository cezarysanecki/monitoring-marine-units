import {Injectable} from '@angular/core';
import * as L from 'leaflet';
import {VesselPositionService} from "./services/vessel-position.service";
import {mergeMap, timer} from "rxjs";
import {VesselRegistry} from "./types/vessel-position.type";
import {PopupService} from "./popup.service";

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  markers: L.Marker[] = [];

  constructor(private vesselPositionService: VesselPositionService, private popupService: PopupService) { }

  static scaledRadius(val: number, maxVal: number): number {
    return 20 * (val / maxVal);
  }

  makeVesselsMarkers(map: L.Map) {
    timer(0, 10 * 1000)
      .pipe(
        mergeMap(() => this.vesselPositionService.fetchVesselsPositions(map))
      )
      .subscribe((registries: VesselRegistry[]) => {
        this.markers.forEach(marker => map.removeLayer(marker));
        this.markers = [];

        registries.forEach(registry => {
          const lon = registry.point.x;
          const lat = registry.point.y;
          const marker = L.marker([lat, lon]);

          marker.bindPopup(this.popupService.makeVesselPopup(registry))
          marker.addTo(map);

          this.markers.push(marker);
        });
      });
  }
}
