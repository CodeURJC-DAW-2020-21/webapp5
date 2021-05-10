import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import {catchError, map} from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService{

    constructor(private httpClient: HttpClient){}

    getUser(name: string): Observable<User> {
        return this.httpClient.get(BASE_URL + name).pipe(
            catchError(error => throwError('Server error'))
        ) as Observable<User>;
    }

    newUser(user: User){
        return this.httpClient.post(BASE_URL, user).pipe(
            catchError(error => throwError('Server error'))
        ) as Observable<User>;   
    }

    updateUser(user: User, name: string) {
        return this.httpClient.put(BASE_URL + name, { name: user.name, email: user.email, encodedPassword: user.encodedPassword, riot: user.riot, blizzard: user.blizzard, psn: user.psn, xbox: user.xbox, steam: user.steam }).pipe(
                catchError(error => throwError('Server error'))
			) as Observable<User>;
    }
    
    setUserImage(user: User, name: string, formData: FormData) {
		return this.httpClient.post(BASE_URL + name + '/image', formData)
			.pipe(
				catchError(error => throwError('Server error'))
			);
	}

}
