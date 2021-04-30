import { Tournament } from './Tournament.model'
import { User } from './LgUser.model'
import {Game} from './Game.model'


export interface Team {
    id : number;
	name : string;
    description : string;
    tournaments : Tournament[];
    games: Game;
	creator : User;
	users: User[];
	admins: User[];
	requests: number;
	image: boolean;
	nVictories : number;
    nLoses : number;
	recordV : string;
	recordL : string;
}
	
	