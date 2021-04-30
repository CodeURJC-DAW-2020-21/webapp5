

import {Team} from './Team.model'
import {Match} from './Match.model'
export interface Round {
    id : number;
    participants: Team[];
    winners: Team[];
    oddTeam: Team;
    matches : Match[];
    numRound: number;
}
	