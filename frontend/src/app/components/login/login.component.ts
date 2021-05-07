import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.sevice';

@Component({
  selector: 'login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  constructor(private router: Router, public loginService: LoginService) { }

  logIn(event: any, user: string, pass: string) {

    event.preventDefault();

    this.loginService.logIn(user, pass);

    this.router.navigate(['index']);
  }

  logOut() {
    this.loginService.logOut();
  }

}