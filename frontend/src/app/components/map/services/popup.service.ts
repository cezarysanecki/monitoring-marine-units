import {Injectable} from '@angular/core';
import {VesselRegistry} from "../model/vessel-position.type";
import * as moment from "moment";
import {AuthenticationService} from "../../../auth/services/authentication.service";

@Injectable({
  providedIn: 'root'
})
export class PopupService {

  constructor(private authenticationService: AuthenticationService) {
  }

  makeVesselPopup(registry: VesselRegistry): string {
    return `<div style="text-align: center;">
                <div
                    style="
                        margin-bottom: 10px;
                        text-align: center;
                        border-bottom: 2px solid #444554;
                        font-size: 0.8rem"
                >MMSI: ${registry.mmsi}</div>
                <div style="text-decoration: underline;">Last position update:</div>
                <div>${moment(registry.pointInTime.timestamp).format('DD-MM-YYYY HH:mm:ss')}</div>` +
                (this.authenticationService.loggedUser ?
                  `<a style="cursor: pointer;" title="Track vessel" class="add-vessel material-icons-outlined md-24">add</a>` : ``) +
            `</div>`;
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
