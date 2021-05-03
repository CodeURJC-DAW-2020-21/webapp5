import { Team } from '../../models/team.model'
import { User } from '../../models/user.model'
import { Game } from '../../models/game.model'
import { Round } from '../../models/round.model'

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

