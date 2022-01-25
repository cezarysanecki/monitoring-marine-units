import {Component, OnInit} from '@angular/core';
import {VesselService} from "../../../vessels/services/vessel.service";
import {MapService} from "../../map/services/map.service";
import * as moment from "moment";
import {MonitoredVessel} from "../../../vessels/model/vessel.type";
import {interval} from "rxjs";

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

  private readonly intervalUserVessels = interval(10_000);

  ngOnInit() {
    this.intervalUserVessels.subscribe(() => {
      this.vesselService.getUserVessels()
        .subscribe();
    })

    this.vesselService.trackedVessels$
      .subscribe(vessels => this.vessels = vessels);
  }

  adjust(vessel: MonitoredVessel) {
    let latestPoint = vessel.tracks.flatMap(
      track => track.pointsInTime
    ).sort((point1, point2) => {
      return moment(point1.timestamp) < moment(point2.timestamp) ? 1 : -1
    })[0];

    this.mapService.centerOn(latestPoint.coordinates.latitude, latestPoint.coordinates.longitude);
  }

  suspend(mmsi: number) {

  }

  remove(mmsi: number) {

  }
}
