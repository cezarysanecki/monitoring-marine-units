import {Component, OnDestroy, OnInit} from '@angular/core';
import {VesselService} from "../../../vessels/services/vessel.service";
import {MapService} from "../../map/services/map.service";
import * as moment from "moment";
import {MonitoredVessel} from "../../../vessels/model/vessel.type";
import {mergeMap, Subscription, timer} from "rxjs";
import {MapState} from "../../map/type/map.type";
import {PolylineMarkerService} from "../../map/services/polyline-marker.service";

@Component({
  selector: 'app-app-panel',
  templateUrl: './app-panel.component.html',
  styleUrls: ['./app-panel.component.scss']
})
export class AppPanelComponent implements OnInit, OnDestroy {

  vessels: MonitoredVessel[] = [];

  constructor(private mapService: MapService,
              private vesselService: VesselService,
              private polylineMarkerService: PolylineMarkerService) {
  }

  private userVesselsSubscription!: Subscription;

  ngOnInit() {
    this.mapService.changeState(MapState.AppMode);

    this.userVesselsSubscription = timer(0, 10_000).subscribe(() => {
      this.vesselService.getUserVessels()
        .subscribe(vessels => {
          this.markUserMarkers(vessels);
        });
    });
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

    this.mapService.centerOn(latestPoint.coordinates.latitude, latestPoint.coordinates.longitude, 10);
  }

  track(mmsi: number) {
    this.vesselService.trackVessel(mmsi)
      .pipe(
        mergeMap(
          () => this.vesselService.getUserVessels()
        )
      ).subscribe(vessels => this.markUserMarkers(vessels));
  }

  suspend(mmsi: number) {
    this.vesselService.suspendTrackingVessel(mmsi)
      .pipe(
        mergeMap(
          () => this.vesselService.getUserVessels()
        )
      ).subscribe(vessels => this.markUserMarkers(vessels));
  }

  remove(mmsi: number) {
    this.vesselService.removeTrackedVessel(mmsi)
      .pipe(
        mergeMap(
          () => this.vesselService.getUserVessels()
        )
      ).subscribe(vessels => this.markUserMarkers(vessels));
  }

  private markUserMarkers(vessels: MonitoredVessel[]) {
    this.assignSortedVessels(vessels);

    let appMarkers = this.polylineMarkerService.convertToAppMarkers(vessels);
    if (this.mapService.mapState === MapState.AppMode) {
      this.mapService.attachLinesOnMap(appMarkers);
    }
  }

  private assignSortedVessels(monitoredVessels: MonitoredVessel[]) {
    this.vessels = monitoredVessels
      .sort((v1, v2) => v1.isSuspended ? 1 : -1);
  }
}
