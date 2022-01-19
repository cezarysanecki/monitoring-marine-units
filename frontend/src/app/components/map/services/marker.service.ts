import {Injectable} from '@angular/core';
import * as L from 'leaflet';
import {VesselService} from "./vessel.service";
import {VesselRegistry} from "../model/vessel-position.type";
import {PopupService} from "./popup.service";
import * as moment from "moment";

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  markers: L.CircleMarker[] = [];

  constructor(private vesselPositionService: VesselService,
              private popupService: PopupService) {
  }

  makeVesselsMarkers(map: L.Map) {
    this.vesselPositionService.fetchVesselsPositions(map)
      .subscribe((registries: VesselRegistry[]) => {
        this.markers.forEach(marker => map.removeLayer(marker));
        this.markers = [];

        let vesselMarkers = this.prepareMarkers(map, registries);
        let mapMarkers = vesselMarkers.map(vesselMarker => {
          const lat = vesselMarker.data.latitude;
          const lon = vesselMarker.data.longitude;

          const marker = L.circleMarker(
            [lat, lon],
            {
              ...vesselMarker.markerOptions
            });
          marker.bindPopup(vesselMarker.popupTemplate);

          return marker;
        });

        mapMarkers.forEach(mapMarker => {
          mapMarker.addTo(map);
          this.markers.push(mapMarker);
        })
      });
  }

  private prepareMarkers(map: L.Map, registries: VesselRegistry[]): VesselMarker[] {
    if (map.getZoom() < 9) {
      return this.groupRegistriesMarkers(map.getBounds(), registries);
    }
    return this.mapToRegistriesMarkers(registries);
  }

  private groupRegistriesMarkers(bounds: L.LatLngBounds, registries: VesselRegistry[]): VesselMarker[] {
    let vesselMarkers: VesselData[] = [];

    let northWest = bounds.getNorthWest();
    let southEast = bounds.getSouthEast();

    let heightPart = Math.abs(northWest.lat - southEast.lat) / 20;
    let widthPart = Math.abs(northWest.lng - southEast.lng) / 20;

    registries.forEach(registry => {
      let closestMarker = vesselMarkers.filter(marker =>
        Math.abs(marker.latitude - registry.coordinates.latitude) <= widthPart &&
        Math.abs(marker.longitude - registry.coordinates.longitude) <= heightPart);

      if (closestMarker.length > 0) {
        closestMarker[0].vessels.push(registry);
      } else {
        vesselMarkers.push({
          vessels: [registry],
          timestamp: null,
          latitude: registry.coordinates.latitude,
          longitude: registry.coordinates.longitude
        });
      }
    });

    return vesselMarkers.map(marker => {
      return {
        data: marker,
        markerOptions: {
          ...this.prepareMarkerOptions(marker.vessels)
        },
        popupTemplate: this.popupService.makeVesselsPopup(marker.vessels)
      }
    });
  }

  private prepareMarkerOptions(registries: VesselRegistry[]): { color: string; radius: number, fillOpacity?: number } {
    let numberOfVessels = registries.length;
    switch (true) {
      case (numberOfVessels <= 5):
        return {
          radius: 3,
          color: 'purple',
          fillOpacity: 1
        };
      case (numberOfVessels <= 10):
        return {
          radius: 5,
          color: 'red'
        };
      case (numberOfVessels <= 20):
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

  private mapToRegistriesMarkers(registries: VesselRegistry[]): VesselMarker[] {
    const limitTimestamp = moment().subtract(5, 'minutes');

    return registries.map(registry => {
      const registryTimestamp = moment(registry.timestamp);

      return {
        data: {
          vessels: [registry],
          timestamp: registry.timestamp,
          latitude: registry.coordinates.latitude,
          longitude: registry.coordinates.longitude
        },
        markerOptions: {
          color: (registryTimestamp < limitTimestamp) ? 'grey' : 'green',
          radius: 1,
          fillOpacity: 1
        },
        popupTemplate: this.popupService.makeVesselPopup(registry)
      }
    });
  }
}

type VesselMarker = {
  data: VesselData,
  markerOptions: {
    color: string;
    radius: number,
    fillOpacity?: number
  },
  popupTemplate: string
}

type VesselData = {
  vessels: VesselRegistry[],
  timestamp: string | null,
  latitude: number,
  longitude: number
}
