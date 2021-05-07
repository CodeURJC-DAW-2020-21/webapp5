import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Team } from "../../../models/team.model";
import { TeamService } from "../../../services/teams.service";

@Component({
    selector: 'team',
    templateUrl: './team.component.html'
})

export class TeamComponent {
    
    active = 1;
    team: Team;
    id: number;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private teamService: TeamService){
        this.id = activatedRoute.snapshot.params.id;
    }

    ngOnInit(): void {
        this.teamService.getTeam(this.id).subscribe(
          data => {
            this.team = data;
          },
          error => console.error(error)
        );
      }

      getTeam() {
        return this.team;
      }
}