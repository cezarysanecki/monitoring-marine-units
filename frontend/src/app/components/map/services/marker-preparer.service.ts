import {Injectable} from "@angular/core";
import {Bounds, CurrentMapParameters, GroupMarker, SingleMarker} from "../type/map.type";
import {CheckedVesselRegistry, VesselRegistry} from "../../../vessels/model/vessel.type";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MarkerPreparerService {

  constructor() {
  }

  prepareVesselsMarkersFor(registries: CheckedVesselRegistry[], markersGroupOptions: CurrentMapParameters): GroupMarker[] | SingleMarker[] {
    if (markersGroupOptions.zoom < environment.zoomThreshold) {
      return this.mapToGroupMarkers(markersGroupOptions.bounds, registries);
    }
    return this.mapToSingleMarkers(registries);
  }

  private mapToGroupMarkers(bounds: Bounds, registries: VesselRegistry[]): GroupMarker[] {
    let groupMarkers: GroupMarker[] = [];

    let heightPart = Math.abs(bounds.northWestLatitude - bounds.southEastLatitude) / environment.netPart;
    let widthPart = Math.abs(bounds.northWestLongitude - bounds.southEastLongitude) / environment.netPart;

    registries.forEach(registry => {
      let closestMarker = groupMarkers.filter(marker =>
        Math.abs(marker.latitude - registry.pointInTime.coordinates.latitude) <= widthPart &&
        Math.abs(marker.longitude - registry.pointInTime.coordinates.longitude) <= heightPart);

      if (closestMarker.length > 0) {
        closestMarker[0].mmsis.push(registry.mmsi);
      } else {
        groupMarkers.push({
          mmsis: [registry.mmsi],
          latitude: registry.pointInTime.coordinates.latitude,
          longitude: registry.pointInTime.coordinates.longitude
        });
      }
    });

    return groupMarkers;
  }

  private mapToSingleMarkers(registries: CheckedVesselRegistry[]): SingleMarker[] {
    return registries.map(registry => {
      return {
        mmsi: registry.mmsi,
        active: registry.active,
        timestamp: registry.pointInTime.timestamp,
        latitude: registry.pointInTime.coordinates.latitude,
        longitude: registry.pointInTime.coordinates.longitude
      }
    });
  }
}
