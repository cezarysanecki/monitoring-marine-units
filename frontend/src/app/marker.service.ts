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

  makeVesselsMarkers(map: L.Map) {
    this.vesselPositionService.fetchVesselsPositions(map)
      .subscribe((registries: VesselRegistry[]) => {
        this.markers.forEach(marker => map.removeLayer(marker));
        this.markers = [];

        if (map.getZoom() < 10) {
          let vesselMarkers: VesselMarker[] = [];

          let bounds = map.getBounds();
          let northWest = bounds.getNorthWest();
          let southEast = bounds.getSouthEast();

          let heightPart = Math.abs(northWest.lat - southEast.lat) / 20;
          let widthPart = Math.abs(northWest.lng - southEast.lng) / 20;

          registries.forEach(registry => {
            let closestMarker = vesselMarkers.filter(marker =>
              Math.abs(marker.latitude - registry.coordinates.latitude) <= widthPart &&
              Math.abs(marker.longitude - registry.coordinates.longitude) <= heightPart)

            if (closestMarker.length > 0) {
              closestMarker[0].vessels.push(registry);
            } else {
              vesselMarkers.push({
                vessels: [registry],
                latitude: registry.coordinates.latitude,
                longitude: registry.coordinates.longitude});
            }
          });

          vesselMarkers.forEach(vesselMarker => {
            const lat = vesselMarker.latitude;
            const lon = vesselMarker.longitude;
            const marker = L.circleMarker([lat, lon], {radius: 3});

            marker.bindPopup(this.popupService.makeVesselPopup(vesselMarker.vessels[0]))
            marker.addTo(map);

            this.markers.push(marker);
          })
        } else {
          registries.forEach(registry => {
            const lat = registry.coordinates.latitude;
            const lon = registry.coordinates.longitude;
            const marker = L.circleMarker([lat, lon], {radius: 3});

            marker.bindPopup(this.popupService.makeVesselPopup(registry))
            marker.addTo(map);

            this.markers.push(marker);
          });
        }
      });
  }


}

type VesselMarker = {
  vessels: VesselRegistry[],
  latitude: number,
  longitude: number
}
