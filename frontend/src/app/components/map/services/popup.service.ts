import {Injectable} from '@angular/core';
import {VesselRegistry} from "../model/vessel-position.type";
import * as moment from "moment";

@Injectable({
  providedIn: 'root'
})
export class PopupService {

  constructor() { }

  makeVesselPopup(registry: VesselRegistry): string {
    return `<div
                style="
                    margin-bottom: 10px;
                    text-align: center;
                    border-bottom: 2px solid #444554;
                    font-size: 0.8rem"
            >MMSI: ${registry.mmsi}</div>
            <div style="text-align: center; text-decoration: underline;">Last position update:</div>
            <div style="text-align: center">${moment(registry.timestamp).format('DD-MM-YYYY HH:mm:ss')}</div>`;
  }

  makeVesselsPopup(registries: VesselRegistry[]): string {
    let splitRegistries = registries.map(registry => `<div style="text-align: center">${registry.mmsi}</div>`)
      .join("");

    return `<div
                style="
                    margin-bottom: 10px;
                    text-align: center;
                    border-bottom: 2px solid #444554;
                    font-size: 0.8rem"
            >Ships in region (${registries.length}):</div>
            <div style="max-height: 150px; overflow: auto">` +
              splitRegistries +
            `</div>`;
  }
}
