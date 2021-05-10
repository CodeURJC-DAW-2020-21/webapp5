import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { Team } from "src/app/models/team.model";
import { Tournament } from "src/app/models/tournament.model";
import { LoginService } from "src/app/services/login.sevice";
import { TournamentService } from "src/app/services/tournaments.service";

@Component({
    selector: 'tCollapse',
    templateUrl: './collapse.component.html'
  })
  export class TCollapse{
    
    public isCollapsed: boolean[] = [];

    tournamentsPages: Tournament[] = [];
    tournaments: Tournament[] = [];
    numPage:number;
    maxPages:number;
    canLoadMore=true;

    constructor(private router: Router, private tournamentService: TournamentService, public loginService: LoginService){
    }

    ngOnInit(): void {
        this.numPage = 0;
        this.tournamentService.getTournamentsPage(this.numPage).subscribe(
          data => {
            this.tournamentsPages = data;
            console.log('Tournaments Page: ', data);
          },
          error => console.error(error)
        );
  
        this.tournamentService.getTournaments().subscribe(
          data => {
            this.tournaments = data;
            console.log('Tournaments: ', data);
            this.maxPages = data.length/4;
          },
          error => console.error(error)
        );   
      }
  
      getTournamentsPage() {
        return this.tournamentsPages;
      }
  
      getTournaments(){
        return this.tournaments;
      }
  
      goToNewTournament(){
        this.router.navigate(['/newTournament'])
      }
  
      loadMoreTournaments(){
        this.numPage+=1;
        if(this.numPage <= this.maxPages){
          this.tournamentService.getTournamentsPage(this.numPage).subscribe(
            data => {
              const more: Tournament[] = data;
              for (const t in more){
                this.tournamentsPages.push(more[t]);
              }
              console.log('Page: ', this.numPage, ', Data:', more, 'TournamentPage:', this.tournamentsPages);
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

      join(tournament: Tournament){
        this.tournamentService.joinTournament(tournament).subscribe(
          
        );
        location.reload();
      }
      
      teamImage(team: Team){
        return team.image? '/api/teams/' + team.id + '/image' :  '/assets/images/sample_images/team_default.jpg';
      }
      
  }