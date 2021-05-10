import { Tournament } from './tournament.model'
import { User } from './user.model'
import {Game} from './game.model'

export interface Team {
    id?: number;
	name: string;
    description: string;
    tournaments: Tournament[];
    games: Game;
	creator: User;
	users: User[];
	admins: User[];
	requests: number;
	image: boolean;
	nVictories: number;
    nLoses: number;
	recordV: string;
	recordL: string;
}
	
	