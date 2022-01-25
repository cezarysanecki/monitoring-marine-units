import {Injectable} from "@angular/core";
import {VesselService} from "../../../vessels/services/vessel.service";

@Injectable({
  providedIn: 'root'
})
export class TrackerService {

  constructor(private vesselService: VesselService) {
  }


}
