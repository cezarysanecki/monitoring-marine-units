import {Component, LOCALE_ID, OnInit} from '@angular/core';
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {map, mergeMap, Observable, Subject, Subscription, timer} from "rxjs";
import {VesselService} from "./vessels/services/vessel.service";
import {MapService} from "./components/map/services/map.service";
import {MarkerPreparerService} from "./components/map/services/marker-preparer.service";
import {MarkerService} from "./components/map/services/marker.service";
import {CurrentMapParameters, MapState} from "./components/map/type/map.type";
import {MonitoredVessel} from "./vessels/model/vessel.type";
import {PolylineMarkerService} from "./components/map/services/polyline-marker.service";
import {CircleMarker} from "leaflet";
import {UserVesselService} from "./vessels/services/user-vessel.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  isPanelShown: boolean = true;
  isPanelShownSubject: Subject<void> = new Subject();

  private canAttachElementsToMap = false;

  private subscriptions: Subscription[] = [];

  constructor(private vesselService: VesselService,
              private userVesselService: UserVesselService,
              private polylineMarkerService: PolylineMarkerService,
              private markerPreparerService: MarkerPreparerService,
              private markerService: MarkerService,
              private mapService: MapService) {
    registerLocaleData(localePl, LOCALE_ID);
  }

  ngOnInit() {
    this.mapService.mapState$.subscribe(mapState => {
        this.subscriptions.forEach(subscription => subscription.unsubscribe());

        switch (mapState) {
          case MapState.Ready:
            this.canAttachElementsToMap = true;
            break;
          case MapState.PublicMode:
            if (this.canAttachElementsToMap) {
              this.subscriptions.push(this.mapService.mapMoveEnd$.subscribe(markers => this.prepareMarkersForVessels(markers)));
            }
            break;
          case MapState.AppMode:
          case MapState.RefreshUserVessels:
            if (this.canAttachElementsToMap) {
              this.subscriptions.push(timer(0, 10_000)
                .pipe(
                  mergeMap(() => this.vesselService.getUserVessels())
                )
                .subscribe(vessels => {
                  const sortedUserVessels = this.sortVessels(vessels);
                  this.userVesselService.pushUserVessels(sortedUserVessels);
                  this.markUserMarkers(sortedUserVessels);
                }));
            }
            break;
        }
      }
    );
  }

  handlePanelShownEvent(value: boolean) {
    this.isPanelShown = value;
    this.isPanelShownSubject.next();
  }

  private prepareMarkersForVessels(mapParameters: CurrentMapParameters) {
    let bounds = mapParameters.bounds;
    if (bounds.southEastLatitude > bounds.northWestLatitude || bounds.northWestLongitude > bounds.southEastLongitude) {
      return;
    }

    this.subscriptions.push(this.vesselService.fetchVesselsPositions(bounds)
      .pipe(
        map(registries => this.markerPreparerService.prepareVesselsMarkersFor(registries, mapParameters)),
        map(preparedMarkers => this.markerService.convertToMapCircleMarkers(preparedMarkers))
      ).subscribe(markers => {
        this.mapService.attachMarkersOnMap(markers);
      }));
  }

  private sortVessels(monitoredVessels: MonitoredVessel[]): MonitoredVessel[] {
    return monitoredVessels.sort((v1) => v1.isSuspended ? 1 : -1);
  }

  private markUserMarkers(vessels: MonitoredVessel[]) {
    let appMarkers = this.polylineMarkerService.convertToAppMarkers(vessels);
    this.mapService.attachLinesOnMap(appMarkers);
  }
}
