import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { User } from "src/app/models/user.model";
import { UserService } from "src/app/services/users.service";

@Component({
    selector: 'profile',
    templateUrl: './profile.component.html'
})

export class ProfileComponent {

    user: User;
    name: string;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private usersService: UserService){
        this.name = activatedRoute.snapshot.params.name;
    }

    ngOnInit(): void {
        this.usersService.getUser(this.name).subscribe(
          data => {
            this.user = data;
          },
          error => console.error(error)
        );
      }

      getUser() {
        return this.user;
      }

      userImage(){
        return this.user.image? '/api/users/' + this.user.name + '/image' :  '/assets/images/sample_images/user_default.jpg';
      }

      hasTeams(){
        return this.user.team != null;
      }

      teamImage(){
        return this.user.team.image? '/api/teams/' + this.user.team.id + '/image' :  '/assets/images/sample_images/team_default.jpg';
      }

      hasRiot(){
        return this.user.riot != null;
      }

      hasBlizzard(){
        return this.user.blizzard != null;
      }

      hasPsn(){
        return this.user.psn != null;
      }

      hasXbox(){
        return this.user.xbox != null;
      }

      hasSteam(){
        return this.user.steam!= null;
      }
}