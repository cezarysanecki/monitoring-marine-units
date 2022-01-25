import {AfterViewInit, Component, EventEmitter, Input, Output} from '@angular/core';
import {Subject} from "rxjs";
import {MapService} from "./services/map.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements AfterViewInit {

  @Input()
  resizeMapSubject!: Subject<void>;

  @Output()
  isMapReadyEvent = new EventEmitter<boolean>();

  constructor(private mapService: MapService) {
  }

  ngAfterViewInit() {
    this.isMapReadyEvent.emit(this.mapService.initMap());

    this.resizeMapSubject.subscribe(() => {
      this.mapService.refreshMap();
    });
  }
}
