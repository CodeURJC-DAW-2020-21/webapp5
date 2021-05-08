import { Component } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Router } from "@angular/router";

import { TeamService } from "src/app/services/teams.service";
import { Team } from "../../../models/team.model";


@Component({
    selector: 'newTeam',
    templateUrl: './newTeam.component.html'
})

export class NewTeamComponent {
    
    team: Team;
    error: boolean;
    errorText: string;

    constructor(private httpClient: HttpClient, private router: Router, private teamService: TeamService){
        this.team={
            name: '',
            description: '',
            tournaments: null,
            games: null,
	        creator: null,
	        users: null,
	        admins: null,
	        requests: 0,
	        image: false,
	        nVictories: 0,
            nLoses: 0,
	        recordV: '',
	        recordL: ''
        };
    }

     newTeam(){
        this.teamService.newTeam(this.team).subscribe(
            (team:Team) => this.router.navigate(['/teams']),
            error => {
                this.error = true;
                this.errorText = "Data Error, Invalid Team Name";
                window.scrollTo(0, 0);
            });
     }
}