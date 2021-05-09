import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Team } from '../models/team.model';
import { User } from '../models/user.model';
import {catchError, map} from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

const BASE_URL = '/api/teams/';
const PAGE_URL = '/api/teams/pages'

@Injectable({ providedIn: 'root' })
export class TeamService{

    constructor(private httpClient: HttpClient){
    }

    getTeams(): Observable<Team[]> {
        return this.httpClient.get(BASE_URL).pipe(
          catchError(error => throwError('Server error'))
        ) as Observable<Team[]>;
    }

    getTeam(id: number | string): Observable<Team> {
        return this.httpClient.get(BASE_URL + id).pipe(
          map(response => response),
          catchError(error => throwError('Server error'))
        ) as Observable<Team>;
    }

	getGraph(id: number | string): Observable<Team> {
        return this.httpClient.get(BASE_URL + id + '/chart').pipe(
          map(response => response),
          catchError(error => throwError('Server error'))
        ) as Observable<Team>;
    }
	
	acceptRejectMember(userId: number | string, teamId: number | string , accept: boolean){
		return this.httpClient.post(BASE_URL + teamId + '/requests/' + '?accept=' + accept, {id: userId}).pipe(
            catchError(error => throwError('Server error'))
        );
	}
	
	addAdminToTeam(userId: number | string, teamId: number | string){
		return this.httpClient.post(BASE_URL + teamId + '/admins/', {id: userId}).pipe(
            catchError(error => throwError('Server error'))
        );
	}
	
	kickMemberFromTeam(userId: number | string, teamId: number | string){
		return this.httpClient.put(BASE_URL + teamId + '/members/' + userId,{}).pipe(
            catchError(error => throwError('Server error'))
        );
	}
	
    getTeamsPage(pageNumber: number): Observable<Team[]>{
        return this.httpClient.get(PAGE_URL + '?numPage=' + pageNumber).pipe(
            map(response => response),
            catchError(error => throwError('Server error'))
        ) as Observable<Team[]>;
    } 

    newTeam(team: Team) {
        console.log(team);
        return this.httpClient.post(BASE_URL, { name: team.name, description: team.description }).pipe(
            catchError(error => throwError('Server error'))
        );
    }

    setTeamImage(team: Team, formData: FormData) {
		return this.httpClient.post(BASE_URL + team.id + '/image', formData)
			.pipe(
				catchError(error => throwError('Server error'))
			);
	}
}