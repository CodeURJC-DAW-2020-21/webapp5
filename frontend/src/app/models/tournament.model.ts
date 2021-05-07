import { Team } from './team.model'
import { User } from './user.model'
import { Game } from './game.model'
import { Round } from './round.model'

export interface Tournament {
    id?: number;
    name: string;
	description: string;
    currentPlayers: number;
    maxPlayers: number;
    iniDate: string;
    endDate: string;
    participants: Team[];
    winner: Team;
    rounds: Round[];
    game: Game;
    roundNumber: number;
    started: boolean;
    finished: boolean;
    admin: User;
	checked: boolean;
}

