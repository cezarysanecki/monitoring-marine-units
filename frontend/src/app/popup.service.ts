import { Injectable } from '@angular/core';
import {VesselRegistry} from "./types/vessel-position.type";

@Injectable({
  providedIn: 'root'
})
export class PopupService {

  constructor() { }

  makeVesselPopup(registry: VesselRegistry): string {
    return `` +
      `<div>MMSI: ${ registry.mmsi }</div>` +
      `<div>Lat: ${ registry.coordinates.latitude }</div>` +
      `<div>Lon: ${ registry.coordinates.longitude }</div>`;
  }
}
