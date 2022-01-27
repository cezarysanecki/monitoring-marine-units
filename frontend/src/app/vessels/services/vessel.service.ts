import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MonitoredVessel, VesselRegistry} from "../model/vessel.type";
import {Observable} from "rxjs";
import {Bounds} from "../../components/map/type/map.type";

@Injectable({
  providedIn: 'root'
})
export class VesselService {

  constructor(private http: HttpClient) {
  }

  fetchVesselsPositions(bounds: Bounds): Observable<VesselRegistry[]> {
    return this.http.get<VesselRegistry[]>('barentswatch/public/vessels?' +
      `xMin=${bounds.northWestLongitude}&` +
      `xMax=${bounds.southEastLongitude}&` +
      `yMin=${bounds.southEastLatitude}&` +
      `yMax=${bounds.northWestLatitude}`
    );
  }

  getUserVessels(): Observable<MonitoredVessel[]> {
    return this.http.get<MonitoredVessel[]>('barentswatch/monitoring/vessels');
  }

  trackVessel(mmsi: number): Observable<void> {
    return this.http.post<void>('barentswatch/monitoring/track', mmsi);
  }

  suspendTrackingVessel(mmsi: number): Observable<void> {
    return this.http.delete<void>('barentswatch/monitoring/track/suspend', {
      body: mmsi
    });
  }

  removeTrackedVessel(mmsi: number): Observable<void> {
    return this.http.delete<void>('barentswatch/monitoring/track', {
      body: mmsi
    });
  }
}
