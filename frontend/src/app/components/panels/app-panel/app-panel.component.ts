import {Component, OnInit} from '@angular/core';
import {VesselService} from "../../../vessels/services/vessel.service";
import {MapService} from "../../map/services/map.service";
import * as moment from "moment";
import {MonitoredVessel} from "../../../vessels/model/vessel.type";
import {MapState} from "../../map/type/map.type";

@Component({
  selector: 'app-app-panel',
  templateUrl: './app-panel.component.html',
  styleUrls: ['./app-panel.component.scss']
})
export class AppPanelComponent implements OnInit {

  vessels: MonitoredVessel[] = [];

  constructor(private mapService: MapService,
              private vesselService: VesselService) {
  }

  ngOnInit() {
    this.mapService.changeState(MapState.AppMode);
  }

  adjust(vessel: MonitoredVessel) {
    let latestPoint = vessel.tracks.flatMap(
      track => track.pointsInTime
    ).sort((point1, point2) => {
      return moment(point1.timestamp) < moment(point2.timestamp) ? 1 : -1
    })[0];

    this.mapService.centerOn(latestPoint.coordinates.latitude, latestPoint.coordinates.longitude, 10);
  }

  track(mmsi: number) {
    this.vesselService.trackVessel(mmsi).subscribe();
    this.mapService.changeState(MapState.RefreshUserVessels)
  }

  suspend(mmsi: number) {
    this.vesselService.suspendTrackingVessel(mmsi).subscribe();
    this.mapService.changeState(MapState.RefreshUserVessels)
  }

  remove(mmsi: number) {
    this.vesselService.removeTrackedVessel(mmsi).subscribe();
    this.mapService.changeState(MapState.RefreshUserVessels)
  }
}
