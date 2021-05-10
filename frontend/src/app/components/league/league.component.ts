
import { Component} from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from "@angular/router";
import { Injectable } from '@angular/core';
import { TeamService } from "../../services/teams.service";
import { Team } from "src/app/models/team.model";

@Component({
    selector: 'league',
    templateUrl: './league.component.html'
})

@Injectable({ providedIn: 'root' })
export class LeagueComponent{
	
  public teams: Team[]=[];

	constructor(private router: Router, private activatedRoute: ActivatedRoute,private httpClient: HttpClient, private teamService: TeamService){}
    
  ngOnInit(): void {

      this.teamService.getLeague().subscribe(
        data => {
          this.teams = data;
          console.log('Teams: ', data);
        },
        error => console.error(error)
      );   
  }

  getTeams(){
    return this.teams;
  }
  getNMatches(team: Team){
    return team.nVictories + team.nLoses;
  }

  teamImage(team: Team){
    return team.image? '/api/teams/' + team.id + '/image' :  '/assets/images/sample_images/team_default.jpg';
  }
}