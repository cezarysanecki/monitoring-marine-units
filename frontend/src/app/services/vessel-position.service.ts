import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {VesselRegistry} from "../types/vessel-position.type";
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class VesselPositionService {

  constructor(private http: HttpClient) { }

  fetchVesselsPositions(map: L.Map) {
    let bounds = map.getBounds();

    return this.http.get<VesselRegistry[]>(
      'barentswatch/vessel/position?' +
      `xMin=${bounds.getNorthWest().lng}&` +
      `xMax=${bounds.getSouthEast().lng}&` +
      `yMin=${bounds.getSouthEast().lat}&` +
      `yMax=${bounds.getNorthWest().lat}`
    );
  }
}
