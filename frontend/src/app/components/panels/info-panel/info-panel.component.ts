import {Component, OnInit} from '@angular/core';
import {MapService} from "../../map/services/map.service";
import {MapState} from "../../map/type/map.type";

@Component({
  selector: 'app-info-panel',
  templateUrl: './info-panel.component.html',
  styleUrls: ['./info-panel.component.scss']
})
export class InfoPanelComponent implements OnInit {

  constructor(private mapService: MapService) {
  }

  ngOnInit() {
    this.mapService.changeState(MapState.PublicMode);
  }
}
