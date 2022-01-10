import {Injectable} from '@angular/core';
import {VesselRegistry} from "./types/vessel-position.type";

@Injectable({
  providedIn: 'root'
})
export class PopupService {

  constructor() { }

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
