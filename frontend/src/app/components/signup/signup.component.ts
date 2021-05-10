import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { error } from 'selenium-webdriver';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/users.service';
import { LoginService } from '../../services/login.sevice';

@Component({
  selector: 'signup',
  templateUrl: './signup.component.html'
})
export class SignupComponent {

    user: User;

    constructor(private router: Router, activatedRoute: ActivatedRoute, public loginService: LoginService, public userService: UserService) { 
        this.user={
            name: '',
            email: '',
            encodedPassword: '',
            roles: null,
            riot: '',
            blizzard: '',
            psn: '',
            xbox: '',
            steam: '',
            image: false,
            team: null
        };
    }

    createUser(){
        this.userService.newUser(this.user).subscribe(
            (user:User) => {
                this.router.navigate(['/index']),
                alert('User created correctly')
            },
            error => alert('Error creating new user: ' + error)
            );
    }

    goToLogin() {
        this.router.navigate(['login']);
    }

}