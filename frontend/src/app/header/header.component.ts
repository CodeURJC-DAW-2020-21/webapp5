import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "../services/login.sevice";
import { UserService } from "../services/users.service";

@Component({
    selector: 'header',
    templateUrl: './header.component.html'
})

export class HeaderComponent {

    constructor(private router: Router, public loginService: LoginService, public usersService: UserService){}

    goToIndex(){this.router.navigate(['/index']);}
    goToLogin(){this.router.navigate(['/login']);}
    goToTournaments(){this.router.navigate(['/tournaments']);}
    goToTeams(){this.router.navigate(['/teams']);}
    goToLeague(){this.router.navigate(['/league']);}
    goToContact(){this.router.navigate(['/contact']);}
    goToProfile(){this.router.navigate(['/user/' + this.loginService.user.name]);}
    goToSettings(){this.router.navigate(['/user/' + this.loginService.user.name + '/settings']);}
    
    logOut(){
        this.loginService.logOut();
        this.router.navigate(['index']);
    }

    userImage(){
        return this.loginService.currentUser().image? 'api/users/' + this.loginService.currentUser().name + '/image' :  '/assets/images/sample_images/user_default.jpg';
    }
}