import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {VesselRegistry} from "../model/vessel-position.type";
import * as L from 'leaflet';
import {Observable} from "rxjs";
import {Vessel} from "../../panels/app-panel/model/vessel.type";

@Injectable({
  providedIn: 'root'
})
export class VesselService {

  constructor(private http: HttpClient) { }

  fetchVesselsPositions(map: L.Map) {
    let bounds = map.getBounds();

    return this.http.get<VesselRegistry[]>('barentswatch/public/vessels?' +
      `xMin=${bounds.getNorthWest().lng}&` +
      `xMax=${bounds.getSouthEast().lng}&` +
      `yMin=${bounds.getSouthEast().lat}&` +
      `yMax=${bounds.getNorthWest().lat}`
    );
  }

  getUserVessels(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>('barentswatch/monitoring/vessels');
  }

  trackVessel(mmsi: number) {
    return this.http.post<void>('barentswatch/monitoring/track', mmsi);
  }

  suspendTrackingVessel(mmsi: number) {
    return this.http.delete<void>('barentswatch/monitoring/track/suspend', {
      body: mmsi
    });
  }

  removeTrackedVessel(mmsi: number) {
    return this.http.delete<void>('barentswatch/monitoring/track', {
      body: mmsi
    });
  }
}
