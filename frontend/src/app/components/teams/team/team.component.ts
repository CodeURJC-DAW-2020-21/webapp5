import { Component, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Team } from "../../../models/team.model";
import { TeamService } from "../../../services/teams.service";

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

	  getChart(){
		return this.team;
	  }

	  acceptRejectMember(userId: number, teamId:string|number, accept: boolean){
		this.teamService.acceptRejectMember(userId , teamId, accept).subscribe(
            (team:Team) => this.router.navigate(['/teams/teamId']),
            error => {
                console.error(error);             
            });
	  }

	  addAdminToTeam(userId: string|number, teamId: number | string){
		this.teamService.addAdminToTeam(userId, teamId).subscribe(
            (team:Team) => this.router.navigate(['/teams/teamId']),
            error => {
                console.error(error);             
            });
	  }

	  kickMemberFromTeam(userId: number, teamId: number | string){
		this.teamService.kickMemberFromTeam(userId, teamId).subscribe(
            (team:Team) => this.router.navigate(['/teams/teamId']),
            error => {
                console.error(error);             
            });
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
            _ => this.router.navigate(['/teams/' + team.id]),
            error => alert('Error uploading team image: ' + error)
          );
        }
      }
}