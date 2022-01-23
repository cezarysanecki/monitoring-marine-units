import {Component, OnInit} from '@angular/core';
import {animate, state, style, transition} from "@angular/animations";
import {VesselService} from "./services/vessel.service";
import {Vessel} from "./model/vessel.type";

@Component({
  selector: 'app-app-panel',
  templateUrl: './app-panel.component.html',
  styleUrls: ['./app-panel.component.scss'],
  animations: [
    state('open', style({
      height: '200px',
      opacity: 1,
      backgroundColor: 'yellow'
    })),
    transition('open => closed', [
      animate('1s')
    ]),
  ]
})
export class AppPanelComponent implements OnInit {

  vessels: Vessel[] = [];

  constructor(private vesselService: VesselService) { }

  ngOnInit() {
    this.vesselService.getUserVessels()
      .subscribe(vessels => this.vessels = vessels);
  }
}
