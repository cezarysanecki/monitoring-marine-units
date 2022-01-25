import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {VesselRegistry} from "../model/vessel.type";
import * as L from 'leaflet';
import {BehaviorSubject, map, mergeMap, Observable} from "rxjs";
import {Vessel} from "../../components/panels/app-panel/model/vessel.type";

@Injectable({
  providedIn: 'root'
})
export class VesselService {

  private trackedVesselsSubject: BehaviorSubject<Vessel[]>;
  public trackedVessels$: Observable<Vessel[]>;

  constructor(private http: HttpClient) {
    this.trackedVesselsSubject = new BehaviorSubject<Vessel[]>([]);
    this.trackedVessels$ = this.trackedVesselsSubject.asObservable();
  }

  fetchVesselsPositions(map: L.Map): Observable<VesselRegistry[]> {
    let bounds = map.getBounds();

    return this.http.get<VesselRegistry[]>('barentswatch/public/vessels?' +
      `xMin=${bounds.getNorthWest().lng}&` +
      `xMax=${bounds.getSouthEast().lng}&` +
      `yMin=${bounds.getSouthEast().lat}&` +
      `yMax=${bounds.getNorthWest().lat}`
    );
  }

  getUserVessels(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>('barentswatch/monitoring/vessels')
      .pipe(
        map(trackedVessels => {
          this.trackedVesselsSubject.next(trackedVessels);
          return trackedVessels;
        })
      );
  }

  trackVessel(mmsi: number): Observable<Vessel[]> {
    return this.http.post<void>('barentswatch/monitoring/track', mmsi)
      .pipe(
        mergeMap(() => this.getUserVessels())
      );
  }

  suspendTrackingVessel(mmsi: number): Observable<Vessel[]> {
    return this.http.delete<void>('barentswatch/monitoring/track/suspend', {
      body: mmsi
    }).pipe(
      mergeMap(() => this.getUserVessels())
    );
  }

  removeTrackedVessel(mmsi: number): Observable<Vessel[]> {
    return this.http.delete<void>('barentswatch/monitoring/track', {
      body: mmsi
    }).pipe(
      mergeMap(() => this.getUserVessels())
    );
  }
}
