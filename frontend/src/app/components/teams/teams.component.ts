import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { Team } from "../../models/team.model";
import { TeamService } from "../../services/teams.service";

@Component({
    selector: 'teams',
    templateUrl: './teams.component.html'
})

export class TeamsComponent {
    
    private teams: Team[] = [];

    constructor(private router: Router, private teamService: TeamService){}

    ngOnInit(): void {
        this.teamService.getTeams().subscribe(
          data => {
            this.teams = data;
            console.log('Teams: ', data);
          },
          error => console.error(error)
        );
      }

      getTeams() {
        return this.teams;
      }

      goToNewTeam(){
        this.router.navigate(['/newTeam'])
      }
}