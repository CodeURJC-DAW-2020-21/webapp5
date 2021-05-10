import {Team} from './team.model'
import {Match} from './match.model'

export interface Round {
    id?: number;
    participants: Team[];
    winners: Team[];
    oddTeam?: Team;
    matches: Match[];
    numRound: number;
}
	