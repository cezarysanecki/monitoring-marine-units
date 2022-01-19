import {Component} from '@angular/core';
import {AuthenticationService} from "../../../auth/services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-panel',
  templateUrl: './login-panel.component.html',
  styleUrls: ['./login-panel.component.scss']
})
export class LoginPanelComponent {

  email = '';
  password = '';

  constructor(private authenticationService: AuthenticationService,
              private router: Router) {
  }

  login() {
    this.authenticationService.login({
      email: this.email,
      password: this.password
    }).subscribe(() => {
      void this.router.navigate(['']);
    });
  }
}
