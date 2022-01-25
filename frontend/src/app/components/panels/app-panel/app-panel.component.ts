import {Component, OnInit} from '@angular/core';
import {Vessel} from "./model/vessel.type";
import {VesselService} from "../../../vessels/services/vessel.service";

@Component({
  selector: 'app-app-panel',
  templateUrl: './app-panel.component.html',
  styleUrls: ['./app-panel.component.scss']
})
export class AppPanelComponent implements OnInit {

  vessels: Vessel[] = [];

  constructor(private vesselService: VesselService) {
  }

  ngOnInit() {
    this.vesselService.trackedVessels$
      .subscribe(vessels => this.vessels = vessels);
  }
}
