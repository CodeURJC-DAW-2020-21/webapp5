import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Team } from '../models/team.model';
import {catchError, map} from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

const BASE_URL = '/api/teams/';

@Injectable({ providedIn: 'root' })
export class TeamService{

    constructor(private http: HttpClient){
    }

    getTeams() {
        return this.http.get(BASE_URL).pipe(
          catchError(error => throwError('Server error'))
        ) as Observable<Team[]>;
    }

    getTeam(id: number | string): Observable<Team> {
        return this.http.get(BASE_URL + id).pipe(
          map(response => response),
          catchError(error => throwError('Server error'))
        ) as Observable<Team>;
    }

    newTeam(team:Team) {
        return this.http.post(BASE_URL, team).pipe(
            catchError(error => throwError('Server error'))
        );
    }
}