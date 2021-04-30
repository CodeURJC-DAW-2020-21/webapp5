import { Team } from './Team.model'
import { User } from './LgUser.model'
import { Game } from './Game.model'
import { Round } from './Round.model'


export interface Tournament {
	
    id: number;
    name: string;
	description: string;

    currentPlayers: number;
    maxPlayers: number;
    iniDate: string ;

    endDate: string ;
    participants : Team[];
    winner: Team;

    rounds: Round[];
    game: Game;
    roundNumber: number;

    started: boolean;
    finished: boolean;
    admin: User;



	checked: boolean;
}

