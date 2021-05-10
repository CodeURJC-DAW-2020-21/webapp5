import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Tournament } from '../models/tournament.model';
import {catchError, map} from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { Match } from '../models/match.model';


const BASE_URL = '/api/tournaments/';
const PAGE_URL = '/api/tournaments/pages'

@Injectable({ providedIn: 'root' })
export class TournamentService{

    constructor(private httpClient: HttpClient){}

    getTournaments() {
        console.log('Get to: ' + BASE_URL);
        return this.httpClient.get<Tournament[]>(BASE_URL).pipe(
          map(response => response),
          catchError(error => throwError('Server error'))
        );
      }

    getTournament(id: number | string): Observable<Tournament> {
        return this.httpClient.get(BASE_URL + id).pipe(
          map(response => response),
          catchError(error => throwError('Server error'))
        ) as Observable<Tournament>;
    }

    getTournamentsPage(pageNumber: number): Observable<Tournament[]>{
        return this.httpClient.get(PAGE_URL + '?numPage=' + pageNumber).pipe(
            map(response => response),
            catchError(error => throwError('Server error'))
        ) as Observable<Tournament[]>;
    } 

    newTournament(tournament: Tournament) {
        console.log(tournament);
        return this.httpClient.post(BASE_URL, { name: tournament.name, description: tournament.description,  maxPlayers: tournament.maxPlayers, iniDate: tournament.iniDate, endDate: tournament.endDate, game: tournament.game}).pipe(
            catchError(error => throwError('Server error'))
        );
    }

    nextRound(tournament: Tournament){
        console.log(tournament);
        return this.httpClient.post(BASE_URL + tournament.id + '/rounds', {}).pipe(
            catchError(error => throwError('Server error'))
        );
    }

    submitScore(tournament: Tournament, match: Match, roundId: number){
        return this.httpClient.put(BASE_URL + tournament.id + '/rounds/' + roundId + '/matches/' + match.id, {score1: match.score1, score2: match.score2}).pipe(
            catchError(error => throwError('Server error'))
        );
    }

    joinTournament(tournament: Tournament){
        return this.httpClient.post(BASE_URL + tournament.id + '/participants', {}).pipe(
            catchError(error => throwError('Server error')),
        );
    }
}
