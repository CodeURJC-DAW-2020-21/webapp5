import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Tournament } from '../models/tournament.model';
import {catchError, map} from 'rxjs/operators';
import { throwError } from 'rxjs';



@Injectable({ providedIn: 'root' })
export class TournamentService{

    private readonly tournamentsUrl: string;
    private readonly tournamentUrl: string;

    constructor(private http: HttpClient){
        this.tournamentsUrl = '/api/tournaments/';
        this.tournamentUrl = '/api/tournament/';
    }

    getTournaments() {
        console.log('Get to: ' + this.tournamentsUrl);
        return this.http.get<Tournament[]>(this.tournamentsUrl).pipe(
          map(response => response),
          catchError(error => throwError('Server error'))
        );
      }
}
