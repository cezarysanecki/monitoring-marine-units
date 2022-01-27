import {Component, LOCALE_ID, OnInit} from '@angular/core';
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {map, Subject, Subscription} from "rxjs";
import {VesselService} from "./vessels/services/vessel.service";
import {MapService} from "./components/map/services/map.service";
import {MarkerPreparerService} from "./components/map/services/marker-preparer.service";
import {MarkerService} from "./components/map/services/marker.service";
import {CurrentMapParameters, MapState} from "./components/map/type/map.type";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  isPanelShown: boolean = true;
  isPanelShownSubject: Subject<void> = new Subject();

  private subscriptions: Subscription[] = [];

  constructor(private vesselService: VesselService,
              private markerPreparerService: MarkerPreparerService,
              private markerService: MarkerService,
              private mapService: MapService) {
    registerLocaleData(localePl, LOCALE_ID);
  }

  ngOnInit() {
    this.mapService.mapState$.subscribe(mapState => {
      switch (mapState) {
        case MapState.Ready:
        case MapState.PublicMode:
          this.subscriptions.push(this.mapService.mapMoveEnd$.subscribe(markersGroupOptions => {
            this.prepareMarkersForVessels(markersGroupOptions);
          }));
          break;
        case MapState.AppMode:
          this.subscriptions.forEach(subscription => subscription.unsubscribe());
          break;
      }
    })
  }

  handlePanelShownEvent(value: boolean) {
    this.isPanelShown = value;
    this.isPanelShownSubject.next();
  }

  private prepareMarkersForVessels(mapParameters: CurrentMapParameters) {
    this.subscriptions.push(this.vesselService.fetchVesselsPositions(mapParameters.bounds)
      .pipe(
        map(registries => this.markerPreparerService.prepareVesselsMarkersFor(registries, mapParameters)),
        map(preparedMarkers => this.markerService.convertToMapCircleMarkers(preparedMarkers))
      ).subscribe(markers => {
      if ([MapState.PublicMode, MapState.Ready].includes(this.mapService.mapState)) {
        this.mapService.attachMarkersOnMap(markers);
      }
    }));
  }
}
