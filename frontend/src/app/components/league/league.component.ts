
import { Component} from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from "@angular/router";
import { Injectable } from '@angular/core';
import { TeamService } from "../../services/teams.sevice";

@Component({
    selector: 'league',
    templateUrl: './league.component.html'
})

@Injectable({ providedIn: 'root' })
export class LeagueComponent{
	
	constructor(private router: Router, private activatedRoute: ActivatedRoute,private httpClient: HttpClient, private teamService: TeamService){}

	getLeague(){
        return this.teamService.getLeague().subscribe(
		error => {
                console.error(error);             
            });
    }
}