import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../auth/services/authentication.service";
import {Router} from "@angular/router";
import {MapService} from "../../map/services/map.service";
import {MapState} from "../../map/type/map.type";

@Component({
  selector: 'app-login-panel',
  templateUrl: './login-panel.component.html',
  styleUrls: ['./login-panel.component.scss']
})
export class LoginPanelComponent implements OnInit {

  email = '';
  password = '';

  constructor(private authenticationService: AuthenticationService,
              private mapService: MapService,
              private router: Router) {
  }

  ngOnInit() {
    this.mapService.changeState(MapState.PublicMode);
  }

  login() {
    this.authenticationService.login({
      email: this.email,
      password: this.password
    }).subscribe(() => {
      void this.router.navigate(['/app']);
    });
  }
}
