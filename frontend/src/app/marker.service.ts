import {Injectable} from '@angular/core';
import * as L from 'leaflet';
import {VesselHttpService} from "./services/vessel-http.service";
import {VesselRegistry} from "./types/vessel-position.type";
import {PopupService} from "./popup.service";

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  markers: L.CircleMarker[] = [];

  constructor(private vesselPositionService: VesselHttpService, private popupService: PopupService) { }

  makeVesselsMarkers(map: L.Map) {
    this.vesselPositionService.fetchVesselsPositions(map)
      .subscribe((registries: VesselRegistry[]) => {
        this.markers.forEach(marker => map.removeLayer(marker));
        this.markers = [];

        let vesselMarkers = this.prepareMarkers(map, registries);
        vesselMarkers.forEach(vesselMarker => {
          const lat = vesselMarker.latitude;
          const lon = vesselMarker.longitude;
          const marker = L.circleMarker(
            [lat, lon],
            {
              ...this.setRadius(vesselMarker.vessels)
            });

          marker.bindPopup(this.popupService.makeVesselsPopup(vesselMarker.vessels))
          marker.addTo(map);

          this.markers.push(marker);
        });
      });
  }

  private setRadius(registries: VesselRegistry[]): { color: string; radius: number, fillOpacity?: number } {
    let numberOfVessels = registries.length;
    switch (true) {
      case (numberOfVessels < 5):
        return  {
          radius: 3,
          color: 'purple',
          fillOpacity: 1
        };
      case (numberOfVessels < 10):
        return  {
          radius: 5,
          color: 'red'
        };
      case (numberOfVessels < 20):
        return {
          radius: 8,
          color: 'yellow'
        };
      default:
        return {
          radius: 10,
          color: 'blue'
        };
    }
  }

  private prepareMarkers(map: L.Map, registries: VesselRegistry[]): VesselMarker[] {
    if (map.getZoom() < 10) {
      return this.groupRegistriesMarkers(map.getBounds(), registries);
    }
    return this.mapToRegistriesMarkers(registries);
  }

  private groupRegistriesMarkers(bounds: L.LatLngBounds, registries: VesselRegistry[]) {
    let vesselMarkers: VesselMarker[] = [];

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
          longitude: registry.coordinates.longitude
        });
      }
    });

    return vesselMarkers;
  }

  private mapToRegistriesMarkers(registries: VesselRegistry[]): VesselMarker[] {
    return registries.map(registry => {
      return {
        vessels: [registry],
        latitude: registry.coordinates.latitude,
        longitude: registry.coordinates.longitude
      }
    });
  }
}

type VesselMarker = {
  vessels: VesselRegistry[],
  latitude: number,
  longitude: number
}
