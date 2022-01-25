import {Injectable} from "@angular/core";
import {VesselService} from "../../../vessels/services/vessel.service";
import {Bounds, GroupMarker, SingleMarker} from "../type/marker.type";
import {iif, mergeMap, Observable, of} from "rxjs";
import {VesselRegistry} from "../../../vessels/model/vessel.type";
import * as moment from "moment";
import {environment} from "../../../../environments/environment";
import DurationConstructor = moment.unitOfTime.DurationConstructor;

@Injectable({
  providedIn: 'root'
})
export class MarkerPreparerService {

  constructor(private vesselService: VesselService) {
  }

  prepareVesselsMarkers(bounds: Bounds, zoom: number): Observable<GroupMarker[] | SingleMarker[]> {
    return this.vesselService.fetchVesselsPositions(bounds)
      .pipe(
        mergeMap(registries =>
          iif(
            () => zoom < environment.zoomThreshold,
            of(this.mapToGroupMarkers(bounds, registries)),
            of(this.mapToSingleMarkers(registries))
          )
        )
      );
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

  private mapToSingleMarkers(registries: VesselRegistry[]): SingleMarker[] {
    const limitTimestamp = moment().subtract(environment.activeThreshold.value, environment.activeThreshold.unit as DurationConstructor);

    return registries.map(registry => {
      const registryTimestamp = moment(registry.pointInTime.timestamp);

      return {
        mmsi: registry.mmsi,
        active: registryTimestamp < limitTimestamp,
        timestamp: registry.pointInTime.timestamp,
        latitude: registry.pointInTime.coordinates.latitude,
        longitude: registry.pointInTime.coordinates.longitude
      }
    });
  }
}
