import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { User } from "src/app/models/user.model";
import { UserService } from "src/app/services/users.service";

@Component({
    selector: 'settings',
    templateUrl: './settings.component.html'
})

export class SettingsComponent {

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
}