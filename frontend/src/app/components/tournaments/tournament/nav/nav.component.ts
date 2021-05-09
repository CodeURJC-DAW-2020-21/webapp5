import {Component} from '@angular/core';

import { ActivatedRoute, Router } from "@angular/router";
import { Tournament } from "src/app/models/tournament.model";
import { LoginService } from "src/app/services/login.sevice";
import { TournamentService } from "src/app/services/tournaments.service";

@Component({
  selector: 'tNav',
  templateUrl: './nav.component.html'
})
export class TNav {
  active = 1;
  tabs = [2, 3, 4, 5, 6];
  counter = this.tabs.length + 1;

  tournament: Tournament;
  id: number;

  constructor(private router: Router, activatedRoute: ActivatedRoute, private tournamentService: TournamentService, public loginService: LoginService){
      this.id = activatedRoute.snapshot.params.id;
  }

  ngOnInit(): void {
      this.tournamentService.getTournament(this.id).subscribe(
        data => {
          this.tournament = data;
        },
        error => console.error(error)
      );
  }

  add(event: MouseEvent) {
    this.tabs.push(this.counter++);
    event.preventDefault();
  }
}