import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../auth/services/authentication.service";
import {Router} from "@angular/router";
import {MapService} from "../../map/services/map.service";
import {MapState} from "../../map/type/map.type";
import {catchError, EMPTY} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login-panel',
  templateUrl: './login-panel.component.html',
  styleUrls: ['./login-panel.component.scss']
})
export class LoginPanelComponent implements OnInit {

  login_form_on = true;
  passwords_not_match = false;
  email_is_empty = false;
  passwords_not_long_enough = false;

  email = '';
  password = '';
  password_repeat = '';

  constructor(private authenticationService: AuthenticationService,
              private mapService: MapService,
              private router: Router,
              private toastrService: ToastrService) {
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

  register() {
    let errors = false;
    if (this.email === '') {
      this.email_is_empty = true;
      errors = true;
    }
    if (this.password !== this.password_repeat) {
      this.passwords_not_match = true;
      errors = true;
    }
    if (this.password.length < 4) {
      this.passwords_not_long_enough = true;
      errors = true;
    }

    if (errors) {
      return;
    }

    this.authenticationService.register({
      email: this.email,
      password: this.password
    })
      .pipe(
        catchError(err => {
          if (err.error.parameterViolations) {
            err.error.parameterViolations.forEach((violation: Violation) => {
              let split = violation.path.split('.');
              this.toastrService.error(`${split[split.length - 1]} - ${violation.message}`)
            });
          } else {
            this.toastrService.error(err.error.message);
          }

          return EMPTY;
        })
      )
      .subscribe(() => {
        this.password = '';
        this.password_repeat = '';
        this.login_form_on = true;
      });
  }

  resetErrors() {
    this.passwords_not_match = false;
    this.email_is_empty = false;
    this.passwords_not_long_enough = false;
  }

  toggleForm() {
    this.email = '';
    this.password = '';
    this.password_repeat = '';
    this.login_form_on = !this.login_form_on;
  }
}

type Violation = {
  message: string;
  path: string;
}
