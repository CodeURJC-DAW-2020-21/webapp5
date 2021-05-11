import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "src/app/services/login.sevice";
import { Team } from "../../models/team.model";
import { TeamService } from "../../services/teams.service";


@Component({
    selector: 'teams',
    templateUrl: './teams.component.html'
})

export class TeamsComponent {
    
    teamsPages: Team[] = [];
    teams: Team[] = [];
    numPage:number;
    maxPages:number;
    canLoadMore=true;

    constructor(private router: Router, private teamService: TeamService, public loginService: LoginService){
    }

    ngOnInit(): void {
      this.numPage = 0;
      this.teamService.getTeamsPage(this.numPage).subscribe(
        data => {
          this.teamsPages = data;
          console.log('Team Page: ', data);
        },
        error => console.error(error)
      );

      this.teamService.getTeams().subscribe(
        data => {
          this.teams = data;
          console.log('Teams: ', data);
          this.maxPages = data.length/4;
        },
        error => console.error(error)
      );   
    }

    getTeamsPage() {
      return this.teamsPages;
    }

    getTeams(){
      return this.teams;
    }

    goToNewTeam(){
      this.router.navigate(['/newTeam'])
    }

    loadMoreTeams(){
      this.numPage+=1;
      if(this.numPage <= this.maxPages){
        this.teamService.getTeamsPage(this.numPage).subscribe(
          data => {
            const more: Team[] = data;
            for (const t in more){
              this.teamsPages.push(more[t]);
            }
            console.log('Page: ', this.numPage, ', Data:', more, 'TeamPage:', this.teamsPages);
          },
          error => console.error(error)
        );
      }else {
        this.canLoadMore = false;
      }
    }

    getCanLoadMore(){
      return this.canLoadMore;
    }

    teamImage(team: Team){
      return team.image? '/api/teams/' + team.id + '/image' :  'assets/images/sample_images/team_default.jpg';
    }

    goToLogin(){this.router.navigate(['/login']);}
}