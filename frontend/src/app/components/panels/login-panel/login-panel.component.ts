import {Component} from '@angular/core';
import {LoginHttpService} from "../../../http-services/login-http.service";

@Component({
  selector: 'app-login-panel',
  templateUrl: './login-panel.component.html',
  styleUrls: ['./login-panel.component.scss']
})
export class LoginPanelComponent {

  email = '';
  password = '';

  constructor(private loginHttpService: LoginHttpService) { }

  login() {
    this.loginHttpService.login({
      email: this.email,
      password: this.password
    }).subscribe();
  }
}
