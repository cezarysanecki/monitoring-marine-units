import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MonitoredVessel, VesselRegistry} from "../model/vessel.type";
import {BehaviorSubject, map, mergeMap, Observable} from "rxjs";
import {Bounds} from "../../components/map/type/marker.type";

@Injectable({
  providedIn: 'root'
})
export class VesselService {

  private trackedVesselsSubject: BehaviorSubject<MonitoredVessel[]>;
  public trackedVessels$: Observable<MonitoredVessel[]>;

  constructor(private http: HttpClient) {
    this.trackedVesselsSubject = new BehaviorSubject<MonitoredVessel[]>([]);
    this.trackedVessels$ = this.trackedVesselsSubject.asObservable();
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
    return this.http.get<MonitoredVessel[]>('barentswatch/monitoring/vessels')
      .pipe(
        map(trackedVessels => {
          this.trackedVesselsSubject.next(trackedVessels);
          return trackedVessels;
        })
      );
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
