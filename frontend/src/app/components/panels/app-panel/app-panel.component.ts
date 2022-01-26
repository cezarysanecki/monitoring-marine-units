import {Component, OnDestroy, OnInit} from '@angular/core';
import {VesselService} from "../../../vessels/services/vessel.service";
import {MapService} from "../../map/services/map.service";
import * as moment from "moment";
import {MonitoredVessel} from "../../../vessels/model/vessel.type";
import {Subscription, timer} from "rxjs";
import {MapState} from "../../map/type/map.type";

@Component({
  selector: 'app-app-panel',
  templateUrl: './app-panel.component.html',
  styleUrls: ['./app-panel.component.scss']
})
export class AppPanelComponent implements OnInit, OnDestroy {

  vessels: MonitoredVessel[] = [];

  constructor(private mapService: MapService,
              private vesselService: VesselService) {
  }

  private userVesselsSubscription!: Subscription;

  ngOnInit() {
    this.mapService.changeState(MapState.AppMode);

    this.userVesselsSubscription = timer(0, 10_000).subscribe(() => {
      this.vesselService.getUserVessels()
        .subscribe(vessels => this.vessels = vessels);
    });

    this.mapService.centerOnInitialPlace();
  }

  ngOnDestroy() {
    this.userVesselsSubscription.unsubscribe();
    this.vessels = [];
    this.mapService.changeState(MapState.PublicMode);
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
