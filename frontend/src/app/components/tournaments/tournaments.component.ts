import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "src/app/services/login.sevice";

@Component({
    selector: 'tournaments',
    templateUrl: './tournaments.component.html'
})

export class TournamentsComponent{
    
    constructor(private router: Router, public loginService: LoginService){
    }

      goToNewTournament(){
        this.router.navigate(['/newTournament'])
      }

      goToLogin(){this.router.navigate(['/login']);}
}