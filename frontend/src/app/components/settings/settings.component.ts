import { Component, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { User } from "src/app/models/user.model";
import { LoginService } from "src/app/services/login.sevice";
import { UserService } from "src/app/services/users.service";

@Component({
    selector: 'settings',
    templateUrl: './settings.component.html'
})

export class SettingsComponent {

    user: User;
    newUserData: User;
    name: string;

    @ViewChild("file")
    file: any;

    constructor(private router: Router, public activatedRoute: ActivatedRoute, private loginService: LoginService, private usersService: UserService){
        this.name = activatedRoute.snapshot.params.name;
        this.newUserData={
          name: '',
          email: '',
          encodedPassword: '',
          riot: '',
          blizzard: '',
          psn: '',
          xbox: '',
          steam: '',
          team: null,
          roles: null,
          image: false
        };
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

      updateUser(){
        this.usersService.updateUser(this.newUserData, this.name).subscribe(
          (user : User) => this.uploadImage(user),
          error => alert('Error updating the user: ' + error)
        );
      }

      uploadImage(user: User){
        const image = this.file.nativeElement.files[0];
        if (image) {
          let formData = new FormData();
          formData.append("imageFile", image);
          this.usersService.setUserImage(user, this.name, formData).subscribe(
            _ => this.afterUploadImage(),
            error => alert('Error uploading user image: ' + error)
          );
        } else {
          this.afterUploadImage();
        }
      }

      private afterUploadImage(){
        alert('You have changed your profile correctly, session is going to close to prevent errors');
        this.loginService.logOut();
        this.router.navigate(['/index']);
      }

      userImage(){
        return this.user.image? 'api/user/' + this.user.name + '/image' :  '/assets/images/sample_images/user_default.jpg';
      }
}