import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Team } from '../models/team.model';
import {catchError, map} from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService{

    constructor(private httpClient: HttpClient){}

    getUser(name: string): Observable<User>{
        return this.httpClient.get(BASE_URL + name).pipe(
            catchError(error => throwError('Server error'))
        ) as Observable<User>;
    } 

}
