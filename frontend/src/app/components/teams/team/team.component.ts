import { Component, ViewChild} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Team } from "../../../models/team.model";
import { TeamService } from "../../../services/teams.service";
import { LoginService } from "../../../services/login.sevice";
import { User } from "src/app/models/user.model";

@Component({
    selector: 'team',
    templateUrl: './team.component.html'
})

export class TeamComponent {
      
    active = 'abstract';
    team: Team;
    id: number;

    @ViewChild("file")
    file:any;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private teamService: TeamService,public loginService: LoginService){
        this.id = activatedRoute.snapshot.params.id;
    }

    ngOnInit(): void {
      console.log(this.loginService.currentUser());
      this.teamService.getTeam(this.id).subscribe(
          data => {
            this.team = data;
            this.teamService.getGraph(this.id).subscribe(
              data2 => {
                this.team.nVictories = data2.nVictories;
                this.team.nLoses = data2.nLoses;
                this.team.recordV = data2.recordV;
                this.team.recordL = data2.recordL;
              }
            )
          },
          error => console.error(error)
        );
      }

    getTeam() {
      return this.team;
    }

	  getChart(){
		return this.team;
	  }

    isMember(){
      if(this.loginService.currentUser().team === null){
        return false;
      } else {
        return (this.loginService.currentUser().team.name === this.team.name);
      }
    }

    hasNoTeam(user:User){
      return (user.team === null);
    }

	  requestToJoin(teamId: number){
		this.teamService.requestToJoin(teamId).subscribe(
			(team: Team)  => this.router.navigate(['/teams/' + teamId]),
			error => {
                console.error(error);             
            });
      location.reload();
	  }

	  addGame(teamId: number, gameName:string){
		this.teamService.addGame(teamId, gameName).subscribe(
			(team: Team)  => this.router.navigate(['/teams/' + teamId]),
			error => {
                console.error(error);             
            });
      location.reload();
	  }

	  leaveTeam(teamId: number){
		this.teamService.leaveTeam(teamId).subscribe(
			(team: Team)  => this.router.navigate(['/teams/' + teamId]),
			error => {
                console.error(error);             
            });
            location.reload();
	  }

	  acceptRejectMember(userId: number, teamId:string|number, accept: boolean){
		this.teamService.acceptRejectMember(userId , teamId, true).subscribe(
            (team:Team) => this.router.navigate(['/teams/' + teamId]),
            error => {
                console.error(error);             
            });
            location.reload();
	  }

	  addAdminToTeam(userId: string|number, teamId: number | string){
		this.teamService.addAdminToTeam(userId, teamId).subscribe(
            (team:Team) => this.router.navigate(['/teams/' + teamId]),
            error => {
                console.error(error);             
            });
            location.reload();
	  }

	  kickMemberFromTeam(userId: number, teamId: number | string){
		this.teamService.kickMemberFromTeam(userId, teamId).subscribe(
            (team:Team) => this.router.navigate(['/teams/' + teamId]),
            error => {
                console.error(error);             
            });
            location.reload();
	  }

    teamImage(){
      return this.team.image? 'api/teams/' + this.team.id + '/image' :  '/assets/images/sample_images/team_default.jpg';
    }

    uploadTeamImage(team: Team): void{
      const image = this.file.nativeElement.files[0];
      if (image) {
        let formData = new FormData();
        formData.append("imageFile", image);
        this.teamService.setTeamImage(team, formData).subscribe(
          _ => {
            this.router.navigate(['/teams/' + team.id]),
            alert('Team Image has been changed')
          },
          error => alert('Error uploading team image: ' + error)
        );
      }
    }

    userImage(user: User){
      return user.image? 'api/user/' + user.name + '/image' :  '/assets/images/sample_images/user_default.jpg';
    }
}