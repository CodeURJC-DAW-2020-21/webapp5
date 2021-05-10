import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({selector: 'ngcarousel', templateUrl: './carousel.component.html'})
export class CarouselComponent {
  image = ["assets/images/soccer/main-slider.jpg"];

  constructor(private router: Router){}

  goToSignup(){this.router.navigate(['/signup']);}
  goToTournaments(){this.router.navigate(['/tournaments']);}
  goToTeams(){this.router.navigate(['/teams']);}
  goToLeague(){this.router.navigate(['/league']);}
}